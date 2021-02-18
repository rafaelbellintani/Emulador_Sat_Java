// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import org.w3c.dom.Document;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeTeste;
import br.com.um.controles.ControleTempo;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.SATAssociadoException;
import br.com.satcfe.satbl.modelos.cfe.CFe;
import br.com.satcfe.satbl.excecoes.SATBloqueadoException;
import br.com.satcfe.satbl.excecoes.SATNaoAtivoException;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorEmissaoTeste
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorEmissaoTeste(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String trataMensagem(final String codigoAtivacao, final String numeroSessao, final String dadosVenda, final String aviso) {
        try {
            if (!Configuracoes.SAT.ativado) {
                throw new SATNaoAtivoException();
            }
            if (Configuracoes.SAT.bloqueado) {
                throw new SATBloqueadoException();
            }
            if (ControladorEmuladorOffLine.isAtualizando()) {
                return "09099||SAT-CFe em processo de Atualiza\u00e7\u00e3o.||" + aviso;
            }
            ControleLogs.logar("Carregando CFe Teste.");
            final CFe docCFe = new CFe(dadosVenda);
            if (Configuracoes.SAT.validarCamposCFe) {
                if (!Configuracoes.SAT.associado && !docCFe.getInfCFe().getIde().getCNPJ().equals("00000000000000")) {
                    throw new SATAssociadoException();
                }
                ControleLogs.logar("Validando CFe Teste.");
                final String validacao = docCFe.validar();
                if (!validacao.equals("1000")) {
                    throw new ErroDesconhecidoException();
                }
            }
            ControleLogs.logar("Completando CFe Teste.");
            docCFe.completar(2, "123456");
            if (docCFe.getInfCFe().getIde().getQrCode().length() == 344 && Configuracoes.SAT.VERSAO_LAYOUT_CFE.equals("0.03")) {
                final String qrCode = docCFe.getInfCFe().getIde().getQrCode();
                docCFe.getInfCFe().getIde().setQrCode(String.valueOf(qrCode) + "hygljwuohmmoewarfnmighlxzke7k2bjlto4sb2vltorgm26khhangknnfvpzydt5terudyw5vuvtzlhlqs3qrzvplfnlvw==");
            }
            ControleLogs.logar("Assinando CFe Teste.");
            final ControleAssinaturaXML assinaturaXML = new ControleAssinaturaXML(docCFe.toString());
            final Document docAssinado = assinaturaXML.assinar(ControleDados.gerarHashMD5(codigoAtivacao));
            final String resultado = ControleDados.removerCabecalhoXML(ControleDados.outputXML(docAssinado));
            final String chaveAcesso = docCFe.getInfCFe().getId();
            final String numeroCFe = docCFe.getInfCFe().getIde().getnCFe();
            new ControleNumeracaoSAT().gravarNumeroCFeTeste(numeroCFe);
            ControleLogs.logar("Enviando CFe Teste ao WebService.");
            if (Configuracoes.SAT.emuladorOffLine) {
                ControleLogs.logar("TESTE EMITIDO COM SUCESSO");
                final StringBuilder sb = new StringBuilder("09000|Emitido com sucesso||");
                sb.append(String.valueOf(aviso) + "|");
                sb.append(StringUtil.base64Encode(resultado.getBytes("utf-8")));
                sb.append("|").append(ControleTempo.getTimeStamp());
                sb.append("|").append(numeroCFe);
                sb.append("|").append(chaveAcesso);
                return sb.toString();
            }
            WebServiceCFeTeste wsTeste = null;
            try {
                wsTeste = new WebServiceCFeTeste();
                wsTeste.setCFe(resultado);
                wsTeste.setIdLote(ControleSeguranca.gerarIdLoteTeste());
                if (!ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA)) {
                    wsTeste.setOptTeste("1");
                }
                else {
                    wsTeste.setOptTeste("2");
                }
                if (!wsTeste.consumirWebService()) {
                    ControleLogs.logar("Erro na comunica\u00e7\u00e3o.");
                    throw new ErroComunicacaoRetaguardaException();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new ErroComunicacaoRetaguardaException();
            }
            if (wsTeste.getcStat() != null && (wsTeste.getcStat().equals("125") || wsTeste.getcStat().equals("126"))) {
                ControleLogs.logar("TESTE EMITIDO COM SUCESSO");
                final StringBuilder sb2 = new StringBuilder("09000|Emitido com sucesso||");
                sb2.append(String.valueOf(aviso) + "|");
                sb2.append(StringUtil.base64Encode(resultado.getBytes("utf-8")));
                sb2.append("|").append(ControleTempo.getTimeStamp());
                sb2.append("|").append(numeroCFe);
                sb2.append("|").append(chaveAcesso);
                return sb2.toString();
            }
            if (wsTeste.getcStat() != null && wsTeste.getcStat().equals("208")) {
                throw new SATNaoAtivoException();
            }
            if (wsTeste.getxMotivo() != null) {
                ControleLogs.logar("Erro: " + wsTeste.getxMotivo());
                throw new ErroDesconhecidoException();
            }
            if (wsTeste.getResponseCode() != null || wsTeste.getResponseMessage() != null) {
                ControleLogs.logar("Erro: " + wsTeste.getResponseCode() + " - " + wsTeste.getResponseMessage());
                throw new ErroDesconhecidoException();
            }
            throw new ErroDesconhecidoException();
        }
        catch (SATBloqueadoException e3) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O: SAT est\u00e1 bloqueado");
            if (Parametrizacoes.autorBloqueio == 1) {
                return "09006|SAT bloqueado pelo contribuinte||" + aviso;
            }
            return "09007|SAT bloqueado pela SEFAZ||" + aviso;
        }
        catch (NoSuchAlgorithmException e4) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09099|Par de chaves corrompido.||" + aviso;
        }
        catch (SATNaoAtivoException e5) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09002|SAT-CF-e ainda n\u00e3o ativado.||" + aviso;
        }
        catch (InvalidKeySpecException e6) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09099|Par de chaves corrompido.||" + aviso;
        }
        catch (DataLengthException e7) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (IllegalStateException e8) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (InvalidCipherTextException e9) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (SATAssociadoException e10) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            return "09099|SAT N\u00e3o est\u00e1 associado.||" + aviso;
        }
        catch (Exception e2) {
            ControleLogs.logar("ERRO NA EMISS\u00c3O DE TESTE");
            e2.printStackTrace();
            return "09099|Erro desconhecido.||" + aviso;
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao, final String dadosVenda) {
        final String r = "true";
        try {
            if (!Configuracoes.SAT.ativado) {
                ControleLogs.logar("ERRO: SAT nao ativado.");
                return "09002|SAT-CF-e ainda n\u00e3o ativado.";
            }
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "09099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "09001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
            if (dadosVenda.length() <= 0) {
                ControleLogs.logar("ERRO: Dados Venda Invalido");
                return "09099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "09099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        return r;
    }
}
