// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import org.w3c.dom.Document;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.modelos.MensagensErroAlerta;
import br.com.um.controles.ValidadorSchema;
import br.com.satcfe.satbl.modelos.cfe.CFe;
import br.com.satcfe.satbl.excecoes.SATBloqueadoException;
import br.com.satcfe.satbl.excecoes.SATAssociadoException;
import br.com.satcfe.satbl.excecoes.SATNaoAtivoException;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorEmissao
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorEmissao(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String trataMensagem(final String codigoAtivacao, final String dadosVenda, final String aviso) {
        try {
            if (!Configuracoes.SAT.ativado) {
                throw new SATNaoAtivoException();
            }
            if (!Configuracoes.SAT.associado) {
                throw new SATAssociadoException();
            }
            if (Configuracoes.SAT.bloqueado) {
                throw new SATBloqueadoException();
            }
            if (ControladorEmuladorOffLine.isAtualizando()) {
                return "06099|0000|SAT-CFe em processo de Atualiza\u00e7\u00e3o.||" + aviso;
            }
            ControleArquivos.escreverCaracteresArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_DEBUG) + "dadosVenda.xml", dadosVenda.toCharArray(), false);
            ControleLogs.logar("Carregando CFe.");
            String codFalha = "0000";
            String codigo = "1000";
            CFe docCFe = null;
            try {
                docCFe = new CFe(dadosVenda);
            }
            catch (Exception e) {
                codigo = "1999";
                ControleLogs.logar("Erro: Falha na estrutura do XML de venda.");
                e.printStackTrace();
            }
            if (codigo.equals("1000") && Configuracoes.SAT.validarCamposCFe) {
                ControleLogs.logar("Validando Campos do CFe.");
                codigo = docCFe.validar();
            }
            Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/emissao_cfe_0006.xsd").getPath();
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) == 0.07) {
                Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/emissao_cfe_0007.xsd").getPath();
            }
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) == 0.08) {
                Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/emissao_cfe_0008.xsd").getPath();
            }
            if (codigo.equals("1000") && Configuracoes.SAT.validarSchema && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA)) {
                final String xsd = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA);
                final ValidadorSchema vs = new ValidadorSchema();
                vs.setSchema(xsd);
                vs.setXml(dadosVenda);
                if (!vs.validar()) {
                    codigo = "1999";
                    ControleLogs.logar("Erro: Falha na estrutura do XML de venda.");
                    if (vs.getErro() != null) {
                        ControleLogs.logar("Erro: " + vs.getErro());
                    }
                }
            }
            if (!codigo.equals("1000")) {
                final String msg = MensagensErroAlerta.getMensagemErroAlerta(codigo);
                if (!codigo.equals("1000") && !codigo.equals("1234")) {
                    ControleLogs.logar("ERRO NA EMISS\u00c3O: " + codigo + ": " + msg);
                    return "06010|" + codigo + "|" + msg + "||" + aviso;
                }
                if (codigo.equals("1234")) {
                    codFalha = "1234";
                }
            }
            ControleLogs.logar("Processando as informa\u00e7\u00f5es CFe.");
            docCFe.completar(1, "123456");
            if (Configuracoes.SAT.validarCamposCFe) {
                ControleLogs.logar("Continuando a Valida\u00e7\u00e3o do CFe.");
                codigo = docCFe.validarCamposAposCalculo();
                final String msg = MensagensErroAlerta.getMensagemErroAlerta(codigo);
                if (!codigo.equals("1000") && !codigo.equals("1234")) {
                    ControleLogs.logar("ERRO NA EMISS\u00c3O: " + codigo + ": " + msg);
                    return "06010|" + codigo + "|" + msg + "||" + aviso;
                }
                if (codigo.equals("1234")) {
                    codFalha = "1234";
                }
            }
            final String chaveAcesso = docCFe.getInfCFe().getId();
            final String xNome = docCFe.getInfCFe().getEmit().getxNome();
            final String dEmi = docCFe.getInfCFe().getIde().getdEmi();
            final String hEmi = docCFe.getInfCFe().getIde().gethEmi();
            String dest = (docCFe.getInfCFe().getDest() != null) ? docCFe.getInfCFe().getDest().getCNPJ() : null;
            if (dest == null) {
                dest = ((docCFe.getInfCFe().getDest() != null) ? docCFe.getInfCFe().getDest().getCPF() : null);
            }
            final String vCFe = docCFe.getInfCFe().getTotal().getvCFe();
            final String numeroCFe = docCFe.getInfCFe().getIde().getnCFe();
            final String caminho = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + chaveAcesso + ".xml";
            ControleLogs.logar("Assinando CFe.");
            final ControleAssinaturaXML assinaturaXML = new ControleAssinaturaXML(docCFe.toString());
            final Document docAssinado = assinaturaXML.assinar(ControleDados.gerarHashMD5(codigoAtivacao));
            final String resultado = ControleDados.outputXML(docAssinado);
            ControleArquivos.escreverCaracteresArquivoUTF8(caminho, resultado.toCharArray(), false);
            final String timeStamp = String.valueOf(dEmi) + hEmi;
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE, timeStamp.toCharArray());
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CHAVE_ULTIMO_CFE, chaveAcesso.toCharArray());
            this.salvarDadosCFeParaCancelamento(String.valueOf(chaveAcesso) + "|" + vCFe + "|" + timeStamp + "|" + xNome + "|" + dEmi + "|" + hEmi + "|" + dest);
            new ControleNumeracaoSAT().gravarNumeroCFe(numeroCFe);
            final String arqBase64 = StringUtil.base64Encode(resultado.getBytes("utf-8"));
            String cpfCnpjValue = docCFe.getInfCFe().getDest().getCNPJ();
            if (cpfCnpjValue == null) {
                cpfCnpjValue = docCFe.getInfCFe().getDest().getCPF();
            }
            if (cpfCnpjValue == null) {
                cpfCnpjValue = "";
            }
            final String qrCode = docCFe.getInfCFe().getIde().getQrCode();
            ControleLogs.logar("EMITIDO COM SUCESSO");
            return "06000|" + codFalha + "|Emitido com sucesso + conte\u00fado notas||" + aviso + "|" + arqBase64 + "|" + timeStamp + "|" + chaveAcesso + "|" + vCFe + "|" + cpfCnpjValue + "|" + qrCode;
        }
        catch (SATBloqueadoException e2) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O: SAT est\u00e1 bloqueado");
            e2.printStackTrace();
            if (Parametrizacoes.autorBloqueio == 1) {
                return "06006|0000|SAT bloqueado pelo contribuinte||" + aviso;
            }
            if (Configuracoes.SAT.autoBloqueado) {
                return "06008|0000|SAT bloqueado por falta de comunica\u00e7\u00e3o||" + aviso;
            }
            return "06007|0000|SAT bloqueado pela SEFAZ||" + aviso;
        }
        catch (SATAssociadoException e3) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O: SAT n\u00e3o est\u00e1 Associado ao AC");
            e3.printStackTrace();
            return "06003|0000|SAT n\u00e3o vinculado ao AC||" + aviso;
        }
        catch (SATNaoAtivoException e4) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O: SAT n\u00e3o est\u00e1 Ativado");
            e4.printStackTrace();
            return "06002|0000|SAT ainda n\u00e3o ativado.||" + aviso;
        }
        catch (NoSuchAlgorithmException e5) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O");
            e5.printStackTrace();
            return "06099|0000|Falha na Assinatura.||" + aviso;
        }
        catch (InvalidKeySpecException e6) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O");
            e6.printStackTrace();
            return "06099|0000|Par de chaves corrompido.||" + aviso;
        }
        catch (Exception e7) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O");
            e7.printStackTrace();
            return "06099|0000|Erro desconhecido.||" + aviso;
        }
    }
    
    private void salvarDadosCFeParaCancelamento(final String dados) {
        try {
            ControleArquivos.escreverCaracteresArquivoASCII(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO, (String.valueOf(dados) + "\n").toCharArray(), true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao, final String dadosVenda) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "06099|0000|Numero de Sessao Invalido";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "06001|0000|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
            if (dadosVenda.length() <= 0) {
                ControleLogs.logar("ERRO: Dados Venda Invalido");
                return "06099|0000|Dados Venda Invalido";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "06099|0000|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        return r;
    }
}
