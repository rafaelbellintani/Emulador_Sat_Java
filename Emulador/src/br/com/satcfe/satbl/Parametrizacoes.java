// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl;

import br.com.satcfe.satbl.modelos.cfe.ObsFisco;
import java.util.List;
import br.com.satcfe.satbl.modelos.parametrizacao.TabelaCodigosAnp;
import br.com.satcfe.satbl.modelos.parametrizacao.TabelaVigenciaLeiaute;

public class Parametrizacoes
{
    public static String enderecoNtp;
    public static int portaNTP;
    public static int tempoTransmissao;
    public static int tempoVerificacao;
    public static String horarioVeraoInicio;
    public static String horarioVeraoFim;
    public static String fusoHorario;
    public static String nomeCertificadoRaiz;
    @Deprecated
    public static Object certificadoValidacaoSefaz;
    public static String[] endereco;
    public static String porta;
    @Deprecated
    public static String certificadoHTTPS;
    public static String razaoSocialEmitente;
    public static String nomeFantasiaEmitente;
    public static String xLgr;
    public static String nro;
    public static String xCpl;
    public static String xBairro;
    public static String xMun;
    public static String CEP;
    public static String codigoRegimeTributario;
    public static String cUF;
    public static String CNPJ;
    public static String IE;
    public static String tipoTransmissao;
    public static long valorTransmissao;
    public static String tipoVerificacao;
    public static long valorVerificacao;
    public static String tipoComandos;
    public static long valorComandos;
    public static String ambiente;
    public static String mensagemAmbiente;
    public static String CFeParametrizacao;
    public static String CFeRecepcao;
    public static String CFeRetRecepcao;
    public static String CFeCancelamento;
    public static String CFeStatus;
    public static String CFeComandos;
    public static String CFeAtualizacao;
    public static String CFeTeste;
    public static String CFeSignAC;
    public static String CFeLogs;
    public static String CFeConsultaGestao;
    public static String CFeReset;
    public static String CFeAtivacao;
    public static String CFeServicoNacional;
    public static String CFeCertificacao;
    public static int verProcesso;
    public static String tipoBloqueio;
    public static TabelaVigenciaLeiaute tabelaVigenciaLeiaute;
    public static TabelaCodigosAnp tabelaCodigosAnp;
    public static long valorBloqueio;
    public static int nivelLog;
    public static List<ObsFisco> obsFisco;
    public static double limiteCFe;
    public static int autorBloqueio;
    public static boolean cessacao;
    public static final int FABRICA = 0;
    public static final int ATIVACAO = 1;
    public static final int UF = 2;
    public static final int USO = 3;
    public static final int BLOQUEIO = 4;
    public static final int GESTAO = 5;
    
    static {
        Parametrizacoes.enderecoNtp = null;
        Parametrizacoes.portaNTP = 0;
        Parametrizacoes.tempoTransmissao = 60000;
        Parametrizacoes.horarioVeraoInicio = null;
        Parametrizacoes.horarioVeraoFim = null;
        Parametrizacoes.fusoHorario = null;
        Parametrizacoes.nomeCertificadoRaiz = null;
        Parametrizacoes.certificadoValidacaoSefaz = null;
        Parametrizacoes.endereco = null;
        Parametrizacoes.porta = null;
        Parametrizacoes.certificadoHTTPS = null;
        Parametrizacoes.razaoSocialEmitente = null;
        Parametrizacoes.nomeFantasiaEmitente = null;
        Parametrizacoes.xLgr = null;
        Parametrizacoes.nro = null;
        Parametrizacoes.xCpl = null;
        Parametrizacoes.xBairro = null;
        Parametrizacoes.xMun = null;
        Parametrizacoes.CEP = null;
        Parametrizacoes.codigoRegimeTributario = null;
        Parametrizacoes.cUF = null;
        Parametrizacoes.CNPJ = null;
        Parametrizacoes.IE = null;
        Parametrizacoes.tipoTransmissao = null;
        Parametrizacoes.tipoVerificacao = null;
        Parametrizacoes.tipoComandos = null;
        Parametrizacoes.ambiente = "2";
        Parametrizacoes.mensagemAmbiente = null;
        Parametrizacoes.CFeParametrizacao = null;
        Parametrizacoes.CFeRecepcao = null;
        Parametrizacoes.CFeRetRecepcao = null;
        Parametrizacoes.CFeCancelamento = null;
        Parametrizacoes.CFeStatus = null;
        Parametrizacoes.CFeComandos = null;
        Parametrizacoes.CFeAtualizacao = null;
        Parametrizacoes.CFeTeste = null;
        Parametrizacoes.CFeSignAC = null;
        Parametrizacoes.CFeLogs = null;
        Parametrizacoes.CFeConsultaGestao = null;
        Parametrizacoes.CFeReset = null;
        Parametrizacoes.CFeAtivacao = null;
        Parametrizacoes.CFeServicoNacional = null;
        Parametrizacoes.CFeCertificacao = null;
        Parametrizacoes.tipoBloqueio = null;
        Parametrizacoes.tabelaVigenciaLeiaute = null;
        Parametrizacoes.tabelaCodigosAnp = null;
        Parametrizacoes.valorBloqueio = 0L;
        Parametrizacoes.obsFisco = null;
        Parametrizacoes.autorBloqueio = 0;
        Parametrizacoes.cessacao = false;
    }
    
    public static void limparParametrizacoes() {
        Parametrizacoes.enderecoNtp = null;
        Parametrizacoes.CFeParametrizacao = null;
        Parametrizacoes.CFeRecepcao = null;
        Parametrizacoes.CFeRetRecepcao = null;
        Parametrizacoes.CFeCancelamento = null;
        Parametrizacoes.CFeStatus = null;
        Parametrizacoes.CFeComandos = null;
        Parametrizacoes.CFeAtualizacao = null;
        Parametrizacoes.CFeTeste = null;
        Parametrizacoes.CFeSignAC = null;
        Parametrizacoes.CFeLogs = null;
        Parametrizacoes.CFeAtivacao = null;
        Parametrizacoes.CFeServicoNacional = null;
        Parametrizacoes.CFeCertificacao = null;
    }
}
