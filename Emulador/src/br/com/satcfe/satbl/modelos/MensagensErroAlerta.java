// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import java.util.ArrayList;

public class MensagensErroAlerta
{
    private ArrayList<MsgErro> msgs;
    
    public MensagensErroAlerta() {
        this.msgs = null;
    }
    
    public void carregarArquivoErrosAlertas() {
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS)) {
            try {
                final String xml = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS);
                final Document docErros = ControleDados.parseXML(xml);
                final NodeList filhos = docErros.getFirstChild().getChildNodes();
                for (int i = 0; i < filhos.getLength(); ++i) {
                    final Node filhoAtual = filhos.item(i);
                    if (filhoAtual.getNodeName().indexOf("cod") != -1) {
                        if (this.msgs == null) {
                            this.msgs = new ArrayList<MsgErro>();
                        }
                        this.msgs.add(new MsgErro(filhoAtual.getNodeName(), filhoAtual.getTextContent()));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String getMsg(final String cod) {
        if (this.msgs == null || this.msgs.size() <= 0) {
            return null;
        }
        for (int i = 0; i < this.msgs.size(); ++i) {
            if (this.msgs.get(i).getCod().equals(cod)) {
                return this.msgs.get(i).getMsg();
            }
        }
        return null;
    }
    
    public static String getMensagemErroAlerta(final String cod) {
        if (Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS != null && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS)) {
            try {
                final String xml = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS_ALERTAS);
                if (xml.indexOf("cod" + cod) > 0) {
                    return ControleDados.getConteudoTAG(xml, "cod" + cod);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public class MsgErro
    {
        private String cod;
        private String msg;
        
        public MsgErro(final String cod, final String msg) {
            this.cod = null;
            this.msg = null;
            this.cod = cod.replace("cod", "");
            this.msg = msg;
        }
        
        public void setCod(final String cod) {
            this.cod = cod;
        }
        
        public void setMsg(final String msg) {
            this.msg = msg;
        }
        
        public String getCod() {
            return this.cod;
        }
        
        public String getMsg() {
            return this.msg;
        }
    }
}
