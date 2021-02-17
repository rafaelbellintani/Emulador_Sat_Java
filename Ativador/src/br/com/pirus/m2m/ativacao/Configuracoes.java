// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao;

public class Configuracoes
{
    public static final String VERSAO_ATIVACAO = "2.2.5";
    public static final String ATIVACAO_OK = "4000";
    public static final String ATIVACAO_ERRO_CERTIFICADO = "4001";
    public static final String ATIVACAO_SEFAZ_NAO_RECONHECE = "4002";
    public static final String ATIVACAO_JA_ATIVADO = "4003";
    public static final String ATIVACAO_ERRO_CNPJ_INVALIDO = "4004";
    public static final String ATIVACAO_ERRO_CONEXAO_SEFAZ = "4005";
    public static final String ATIVACAO_ERRO_DESCONHECIDO = "4999";
    public static final String EMISSAOTESTE_ERRO_DESCONHECIDO = "6999";
    public static final String EMISSAOTESTE_CHAVE_INCORRETA = "6001";
    public static final String EMISSAOTESTE_OK = "6000";
    public static final String CAMINHO_ICONE_NFP = "/images/nfpaulista.PNG";
    public static final int TIMEOUT_PING = 5000;
    public static final String MENSAGEM_TIMEOUT = "Timeout.";
    
    public static class SAT
    {
        public static String codigoDeAtivacao;
        public static String IE;
        public static String CNPJ;
        public static String signAC;
        public static String cnpjSoftwareHouse;
        
        static {
            SAT.codigoDeAtivacao = null;
            SAT.IE = null;
            SAT.CNPJ = null;
            SAT.signAC = null;
            SAT.cnpjSoftwareHouse = null;
        }
    }
}
