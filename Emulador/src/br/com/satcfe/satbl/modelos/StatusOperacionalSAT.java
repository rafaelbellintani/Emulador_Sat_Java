// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import java.util.Arrays;
import java.util.ArrayList;
import java.security.cert.Certificate;
import java.util.Calendar;
import javax.security.cert.X509Certificate;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Configuracoes;

public class StatusOperacionalSAT
{
    private StringBuffer statusSAT;
    private String tipoLan;
    private String lanIP;
    private String lanMAC;
    private String lanMASK;
    private String lanGW;
    private String lanDNS1;
    private String lanDNS2;
    private String statusLan;
    private String nivelBateria;
    private String memoriaTotal;
    private String memoriaUsada;
    private String dataAtual;
    private String versaoSoftwareBasico;
    private String versaoLayout;
    private String ultimoCFe;
    private String listaInicial;
    private String listaFinal;
    private String dhTransmissao;
    private String dhComunicacao;
    private String CERT_EMISSAO;
    private String CERT_VENCIMENTO;
    private String ESTADO_OPERACAO;
    
    public StatusOperacionalSAT() {
        this.statusSAT = null;
        this.tipoLan = "";
        this.lanIP = "";
        this.lanMAC = "";
        this.lanMASK = "";
        this.lanGW = "";
        this.lanDNS1 = "";
        this.lanDNS2 = "";
        this.statusLan = "";
        this.nivelBateria = "";
        this.memoriaTotal = "";
        this.memoriaUsada = "";
        this.dataAtual = "";
        this.versaoSoftwareBasico = "";
        this.versaoLayout = "";
        this.ultimoCFe = "";
        this.listaInicial = "";
        this.listaFinal = "";
        this.dhTransmissao = "";
        this.dhComunicacao = "";
        this.CERT_EMISSAO = "";
        this.CERT_VENCIMENTO = "";
        this.ESTADO_OPERACAO = "";
        try {
            this.tipoLan = "DHCP";
            this.nivelBateria = "ALTO";
            this.lanGW = "127.127.127.100";
            this.lanIP = "127.127.127.100";
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.06) {
                this.lanMASK = "22:d9:d0:45:17:02";
                this.lanMAC = this.lanIP;
            }
            else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.06) {
                this.lanMAC = "22:d9:d0:45:17:02";
                this.lanMASK = "255.255.000.000";
            }
            this.versaoLayout = Configuracoes.SAT.VERSAO_LAYOUT_CFE;
            final String v = String.valueOf("000003".substring(0, 2)) + "." + "000003".substring(2, 4) + "." + "000003".substring(4);
            this.versaoSoftwareBasico = v;
            this.lanDNS1 = "127.127.127.100";
            this.lanDNS2 = "127.127.127.100";
            this.statusLan = "CONECTADO";
            this.memoriaTotal = "1000.00";
            this.memoriaUsada = "10.00";
            this.dataAtual = ControleTempo.getTimeStamp();
            this.ultimoCFe = "00000000000000000000000000000000000000000000";
            this.listaFinal = "00000000000000000000000000000000000000000000";
            this.listaInicial = "00000000000000000000000000000000000000000000";
            this.dhTransmissao = ControleTempo.getTimeStamp();
            this.dhComunicacao = ControleTempo.getTimeStamp();
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_TRANSMISSAO)) {
                this.dhTransmissao = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_TRANSMISSAO);
            }
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO)) {
                this.dhComunicacao = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO);
            }
            this.CERT_EMISSAO = ControleTempo.getData(true);
            this.CERT_VENCIMENTO = ControleTempo.getData(true);
            this.ESTADO_OPERACAO = "0";
            if (!Configuracoes.SAT.bloqueado) {
                this.ESTADO_OPERACAO = "0";
            }
            else if (Configuracoes.SAT.bloqueado && Parametrizacoes.autorBloqueio == 2 && !Parametrizacoes.cessacao) {
                this.ESTADO_OPERACAO = "1";
            }
            else if (Configuracoes.SAT.bloqueado && Parametrizacoes.autorBloqueio == 1 && !Parametrizacoes.cessacao) {
                this.ESTADO_OPERACAO = "2";
            }
            else if (Configuracoes.SAT.autoBloqueado) {
                this.ESTADO_OPERACAO = "3";
            }
            else if (Configuracoes.SAT.bloqueado && Parametrizacoes.cessacao) {
                this.ESTADO_OPERACAO = "4";
            }
            this.obterDataCertificado();
            this.obterListaCFe();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String format(final int n) {
        if (n < 10) {
            return "0" + String.valueOf(n);
        }
        return String.valueOf(n);
    }
    
    private void obterDataCertificado() {
        try {
            final ControleKeyStore store = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            store.carregarKeyStore();
            final Certificate cert = store.getCertificado();
            final X509Certificate x = X509Certificate.getInstance(cert.getEncoded());
            final Calendar c1 = Calendar.getInstance();
            final Calendar c2 = Calendar.getInstance();
            c1.setTime(x.getNotBefore());
            c2.setTime(x.getNotAfter());
            String ano = String.valueOf(c1.get(1));
            String mes = format(c1.get(2) + 1);
            String dia = format(c1.get(5));
            this.CERT_EMISSAO = String.valueOf(ano) + mes + dia;
            ano = String.valueOf(c2.get(1));
            mes = format(c2.get(2) + 1);
            dia = format(c2.get(5));
            this.CERT_VENCIMENTO = String.valueOf(ano) + mes + dia;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void obterListaCFe() {
        try {
            final String s = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CHAVE_ULTIMO_CFE);
            if (s != null && s.length() >= 44) {
                this.ultimoCFe = s.replace("CFe", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final ArrayList<String> lista = new ArrayList<String>();
            String[] arr = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; ++i) {
                    lista.add(arr[i]);
                }
            }
            arr = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS);
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; ++i) {
                    lista.add(arr[i]);
                }
            }
            arr = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS);
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; ++i) {
                    final String[] arr2 = ControleArquivos.listarDiretorio(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + arr[i]);
                    if (arr2.length > 0) {
                        for (int j = 0; j < arr2.length; ++j) {
                            lista.add(arr2[j]);
                        }
                    }
                }
            }
            if (lista.size() > 0) {
                final Object[] listaGeral = lista.toArray();
                Arrays.sort(listaGeral);
                this.listaInicial = (String)listaGeral[0];
                this.listaFinal = (String)listaGeral[listaGeral.length - 1];
                this.listaInicial = this.listaInicial.replace("CFe", "").replace(".xml", "");
                this.listaFinal = this.listaFinal.replace("CFe", "").replace(".xml", "");
                final StringBuffer sb = new StringBuffer();
                for (int k = 0; k < listaGeral.length; ++k) {
                    sb.append(listaGeral[k]).append("\n");
                }
                ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_DEBUG) + "Lista.txt", sb.toString().toCharArray());
            }
        }
        catch (Exception e) {
            this.listaInicial = "00000000000000000000000000000000000000000000";
            this.listaFinal = "00000000000000000000000000000000000000000000";
            e.printStackTrace();
        }
    }
    
    public void setEstadoOperacao(final String flag) {
        this.ESTADO_OPERACAO = flag;
    }
    
    private void completar() {
        this.statusSAT.append("<tipoLan>").append(this.tipoLan).append("</tipoLan>");
        this.statusSAT.append("<lanIP>").append(this.lanIP).append("</lanIP>");
        this.statusSAT.append("<lanMAC>").append(this.lanMAC).append("</lanMAC>");
        this.statusSAT.append("<lanMASK>").append(this.lanMASK).append("</lanMASK>");
        this.statusSAT.append("<lanGW>").append(this.lanGW).append("</lanGW>");
        this.statusSAT.append("<lanDNS1>").append(this.lanDNS1).append("</lanDNS1>");
        this.statusSAT.append("<lanDNS2>").append(this.lanDNS2).append("</lanDNS2>");
        this.statusSAT.append("<statLan>").append(this.statusLan).append("</statLan>");
        this.statusSAT.append("<nBat>").append(this.nivelBateria).append("</nBat>");
        this.statusSAT.append("<mtTotal>").append(this.memoriaTotal).append("</mtTotal>");
        this.statusSAT.append("<mtUsada>").append(this.memoriaUsada).append("</mtUsada>");
        this.statusSAT.append("<datahora>").append(this.dataAtual).append("</datahora>");
        this.statusSAT.append("<verSoft>").append(this.versaoSoftwareBasico).append("</verSoft>");
        this.statusSAT.append("<verLay>").append(this.versaoLayout).append("</verLay>");
        this.statusSAT.append("<ultimoCFe>").append(this.ultimoCFe).append("</ultimoCFe>");
        this.statusSAT.append("<listaInicial>").append(this.listaInicial).append("</listaInicial>");
        this.statusSAT.append("<listafinal>").append(this.listaFinal).append("</listafinal>");
        this.statusSAT.append("<dhTransmissao>").append(this.dhTransmissao).append("</dhTransmissao>");
        this.statusSAT.append("<dhComunicacao>").append(this.dhComunicacao).append("</dhComunicacao>");
        this.statusSAT.append("<CERT_EMISSAO>").append(this.CERT_EMISSAO).append("</CERT_EMISSAO>");
        this.statusSAT.append("<CERT_VENCIMENTO>").append(this.CERT_VENCIMENTO).append("</CERT_VENCIMENTO>");
        this.statusSAT.append("<ESTADO_OPERACAO>").append(this.ESTADO_OPERACAO).append("</ESTADO_OPERACAO>");
    }
    
    public String getStatusPipe() {
        final StringBuffer s = new StringBuffer();
        s.append(Configuracoes.SAT.numeroDeSerie).append("|");
        s.append(this.tipoLan).append("|");
        s.append(this.lanIP).append("|");
        s.append(this.lanMAC).append("|");
        s.append(this.lanMASK).append("|");
        s.append(this.lanGW).append("|");
        s.append(this.lanDNS1).append("|");
        s.append(this.lanDNS2).append("|");
        s.append(this.statusLan).append("|");
        s.append(this.nivelBateria).append("|");
        s.append(this.memoriaTotal).append("|");
        s.append(this.memoriaUsada).append("|");
        s.append(this.dataAtual).append("|");
        s.append(this.versaoSoftwareBasico).append("|");
        s.append(this.versaoLayout).append("|");
        s.append(this.ultimoCFe).append("|");
        s.append(this.listaInicial).append("|");
        s.append(this.listaFinal).append("|");
        s.append(this.dhTransmissao).append("|");
        s.append(this.dhComunicacao).append("|");
        s.append(this.CERT_EMISSAO).append("|");
        s.append(this.CERT_VENCIMENTO).append("|");
        s.append(this.ESTADO_OPERACAO);
        return s.toString();
    }
    
    public String getStatusXML() {
        this.statusSAT = new StringBuffer();
        this.completar();
        return this.statusSAT.toString();
    }
}
