// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.conexao;

import java.io.OutputStream;
import java.net.UnknownHostException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoSoapHttp implements Conexao
{
    private String response;
    private String request;
    private String responseCode;
    private String responseMessage;
    private int timeout;
    
    public ConexaoSoapHttp() {
        this.response = null;
        this.request = null;
        this.responseCode = null;
        this.responseMessage = null;
        this.timeout = 0;
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
                this.responseCode = String.valueOf(conexao.getResponseCode());
                this.responseMessage = conexao.getResponseMessage();
            }
            catch (Exception e) {
                try {
                    this.responseCode = String.valueOf(conexao.getResponseCode());
                    this.responseMessage = conexao.getResponseMessage();
                    rd = new BufferedReader(new InputStreamReader(conexao.getErrorStream(), "utf-8"));
                }
                catch (Exception e4) {
                    System.out.println(String.valueOf(conexao.getResponseCode()) + " - " + conexao.getResponseMessage());
                    e.printStackTrace();
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
        catch (UnknownHostException e2) {
            System.err.println(url);
            e2.printStackTrace();
        }
        catch (Exception e3) {
            System.err.println(url);
            e3.printStackTrace();
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
    
    @Override
    public String getResponseCode() {
        return this.responseCode;
    }
    
    @Override
    public String getResponseMessage() {
        return this.responseMessage;
    }
    
    @Override
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
}
