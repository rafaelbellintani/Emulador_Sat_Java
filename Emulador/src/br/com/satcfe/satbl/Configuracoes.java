// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl;

import java.util.List;
import java.util.ArrayList;
import java.io.OutputStream;
import br.com.um.controles.ControleDados;
import br.com.um.controles.XmlUtil;
import br.com.satcfe.satbl.controles.ControleArquivos;

public class Configuracoes
{
    public static String setSistemaOperacional() {
        if (SistemaArquivos.DIRETORIO_RAIZ != null) {
            return SistemaArquivos.DIRETORIO_RAIZ;
        }
        //DETECTAR SISTEMA OPERACIONAL
        final String os = System.getProperty("os.name").toUpperCase();
        //DETECTAR VERSÃO DO SISTEMA OPERACIONAL
        final String osVersion = System.getProperty("os.version").toUpperCase();
        //PRINTA O SISTEMA OPERACIONAL ATUAL
        System.out.println("Sistema Operacional: " + os + " | Versão: " + osVersion);
        try {
            if (os.indexOf("WIN") >= 0) {
                Gerais.SO_SAT = Gerais.Sistema.WINDOWS;
                SistemaArquivos.DIRETORIO_RAIZ = SistemaArquivos.DIRETORIO_WINDOWS;
            }
            else if (os.indexOf("NIX") >= 0 || os.indexOf("NUX") >= 0) {
                Gerais.SO_SAT = Gerais.Sistema.LINUX;
                SistemaArquivos.DIRETORIO_RAIZ = SistemaArquivos.DIRETORIO_LINUX;
            }
        }
        catch (Exception e) {
            System.out.println("Erro ao configurar o Sistema Operacional");
        }
        return SistemaArquivos.DIRETORIO_RAIZ;
    }
    
    public boolean carregarConfiguracoesIniciais() {
        try {
        	//VERIFICA SE O ARQUIVO configuracoes.xml foi encontrado na PASTA RAIZ do Sistema Operacional
            if (!ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOE)) {
                return false;
            }
            final String xml = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOE);
            SAT.numeroDeSerie = XmlUtil.getConteudoTAG(xml, "nserieSAT");
            SAT.numeroDeSeguranca = ControleDados.getConteudoTAG(xml, "nSeg");
            SAT.protocolo = ControleDados.getConteudoTAG(xml, "protocoloWebService");
            final String xmlSign = ControleDados.getConteudoTAG(xml, "validarXMLSignature");
            final String habilitaValidacoes = ControleDados.getConteudoTAG(xml, "habilitaValidacoes");
            final String debug = ControleDados.getConteudoTAG(xml, "debug");
            final String fixarCadeiaCert = ControleDados.getConteudoTAG(xml, "fixarCadeiaCert");
            final String versaoSchema = ControleDados.getConteudoTAG(xml, "versaoSchema");
            final String versaoLayoutCFe = ControleDados.getConteudoTAG(xml, "versaoLayoutCFe");
            final String validarPeriodoCert = ControleDados.getConteudoTAG(xml, "validarPeriodoCert");
            final String validarCadeiaCert = ControleDados.getConteudoTAG(xml, "validarCadeiaCert");
            final String informarCadeia = ControleDados.getConteudoTAG(xml, "informarCadeia");
            
