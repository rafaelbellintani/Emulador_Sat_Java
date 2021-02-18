// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import org.w3c.dom.Document;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import java.security.NoSuchAlgorithmException;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleDados;
import br.com.um.controles.ValidadorSchema;
import br.com.satcfe.satbl.modelos.cfecancelamento.CFeCanc;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.excecoes.SATBloqueadoException;
import br.com.satcfe.satbl.excecoes.SATAssociadoException;
import br.com.satcfe.satbl.excecoes.SATNaoAtivoException;
import br.com.satcfe.satbl.Configuracoes;
import java.util.Calendar;

public class ControladorCancelamento
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorCancelamento(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String trataMensagem(final String codigoAtivacao, final String chaveAcessoCancelar, final String dadosCanc, final String aviso) {
        final Calendar hoje = Calendar.getInstance();
        final Calendar novaRedacao = Calendar.getInstance();
        novaRedacao.set(2016, 0, 1, 0, 0, 0);
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
                return "07098||SAT em processamento. Tente novamente.||" + aviso;
            }
            ControleArquivos.escreverCaracteresArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_DEBUG) + "dadosCanc.xml", dadosCanc.toCharArray(), false);
            final long timeStamp = ControleTempo.getCurrentTime();
            if (chaveAcessoCancelar == null || chaveAcessoCancelar.length() != 47 || !ControleSeguranca.validarChaveAcesso(chaveAcessoCancelar)) {
                ControleLogs.logar("ERRO NO CANCELAMENTO: Chave de acesso inv\u00e1lida.");
                if (hoje.after(novaRedacao)) {
                    return "07010|1270|Erro de valida\u00e7\u00e3o do conte\u00fado.||" + aviso;
                }
                return "07007|1270|Erro de valida\u00e7\u00e3o do conte\u00fado.||" + aviso;
            }
            else {
                final String[] informacoesCancelamento = recuperarCFeParaCancelamento(chaveAcessoCancelar);
                if (informacoesCancelamento == null || informacoesCancelamento.length < 6) {
                    ControleLogs.logar("ERRO NO CANCELAMENTO: N\u00e3o h\u00e1 cupons para cancelar");
                    return "07099||Erro desconhecido no cancelamento.||" + aviso;
                }
                ControleLogs.logar("Carregando arquivo de cancelamento.");
                CFeCanc docCFeCanc = null;
                String codigo = "1000";
                try {
                    docCFeCanc = new CFeCanc(dadosCanc);
                }
                catch (Exception e) {
                    codigo = "1999";
                    ControleLogs.logar("Erro: Falha na estrutura do XML de Cancelamento.");
                    e.printStackTrace();
                }
                if (codigo.equals("1000") && Configuracoes.SAT.validarCamposCFe) {
                    if (!informacoesCancelamento[0].equals(chaveAcessoCancelar)) {
                        ControleLogs.logar("ERRO NO CANCELAMENTO: cupom n\u00e3o \u00e9 o \u00faltimo cupom enviado");
                        return "07099||Erro desconhecido no cancelamento.||" + aviso;
                    }
                    if ((timeStamp - ControleTempo.parseTimeStamp(informacoesCancelamento[2])) / 1000L > 1800L) {
                        ControleLogs.logar("ERRO NO CANCELAMENTO: Cupom foi cancelado a mais de 30 minutos");
                        if (hoje.after(novaRedacao)) {
                            return "07007|1412|Erro de valida\u00e7\u00e3o do conte\u00fado.||" + aviso;
                        }
                        return "07007|1412|Erro de valida\u00e7\u00e3o do conte\u00fado.||" + aviso;
                    }
                    else {
                        ControleLogs.logar("Validando arquivo de cancelamento.");
                        final String validacao = docCFeCanc.validar();
                        if (!validacao.equals("01000")) {
                            if (hoje.after(novaRedacao)) {
                                return "07010|" + validacao + "|Erro de valida\u00e7\u00e3o do conte\u00fado||" + aviso;
                            }
                            return "07007|" + validacao + "|Erro de valida\u00e7\u00e3o do conte\u00fado||" + aviso;
                        }
                    }
                }
                if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.06) {
                    Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/cancelamento_0006.xsd").getPath();
                }
                else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) == 0.07) {
                    Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/cancelamento_0007.xsd").getPath();
                }
                else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08) {
                    Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = this.getClass().getResource("/res/schemas/cancelamento_0008.xsd").getPath();
                }
                if (codigo.equals("1000") && Configuracoes.SAT.validarSchema && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA)) {
                    final String xsd = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA);
                    final ValidadorSchema vs = new ValidadorSchema();
                    vs.setSchema(xsd);
                    vs.setXml(dadosCanc);
                    if (!vs.validar()) {
                        codigo = "1999";
                        ControleLogs.logar("Erro: Falha na estrutura do XML de Cancelamento.");
                        if (vs.getErro() != null) {
                            ControleLogs.logar("Erro: " + vs.getErro());
                        }
                        return "07007|1999|Rejei\u00e7\u00e3o: Erro n\u00e3o identificado.||" + aviso;
                    }
                }
                ControleLogs.logar("Completando arquivo de cancelamento.");
                docCFeCanc.completar(chaveAcessoCancelar);
                final String chaveAcesso = docCFeCanc.getInfCFe().getId();
                final String numeroCFeCanc = docCFeCanc.getInfCFe().getIde().getnCFe();
                final String caminho = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + chaveAcesso + ".xml";
                ControleLogs.logar("Assinando arquivo de cancelamento.");
                final Document docAssinado = docCFeCanc.assinar(docCFeCanc.toString(), ControleDados.gerarHashMD5(codigoAtivacao));
                final String resultado = ControleDados.outputXML(docAssinado);
                ControleArquivos.escreverCaracteresArquivo(caminho, resultado.toCharArray());
                new ControleNumeracaoSAT().gravarNumeroCFe(numeroCFeCanc);
                this.apagarInformacaoCFeEmitido(docCFeCanc.getInfCFe().getChCanc());
                ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CHAVE_ULTIMO_CFE, chaveAcesso.toCharArray());
                String cpfCnpjValue = docCFeCanc.getInfCFe().getDest().getCNPJ();
                if (cpfCnpjValue == null) {
                    cpfCnpjValue = docCFeCanc.getInfCFe().getDest().getCPF();
                }
                if (cpfCnpjValue == null) {
                    cpfCnpjValue = "";
                }
                final String qrCode = docCFeCanc.getInfCFe().getIde().getQrCode();
                final String valorTotal = docCFeCanc.getInfCFe().getTotal().getvCFe();
                final String dataHora = ControleTempo.getTimeStamp();
                ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE, dataHora.toCharArray());
                ControleLogs.logar("CANCELADO COM SUCESSO");
                final String res = StringUtil.base64Encode(resultado.getBytes("utf-8"));
                return "07000||Cancelado com sucesso + conte\u00fado notas||" + aviso + "|" + res + "|" + dataHora + "|" + chaveAcesso + "|" + valorTotal + "|" + cpfCnpjValue + "|" + qrCode;
            }
        }
        catch (SATBloqueadoException e3) {
            ControleLogs.logar("ERRO NO CANCELAMENTO: SAT est\u00e1 bloqueado");
            if (hoje.after(novaRedacao)) {
                if (Parametrizacoes.autorBloqueio == 1) {
                    return "07006||SAT bloqueado pelo contribuinte||" + aviso;
                }
                return "07007||SAT bloqueado pela SEFAZ||" + aviso;
            }
            else {
                if (Parametrizacoes.autorBloqueio == 1) {
                    return "07003||SAT bloqueado pelo contribuinte||" + aviso;
                }
                return "07004||SAT bloqueado pela SEFAZ||" + aviso;
            }
        }
        catch (SATAssociadoException e4) {
            ControleLogs.logar("ERRO NO CANCELAMENTO: SAT nao est\u00e1 associado ao AC");
            if (hoje.after(novaRedacao)) {
                return "07004||Vincula\u00e7\u00e3o do AC n\u00e3o confere.||" + aviso;
            }
        }
        catch (SATNaoAtivoException e5) {
            ControleLogs.logar("ERRO NO CANCELAMENTO: SAT nao est\u00e1 ativado");
        }
        catch (NoSuchAlgorithmException e6) {
            ControleLogs.logar("ERRO NO CANCELAMENTO:Par de chaves corrompido.");
        }
        catch (DataLengthException e7) {
            ControleLogs.logar("ERRO NO CANCELAMENTO");
            return "07001||C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (IllegalStateException e8) {
            ControleLogs.logar("ERRO NO CANCELAMENTO");
            return "07001||C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (InvalidCipherTextException e9) {
            ControleLogs.logar("ERRO NO CANCELAMENTO");
            return "07001||C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + aviso;
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return "07099||Erro desconhecido no cancelamento.||" + aviso;
    }
    
    public static String[] recuperarCFeParaCancelamento(final String chave) {
        try {
            apagarCFeEmitidosAntigos();
            if (!ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO)) {
                return null;
            }
            final String[] baseEmitidos = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO).split("\\\n");
            for (int i = 0; i < baseEmitidos.length; ++i) {
                final String linha = baseEmitidos[i];
                if (linha.indexOf(chave) >= 0) {
                    return linha.split("\\|");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void apagarInformacaoCFeEmitido(final String chave) {
        try {
            String listaFinal = "";
            final String[] baseEmitidos = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO).split("\\\n");
            for (int i = 0; i < baseEmitidos.length; ++i) {
                final String linha = baseEmitidos[i];
                if (linha.length() > 61) {
                    final String chaveCFe = linha.split("\\|")[0];
                    if (chave.indexOf(chaveCFe) == -1) {
                        listaFinal = String.valueOf(listaFinal) + linha + "\n";
                    }
                }
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO, listaFinal.toCharArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void apagarCFeEmitidosAntigos() {
        try {
            String listaFinal = "";
            final String[] baseEmitidos = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO).split("\\\n");
            for (int i = 0; i < baseEmitidos.length; ++i) {
                final String linha = baseEmitidos[i];
                if (linha.length() > 61) {
                    final String datahora = linha.split("\\|")[2];
                    if (calculaMinutos(datahora) < 1800L) {
                        listaFinal = String.valueOf(listaFinal) + linha + "\n";
                    }
                }
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO, listaFinal.toCharArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static long calculaMinutos(final String dataHora) {
        try {
            final long l = ControleTempo.parseTimeStamp(dataHora);
            final long atual = System.currentTimeMillis();
            final long dif = atual - l;
            return dif / 1000L;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao, final String chaveAcesso, final String dadosCancelamento) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "07099|Erro desconhecido";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "07001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
            if (chaveAcesso.length() != 47) {
                ControleLogs.logar("ERRO: Chave de Acesso invalida");
                return "07099|Erro desconhecido";
            }
            if (dadosCancelamento.length() <= 0) {
                ControleLogs.logar("ERRO: Dados Cancelamento Invalido");
                return "07099|Erro desconhecido";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "07099|Erro desconhecido";
        }
        return r;
    }
}
