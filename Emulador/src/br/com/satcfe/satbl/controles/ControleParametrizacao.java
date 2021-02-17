// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.controles.webservices.WebServiceCFeConsultaGestao;
import br.com.satcfe.satbl.excecoes.SATNaoAtivoException;
import br.com.satcfe.satbl.excecoes.SATBloqueadoException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeParametrizacao;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeAtivacao;
import br.com.satcfe.satbl.excecoes.SATAtivoException;
import br.com.um.controles.StringUtil;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeServicoNacional;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrosGestao;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoBloqueio;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoUso;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoUf;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoAtivacao;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoFabrica;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.Parametrizacoes;

public class ControleParametrizacao
{
    public static boolean carregarParametrizacao(final int parametrizacao) {
        try {
            switch (parametrizacao) {
                case 0: {
                    Parametrizacoes.limparParametrizacoes();
                    if(System.getProperty("os.name").equals("Windows")) {
                    	final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_WINDOWS);
                    	final ParametrizacaoFabrica pFabrica = new ParametrizacaoFabrica(xml);
                        return pFabrica.parse();
                    }else if(System.getProperty("os.name").equals("Linux")) {
                    	final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_LINUX);
                    	final ParametrizacaoFabrica pFabrica = new ParametrizacaoFabrica(xml);
                        return pFabrica.parse();
                    }
                }
                case 1: {
                    Parametrizacoes.limparParametrizacoes();
                    final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO);
                    final ParametrizacaoAtivacao pAtivacao = new ParametrizacaoAtivacao(xml);
                    return pAtivacao.parse();
                }
                case 2: {
                    Parametrizacoes.limparParametrizacoes();
                    final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_UF);
                    final ParametrizacaoUf pUf = new ParametrizacaoUf(xml);
                    return pUf.parse();
                }
                case 3: {
                    Parametrizacoes.limparParametrizacoes();
                    final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO);
                    final ParametrizacaoUso pUso = new ParametrizacaoUso(xml);
                    return pUso.parse();
                }
                case 4: {
                    Parametrizacoes.limparParametrizacoes();
                    final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO);
                    final ParametrizacaoBloqueio pBloqueio = new ParametrizacaoBloqueio(xml);
                    return pBloqueio.parse();
                }
                case 5: {
                    final String tabelaAnp = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_ANP);
                    final String tabelaVigencia = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_VIGENCIA_LEIAUTE);
                    new ParametrosGestao(tabelaAnp).parse();
                    new ParametrosGestao(tabelaVigencia).parse();
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void baixarParametrizacaoUF(final String UF, final long timeout) throws SATAtivoException, ErroComunicacaoRetaguardaException, ErroDesconhecidoException {
        Parametrizacoes.cUF = UF;
        final WebServiceCFeServicoNacional wsServicoNacional = new WebServiceCFeServicoNacional();
        wsServicoNacional.setcUF(UF);
        wsServicoNacional.setTimeout(timeout);
        if (!wsServicoNacional.consumirWebService()) {
            throw new ErroComunicacaoRetaguardaException((wsServicoNacional.getcStat() != null) ? wsServicoNacional.getcStat() : "");
        }
        String arquivoParam = wsServicoNacional.getParamUF();
        if (wsServicoNacional.getcStat() != null && wsServicoNacional.getcStat().equalsIgnoreCase("115")) {
            if (arquivoParam == null) {
                throw new ErroDesconhecidoException();
            }
            if (StringUtil.isBase64(arquivoParam)) {
                arquivoParam = StringUtil.base64DecodeUTF8(arquivoParam);
            }
            ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_UF, arquivoParam.toCharArray(), false);
        }
        else {
            if (wsServicoNacional.getcStat() != null && wsServicoNacional.getcStat().equals("205")) {
                throw new SATAtivoException();
            }
            if (wsServicoNacional.getcStat() != null) {
                final String msg = String.valueOf(wsServicoNacional.getcStat()) + "|" + wsServicoNacional.getxMotivo();
                throw new ErroDesconhecidoException(msg);
            }
            throw new ErroDesconhecidoException();
        }
    }
    
    public static void baixarParametrizacaoAtivacao(final String CNPJ, final String cUF, final long timeout) throws SATAtivoException, ErroComunicacaoRetaguardaException, ErroDesconhecidoException {
        final WebServiceCFeAtivacao wsAtivacao = new WebServiceCFeAtivacao();
        wsAtivacao.setCNPJ(CNPJ);
        wsAtivacao.setUF(cUF);
        wsAtivacao.setTimeout(timeout);
        String arquivoParam = null;
        if (!wsAtivacao.consumirWebService()) {
            throw new ErroComunicacaoRetaguardaException();
        }
        arquivoParam = wsAtivacao.getParamAtivacao();
        if (wsAtivacao.getcStat() == null || arquivoParam == null) {
            throw new ErroDesconhecidoException();
        }
        if (wsAtivacao.getcStat().equalsIgnoreCase("117") || wsAtivacao.getcStat().equalsIgnoreCase("115")) {
            if (StringUtil.isBase64(arquivoParam)) {
                arquivoParam = StringUtil.base64DecodeUTF8(arquivoParam);
            }
            ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO, arquivoParam.toCharArray(), false);
            return;
        }
        if (wsAtivacao.getcStat().equals("205")) {
            throw new SATAtivoException();
        }
        throw new ErroDesconhecidoException("Erro: " + wsAtivacao.getcStat());
    }
    
    public static void baixarParametrizacaoUso(final String cUF, final long timeout) throws ErroComunicacaoRetaguardaException, SATNaoAtivoException, ErroDesconhecidoException, SATBloqueadoException {
        final WebServiceCFeParametrizacao wsParametrizacao = new WebServiceCFeParametrizacao();
        wsParametrizacao.setTimeout(timeout);
        String arquivoParam = null;
        if (!wsParametrizacao.consumirWebService()) {
            throw new ErroComunicacaoRetaguardaException();
        }
        arquivoParam = wsParametrizacao.getParamUso();
        final String paramBloq = wsParametrizacao.getParamBloqueio();
        if (paramBloq != null && paramBloq.length() > 4) {
            throw new SATBloqueadoException();
        }
        if (wsParametrizacao.getcStat() == null || arquivoParam == null) {
            throw new ErroDesconhecidoException();
        }
        if (wsParametrizacao.getcStat().equals("117")) {
            if (StringUtil.isBase64(arquivoParam)) {
                arquivoParam = StringUtil.base64DecodeUTF8(arquivoParam);
            }
            ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, arquivoParam.toCharArray(), false);
            return;
        }
        if (wsParametrizacao.getcStat().equals("208")) {
            throw new SATNaoAtivoException();
        }
        throw new ErroDesconhecidoException();
    }
    
    public static void baixarParametrosDeGestao(final String cUF, final long timeout) throws ErroComunicacaoRetaguardaException, SATNaoAtivoException, ErroDesconhecidoException, SATBloqueadoException {
        final WebServiceCFeConsultaGestao wsGestao = new WebServiceCFeConsultaGestao();
        wsGestao.setTimeout(timeout);
        if (!wsGestao.consumirWebService()) {
            throw new ErroComunicacaoRetaguardaException();
        }
        String tabelaAnp = wsGestao.getTabelaANP();
        String tabelaVigencaLeiaute = wsGestao.getVigenciaLeiaute();
        String paramFabrica = wsGestao.getParamFabrica();
        if (wsGestao.getcStat() == null || tabelaAnp == null) {
            throw new ErroDesconhecidoException();
        }
        if (wsGestao.getcStat().equals("133")) {
            if (tabelaAnp != null && StringUtil.isBase64(tabelaAnp)) {
                tabelaAnp = StringUtil.base64DecodeUTF8(tabelaAnp);
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_ANP, tabelaAnp.toCharArray(), false);
                ControleLogs.logar("Par\u00e2metro de Gest\u00e3o Atualizado: Tabela ANP. ");
            }
            if (tabelaVigencaLeiaute != null && StringUtil.isBase64(tabelaVigencaLeiaute)) {
                tabelaVigencaLeiaute = StringUtil.base64DecodeUTF8(tabelaVigencaLeiaute);
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_VIGENCIA_LEIAUTE, tabelaVigencaLeiaute.toCharArray(), false);
                ControleLogs.logar("Par\u00e2metro de Gest\u00e3o Atualizado: Tabela de Vig\u00eancia dos Leiautes. ");
            }
            if (paramFabrica != null && StringUtil.isBase64(paramFabrica)) {
                paramFabrica = StringUtil.base64DecodeUTF8(paramFabrica);
                if(System.getProperty("os.name").equals("Windows")) {
                	ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_WINDOWS, paramFabrica.toCharArray(), false);
                }else if(System.getProperty("os.name").equals("Linux")){
                	ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_LINUX, paramFabrica.toCharArray(), false);
                }
                ControleLogs.logar("Par\u00e2metro de Gest\u00e3o Atualizado: Parametriza\u00e7\u00e3o de F\u00e1brica.");
            }
            return;
        }
        if (wsGestao.getcStat().equals("208")) {
            throw new SATNaoAtivoException();
        }
        throw new ErroDesconhecidoException();
    }
    
    public static void removerParametrizacaoBloqueio() {
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO)) {
            ControleLogs.logar("removendo parametriza\u00e7\u00e3o de Bloqueio");
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO);
        }
    }
    
    public static void removerParametrizacaoUso() {
        ControleLogs.logar("removendo parametriza\u00e7\u00e3o de Uso");
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO);
    }
    
    public static void exibirParametrizacoes() {
        System.out.println("endereco0 " + Parametrizacoes.endereco[0]);
        System.out.println("endereco1 " + Parametrizacoes.endereco[1]);
        System.out.println("endereco2 " + Parametrizacoes.endereco[2]);
        System.out.println(" " + Parametrizacoes.CFeServicoNacional);
        System.out.println(" " + Parametrizacoes.CFeStatus);
        System.out.println(" " + Parametrizacoes.CFeAtivacao);
        System.out.println(" " + Parametrizacoes.CFeCertificacao);
        System.out.println(" " + Parametrizacoes.CFeParametrizacao);
        System.out.println(" " + Parametrizacoes.CFeRecepcao);
        System.out.println(" " + Parametrizacoes.CFeRetRecepcao);
        System.out.println(" " + Parametrizacoes.CFeStatus);
        System.out.println(" " + Parametrizacoes.CFeComandos);
        System.out.println(" " + Parametrizacoes.CFeAtualizacao);
        System.out.println(" " + Parametrizacoes.CFeTeste);
        System.out.println(" " + Parametrizacoes.CFeSignAC);
        System.out.println(" " + Parametrizacoes.CFeLogs);
    }
}