            if (versaoSchema != null && versaoSchema.length() >= 4) {
                SAT.VERSAO_SCHEMA_ATUAL = versaoSchema;
            }
            else {
                SAT.VERSAO_SCHEMA_ATUAL = SAT.VERSOES_SCHEMAS_VALIDAS[SAT.VERSOES_SCHEMAS_VALIDAS.length - 1];
            }
            if (versaoLayoutCFe != null && versaoLayoutCFe.length() >= 4) {
                SAT.VERSAO_LAYOUT_CFE = versaoLayoutCFe;
            }
            else {
                SAT.VERSAO_LAYOUT_CFE = SAT.VERSOES_LAYOUT_CFE_VALIDOS[SAT.VERSOES_LAYOUT_CFE_VALIDOS.length - 1];
            }
            if (debug != null && (debug.equals("0") || debug.equalsIgnoreCase("false"))) {
                SAT.debug = false;
            }
            else {
                SAT.debug = true;
            }
            if (xmlSign != null && (xmlSign.equalsIgnoreCase("0") || xmlSign.equalsIgnoreCase("false"))) {
                SAT.validarRetornoXML = false;
            }
            else {
                SAT.validarRetornoXML = true;
            }
            if (habilitaValidacoes != null && (habilitaValidacoes.equalsIgnoreCase("0") || habilitaValidacoes.equalsIgnoreCase("false"))) {
                SAT.validarCamposCFe = false;
            }
            else {
                SAT.validarCamposCFe = true;
            }
            if (fixarCadeiaCert != null && (fixarCadeiaCert.equalsIgnoreCase("0") || fixarCadeiaCert.equalsIgnoreCase("false"))) {
            	SAT.fixarCadeiaCert = false;
            }
            else {
                SAT.fixarCadeiaCert = true;
            }
            if (validarCadeiaCert != null && (validarCadeiaCert.equalsIgnoreCase("0") || validarCadeiaCert.equalsIgnoreCase("false"))) {
                SAT.validarCadeiaCertificacao = false;
            }
            else {
                SAT.validarCadeiaCertificacao = true;
            }
            if (validarPeriodoCert != null && (validarPeriodoCert.equalsIgnoreCase("0") || validarPeriodoCert.equalsIgnoreCase("false"))) {
                SAT.validarPeriodoCertificados = false;
            }
            else {
                SAT.validarPeriodoCertificados = true;
            }
            if (informarCadeia != null && (informarCadeia.equalsIgnoreCase("0") || informarCadeia.equalsIgnoreCase("false"))) {
                SAT.informarCadeia = false;
            }
            else {
                SAT.informarCadeia = true;
            }
            if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CSR)) {
                SAT.aguardandoCertificado = true;
                if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CERT)) {
                    SAT.aguardandoCertificado = false;
                }
            }
           if (Double.parseDouble(SAT.VERSAO_LAYOUT_CFE) <= 0.06) {
                SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS = this.getClass().getResource("/res/erros/tabela_erros_alertas_v0.06.xml").getPath();
            }
            else if (Double.parseDouble(SAT.VERSAO_LAYOUT_CFE) == 0.07) {
                SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS = this.getClass().getResource("/res/erros/tabela_erros_alertas_v0.07.xml").getPath();
            }
            else if (Double.parseDouble(SAT.VERSAO_LAYOUT_CFE) >= 0.08) {
                SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS = this.getClass().getResource("/res/erros/tabela_erros_alertas_v0.08.xml").getPath();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
           return SAT.emuladorOffLine = false;
        }
        SAT.CNPJEstabelecimento = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CNPJ);
        if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CNPJ_VALUE)) {
            final String s = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_CNPJ_VALUE);
            SAT.cnpjSoftwareHouse = s.substring(0, 14);
        }
        else {
            SAT.cnpjSoftwareHouse = "00000000000000";
        }
        if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA)) {
            SAT.signAC = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA);
            SAT.associado = true;
        }
        else {
            SAT.signAC = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        }
        return true;
    };
    
    public static void carregarSATOffLine() {
        try {
        	String osName = System.getProperty("os.name");
        	if(osName.equals("Windows")) {
            if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE)) {
                final String s = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
                final String[] r = ControleDados.quebrarString(s, ",");
                if (r[0].equals("VmxaT1VWSlhNVEZpUjBacllqTktVRkpyV2sxaFZ6VnM=")) {
                    SAT.emuladorOffLine = true;
                }
            }
        	}else if(osName.equals("Linux")) {
        		 if (ControleArquivos.existeArquivo(SistemaArquivos.CAMINHO_ARQUIVO_DAT)) {
                     final String s = ControleArquivos.lerCaracteresArquivo(SistemaArquivos.CAMINHO_ARQUIVO_DAT);
                     final String[] r = ControleDados.quebrarString(s, ",");
                     if (r[0].equals("VmxaT1VWSlhNVEZpUjBacllqTktVRkpyV2sxaFZ6VnM=")) {
                         SAT.emuladorOffLine = true;
                     }
                 }
        	}
        }
        catch (Exception e) {
            e.printStackTrace();
            SAT.emuladorOffLine = false;
        }
    }
    
    public static class Gerais
    {
        public static OutputStream saidaPadrao;
        public static Sistema SO_SAT;
        public static final String DELIMITADOR_DE_LINHA = "\n";
        public static final String ZONA = "GMT-03:00";
        public static final String DELIMITADOR_PADRAO = "|";
        
        static {
        	if(System.getProperty("os.name").equals("Windows")){
            Gerais.SO_SAT = Sistema.WINDOWS;
        }else if(System.getProperty("os.name").equals("Linux")) {
        	Gerais.SO_SAT = Sistema.LINUX;
        }
            Gerais.saidaPadrao = System.out;
        }
        
        public enum Sistema
        {
            WINDOWS("WINDOWS", 0), 
            LINUX("LINUX", 1);
            
            private Sistema(final String name, final int ordinal) {
            }
        }
    }
    
    public static class SAT
    {
        public static String falhaCertificado;
        public static final String VERSAO_PILOTO = "0.1";
        public static String numeroDeSerie;
        public static String numeroDeSeguranca;
        public static String VERSAO_LAYOUT_CFE;
        public static final String[] VERSOES_LAYOUT_CFE_VALIDOS;
        public static final String VERSAO_SB = "000003";
        public static String VERSAO_SCHEMA_ATUAL;
        public static final String[] VERSOES_SCHEMAS_VALIDAS;
        public static final long TAMANHO_MAXIMO_LOGS = 410000L;
        public static String protocolo;
        public static String CNPJEstabelecimento;
        public static String dataUltimaAtualizacao;
        public static String senhaAtivacao;
        public static boolean ativado;
        public static boolean associado;
        public static boolean corrompido;
        public static boolean aguardandoCertificado;
        public static boolean bloqueado;
        public static boolean autoBloqueado;
        public static boolean desligamentoIniciado;
        public static boolean emuladorOffLine;
        public static boolean validarCamposCFe;
        public static boolean validarRetornoXML;
        public static boolean validarSchema;
        public static boolean debug;
        public static boolean fixarCadeiaCert;
        public static boolean validarCadeiaCertificacao;
        public static boolean validarPeriodoCertificados;
        public static boolean informarCadeia;
        public static String signAC;
        public static String cnpjSoftwareHouse;
        
        static {
            SAT.falhaCertificado = "Erro desconhecido no Certificado";
            SAT.VERSAO_LAYOUT_CFE = "0.03";
            VERSOES_LAYOUT_CFE_VALIDOS = new String[] { "0.03", "0.04", "0.05", "0.06", "0.07", "0.08" };
            SAT.VERSAO_SCHEMA_ATUAL = null;
            VERSOES_SCHEMAS_VALIDAS = new String[] { "0.03", "0.04", "0.05", "0.06", "0.07", "0.08" };
            SAT.protocolo = "http";
            SAT.CNPJEstabelecimento = "";
            SAT.dataUltimaAtualizacao = "";
            SAT.ativado = false;
            SAT.associado = false;
            SAT.corrompido = false;
            SAT.aguardandoCertificado = false;
            SAT.bloqueado = false;
            SAT.autoBloqueado = false;
            SAT.desligamentoIniciado = false;
            SAT.emuladorOffLine = false;
            SAT.validarCamposCFe = true;
            SAT.validarRetornoXML = true;
            SAT.validarSchema = true;
            SAT.debug = true;
            SAT.fixarCadeiaCert = true;
            SAT.validarCadeiaCertificacao = true;
            SAT.validarPeriodoCertificados = true;
            SAT.informarCadeia = true;
            SAT.signAC = null;
            SAT.cnpjSoftwareHouse = null;
        }
    }
    
    public static class SistemaArquivos
    {
        public static String DIRETORIO_WINDOWS;
        public static String DIRETORIO_LINUX;
        public static String DIRETORIO_RAIZ;
        public static String DIRETORIO_KEYSTORE;
        public static String DIRETORIO_ARQUIVO_CSR;
        public static String DIRETORIO_ARQUIVO_CERT;
        public static String DIRETORIO_STATUS;
        public static String DIRETORIO_ARQUIVO_VERSAO;
        public static String DIRETORIO_ARQUIVO_CNPJ;
        public static String DIRETORIO_ARQUIVO_PARAMETRIZACAO;
        public static String DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL;
        public static String DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE;
        public static String DIRETORIO_COMANDOS;
        public static String DIRETORIO_DEBUG;
        public static String DIRETORIO_CERTIFICADOS;
        public static String DIRETORIO_CERT_HTTPS;
        public static String DIRETORIO_CERT_SEFAZ;
        public static String DIRETORIO_ARQUIVO_ID_LOTE;
        public static String DIRETORIO_ARQUIVO_ID_LOTE_TESTE;
        public static String DIRETORIO_ARQUIVOS_CFe;
        public static String DIRETORIO_ARQUIVOS_CFE_ENVIADOS;
        public static String DIRETORIO_ARQUIVOS_CFE_CANCELADOS;
        public static String DIRETORIO_ARQUIVOS_CFE_PROCESSADOS;
        public static String DIRETORIO_ARQUIVOS_CFE_REMOVIDOS;
        public static String DIRETORIO_ARQUIVOS_CFE_COM_ERRO;
        public static String DIRETORIO_ARQUIVO_NUMERO_SESSAO;
        public static String DIRETORIO_ARQUIVO_INFORMACOES;
        public static String DIRETORIO_ARQUIVO_ASSINATURA;
        public static String DIRETORIO_ARQUIVO_AVISO;
        public static String DIRETORIO_ARQUIVO_UF;
        public static String DIRETORIO_CSR;
        public static String DIRETORIO_ARQUIVO_ATUALIZACAO;
        public static String DIRETORIO_ARQUIVO_DAT;
        public static String CAMINHO_ARQUIVO_CONFIGURACOE;
        public static String CAMINHO_ARQUIVO_CSR;
        public static String CAMINHO_ARQUIVO_PARAMETROS_CSR;
        public static String CAMINHO_ARQUIVO_CERT;
        public static String CAMINHO_ARQUIVO_VERSAO;
        public static String CAMINHO_ARQUIVO_CNPJ;
        public static String CAMINHO_ARQUIVO_TIPO_CERT;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_WINDOWS;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_UF;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_USO;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO;
        public static String CAMINHO_ARQUIVO_TABELA_ANP;
        public static String CAMINHO_ARQUIVO_TABELA_VIGENCIA_LEIAUTE;
        public static String CAMINHO_ARQUIVO_CERT_SEFAZ;
        public static String CAMINHO_ARQUIVO_CERT_HTTPS;
        public static String CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL;
        public static String CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE;
        public static String CAMINHO_ARQUIVO_KEYSTORE_PUBLICA;
        public static String CAMINHO_ARQUIVO_TRUSTSTORE;
        public static String CAMINHO_ARQUIVO_KEYSTORE;
        public static String CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_LINUX;
        public static String CAMINHO_ARQUIVO_CODIGO_ATIVACAO;
        public static String CAMINHO_ARQUIVO_ID_LOTE;
        public static String CAMINHO_ARQUIVO_ID_LOTE_TESTE;
        public static String CAMINHO_ARQUIVO_NUMERO_SESSAO;
        public static String CAMINHO_ARQUIVO_INF_CANCELAMENTO;
        public static String CAMINHO_ARQUIVO_CHAVE_ULTIMO_CFE;
        public static String CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE;
        public static String CAMINHO_ARQUIVO_ASSINATURA;
        public static String CAMINHO_ARQUIVO_CNPJ_VALUE;
        public static String CAMINHO_ARQUIVO_AVISO;
        public static String CAMINHO_ARQUIVO_DAT;
        public static String CAMINHO_CERTIFICADOS;
        public static String CAMINHO_ARQUIVO_CONFIGURACOES_OFFLINE;
        public static String CAMINHO_ARQUIVO_UF;
        public static String CAMINHO_CSR;
        public static String CAMINHO_ARQUIVO_ATUALIZACOES;
        public static String CAMINHO_ARQUIVO_IDCMD;
        public static String CAMINHO_ARQUIVO_SAT_OFFLINE;
        public static String CAMINHO_ARQUIVO_LOGS;
        public static String CAMINHO_ARQUIVO_ERROS_ALERTAS;
        public static String CAMINHO_ARQUIVO_SCHEMA;
        public static String CAMINHO_ULTIMA_TRANSMISSAO;
        public static String CAMINHO_ULTIMA_COMUNICACAO;
        public static String CAMINHO_ARQUIVO_ERROS;
        public static String CAMINHO_ARQUIVO_HASH;
        
        static {
            SistemaArquivos.DIRETORIO_WINDOWS = "C:/SAT/";
            SistemaArquivos.DIRETORIO_LINUX = "/SAT/";
            SistemaArquivos.DIRETORIO_RAIZ = null;
            SistemaArquivos.DIRETORIO_KEYSTORE = String.valueOf(getDiretorioRaiz()) + "KeyStore/";
            SistemaArquivos.DIRETORIO_ARQUIVO_CSR = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "CSR/";
            SistemaArquivos.DIRETORIO_ARQUIVO_CERT = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "Cert/";
            SistemaArquivos.DIRETORIO_STATUS = String.valueOf(getDiretorioRaiz()) + "Status/";
            SistemaArquivos.DIRETORIO_ARQUIVO_VERSAO = String.valueOf(getDiretorioRaiz()) + "Versao/";
            SistemaArquivos.DIRETORIO_ARQUIVO_CNPJ = String.valueOf(getDiretorioRaiz()) + "Cnpj/";
            SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO = String.valueOf(getDiretorioRaiz()) + "Parametrizacao/";
            SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL = String.valueOf(getDiretorioRaiz()) + "DocFiscal/";
            SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE = String.valueOf(getDiretorioRaiz()) + "DocFiscalTeste/";
            SistemaArquivos.DIRETORIO_COMANDOS = String.valueOf(getDiretorioRaiz()) + "Comandos/";
            SistemaArquivos.DIRETORIO_DEBUG = String.valueOf(getDiretorioRaiz()) + "Debug/";
            SistemaArquivos.DIRETORIO_CERT_HTTPS = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "CertHttps/";
            SistemaArquivos.DIRETORIO_CERT_SEFAZ = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "CertSefaz/";
            SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE = String.valueOf(getDiretorioRaiz()) + "IdLote/";
            SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE_TESTE = String.valueOf(getDiretorioRaiz()) + "IdLoteTeste/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFe = String.valueOf(getDiretorioRaiz()) + "CFes/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS = String.valueOf(getDiretorioRaiz()) + "CFesEnviados/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS = String.valueOf(getDiretorioRaiz()) + "CFesCancelados/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_PROCESSADOS = String.valueOf(getDiretorioRaiz()) + "CFesProcessados/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_REMOVIDOS = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_PROCESSADOS) + "CFesRemovidos/";
            SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_COM_ERRO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_PROCESSADOS) + "CFesComErro/";
            SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO = String.valueOf(getDiretorioRaiz()) + "NumSessao/";
            SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES = String.valueOf(getDiretorioRaiz()) + "Informacoes/";
            SistemaArquivos.DIRETORIO_ARQUIVO_ASSINATURA = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "SignAC/";
            SistemaArquivos.DIRETORIO_ARQUIVO_AVISO = String.valueOf(getDiretorioRaiz()) + "Aviso/";
            SistemaArquivos.DIRETORIO_ARQUIVO_UF = String.valueOf(getDiretorioRaiz()) + "UF/";
            SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO = String.valueOf(getDiretorioRaiz()) + "Atualizacao/";
            SistemaArquivos.DIRETORIO_ARQUIVO_DAT = String.valueOf(getDiretorioRaiz()) + "Dat/";
            SistemaArquivos.DIRETORIO_CERTIFICADOS = String.valueOf(getDiretorioRaiz()) + "Certificados/";
            //INDICA O CAMINHO DO ARQUIVO DE CONFIGURACOES
            SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOE = String.valueOf(getDiretorioRaiz()) + "configuracoes.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_CSR = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_CSR) + "CSR.csr";
            SistemaArquivos.CAMINHO_CSR = String.valueOf(SistemaArquivos.DIRETORIO_CSR) + "CSR.csr";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETROS_CSR = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_CSR) + "parametrosCSR.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_CERT = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_CERT) + "Cert.crt";
            SistemaArquivos.CAMINHO_CERTIFICADOS = String.valueOf(SistemaArquivos.DIRETORIO_CERTIFICADOS) + "Cert.crt";
            SistemaArquivos.CAMINHO_ARQUIVO_VERSAO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_VERSAO) + "Versao.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_CNPJ = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_CNPJ) + "Cnpj.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_TIPO_CERT = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_CERT) + "tipoCert.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_WINDOWS= String.valueOf(getDiretorioRaiz()) + "ParametrizacaoDeFabrica.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_FABRICA_LINUX = String.valueOf(getDiretorioRaiz()) + "Parametrizacao/"+ "ParametrizacaoDeFabrica.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_UF = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "ParametrizacaoDeUF.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "ParametrizacaoDeAtivacao.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "ParametrizacaoDeUso.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "ParametrizacaoDeBloqueio.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_TABELA_ANP = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "TabelaANP.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_TABELA_VIGENCIA_LEIAUTE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "TabelaVigenciaLeiaute.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_DAT = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_DAT) + "SAT_OFFLINE.dat";
            SistemaArquivos.CAMINHO_ARQUIVO_CERT_SEFAZ = String.valueOf(SistemaArquivos.DIRETORIO_CERT_SEFAZ) + "baseCertSEFAZ.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_CERT_HTTPS = String.valueOf(SistemaArquivos.DIRETORIO_CERT_HTTPS) + "baseCertHTTPS.xml";
            SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL) + "numfiscal.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE) + "NumFiscalTeste.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "sat.jks";
            SistemaArquivos.CAMINHO_ARQUIVO_TRUSTSTORE = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "trustStoreHttps.jks";
            SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "keyStore.p12";
            SistemaArquivos.CAMINHO_ARQUIVO_CODIGO_ATIVACAO = String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "sat.dat";
            SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE) + "IdLote.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE_TESTE) + "IdLoteTeste.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_SESSAO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + "NumSessao.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_INF_CANCELAMENTO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES) + "InfCancelamento.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_CHAVE_ULTIMO_CFE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES) + "ultimoCFe.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES) + "timeStampUltimoCFe.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ASSINATURA) + "SignAC.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_CNPJ_VALUE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ASSINATURA) + "cnpjValue.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_AVISO = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_AVISO) + "aviso.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOES_OFFLINE = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO) + "OffLine.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_UF = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_UF) + "cUF.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_ATUALIZACOES = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "RESULTADO_ATUALIZACAO.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_IDCMD = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "idCmd.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE = "SAT_OFFLINE.dat";
            SistemaArquivos.CAMINHO_ARQUIVO_LOGS = String.valueOf(SistemaArquivos.DIRETORIO_DEBUG) + "logsSAT-CFe.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_LOGS = String.valueOf(SistemaArquivos.DIRETORIO_DEBUG) + "logsSAT-CFe.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS = null;
            SistemaArquivos.CAMINHO_ARQUIVO_SCHEMA = null;
            SistemaArquivos.CAMINHO_ULTIMA_TRANSMISSAO = String.valueOf(SistemaArquivos.DIRETORIO_STATUS) + "ultimaTransmissao.txt";
            SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO = String.valueOf(SistemaArquivos.DIRETORIO_STATUS) + "ultimaComunicacao.txt";
            SistemaArquivos.CAMINHO_ARQUIVO_ERROS = String.valueOf(SistemaArquivos.DIRETORIO_DEBUG) + "printErros.log";
            SistemaArquivos.CAMINHO_ARQUIVO_HASH = String.valueOf(SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES) + "hash.txt";
        }
        
        private static String getDiretorioRaiz() {
            return Configuracoes.setSistemaOperacional();
        }
        
        public static void criarDiretoriosSATBL() {
            final List<String> lista = new ArrayList<String>();
            lista.add(SistemaArquivos.DIRETORIO_RAIZ);
            lista.add(SistemaArquivos.DIRETORIO_DEBUG);
            lista.add(SistemaArquivos.DIRETORIO_KEYSTORE);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_CNPJ);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE);
            lista.add(SistemaArquivos.DIRETORIO_COMANDOS);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_CSR);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_CERT);
            lista.add(SistemaArquivos.DIRETORIO_CERT_HTTPS);
            lista.add(SistemaArquivos.DIRETORIO_CERT_SEFAZ);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_ID_LOTE_TESTE);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_PROCESSADOS);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_REMOVIDOS);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_COM_ERRO);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_INFORMACOES);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_ASSINATURA);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_DAT);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_AVISO);
            lista.add(SistemaArquivos.DIRETORIO_ARQUIVO_UF);
            lista.add(SistemaArquivos.DIRETORIO_STATUS);
            lista.add(SistemaArquivos.DIRETORIO_CERTIFICADOS);
            for (int i = 0; i < lista.size(); ++i) {
                ControleArquivos.criarDiretorio(lista.get(i));
            }
            try {
            	//Procurar certificados se for Windows
            	if(System.getProperty("os.name").equals("Windows")) {
                if (ControleArquivos.existeArquivo("raiz.cer")) {
                    final String s = ControleArquivos.lerArquivo("raiz.cer");
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "raiz.cer", s.toCharArray());
                }
                if (ControleArquivos.existeArquivo("intermediario.cer")) {
                    final String ss = ControleArquivos.lerArquivo("intermediario.cer");
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "intermediario.cer", ss.toCharArray());
                	}
            	}
            	//Procurar certificados se for Linux
            	else if(System.getProperty("os.name").equals("Linux")) {
            	if (ControleArquivos.existeArquivo(SistemaArquivos.DIRETORIO_CERTIFICADOS + "raiz.cer")) {
                    final String s = ControleArquivos.lerArquivo(SistemaArquivos.DIRETORIO_CERTIFICADOS + "raiz.cer");
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "raiz.cer", s.toCharArray());
                }
                if (ControleArquivos.existeArquivo(SistemaArquivos.DIRETORIO_CERTIFICADOS + "intermediario.cer")) {
                    final String ss = ControleArquivos.lerArquivo(SistemaArquivos.DIRETORIO_CERTIFICADOS + "intermediario.cer");
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(SistemaArquivos.DIRETORIO_KEYSTORE) + "intermediario.cer", ss.toCharArray());
                }
            }
        }
            catch (Exception e) {
                System.out.println("Erro ao copiar cadeia do certificado.");
            }
        }
    }
}
