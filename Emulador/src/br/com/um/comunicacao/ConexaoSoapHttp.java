// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.comunicacao;

import java.io.OutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoSoapHttp implements Conexao
{
    private String response;
    private String request;
    
    public ConexaoSoapHttp() {
        this.response = null;
        this.request = null;
    }
    
    @Override
    public boolean consumir(final URL url, final String soapAction) {
        try {
            final HttpURLConnection conexao = (HttpURLConnection)url.openConnection();
            conexao.setDoOutput(true);
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            conexao.setRequestProperty("SOAPAction", soapAction);
            final String reqXML = this.request;
            final OutputStream reqStream = conexao.getOutputStream();
            reqStream.write(reqXML.getBytes("utf-8"));
            BufferedReader rd = null;
            try {
                rd = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "utf-8"));
            }
            catch (Exception e2) {
                try {
                    rd = new BufferedReader(new InputStreamReader(conexao.getErrorStream(), "utf-8"));
                }
                catch (Exception ee) {
                    System.out.println(String.valueOf(conexao.getResponseCode()) + " - " + conexao.getResponseMessage());
                    throw new Exception();
                }
            }
            int character = 0;
            final StringBuffer strBuffer = new StringBuffer(conexao.getContentLength() + 100);
            while ((character = rd.read()) != -1) {
                strBuffer.append((char)character);
            }
            this.response = strBuffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public String getResponse() {
        return this.response;
    }
    
    @Override
    public void setRequest(final String request) {
        this.request = request;
    }
}
