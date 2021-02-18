// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

public class CredenciadorasCartao
{
    private static String[] codigos;
    
    static {
        CredenciadorasCartao.codigos = new String[] { "001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033", "034", "999" };
    }
    
    public static boolean existe(final String codigo) {
        for (int i = 0; i < CredenciadorasCartao.codigos.length; ++i) {
            if (CredenciadorasCartao.codigos[i].equals(codigo)) {
                return true;
            }
        }
        return false;
    }
}
