// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.security.spec.InvalidKeySpecException;
import br.com.um.controles.StringUtil;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.controles.ControleDados;

public class ControladorTrocarCodigoAtivacao
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorTrocarCodigoAtivacao(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String tratarMensagem(final String codigoDeAtivacao, final String opcao, final String novoCodigo, final String aviso) {
        try {
            if (opcao.equals("2")) {
                return "18002|C\u00f3digo de ativa\u00e7\u00e3o de emerg\u00eancia Incorreto.||" + aviso;
            }
            final String chave = ControleDados.gerarHashMD5(codigoDeAtivacao);
            final String base64ks = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA);
            byte[] ks;
            try {
                if (!StringUtil.isBase64(base64ks)) {
                    throw new Exception();
                }
                ks = StringUtil.base64Decode(base64ks);
            }
            catch (Exception e2) {
                throw new InvalidKeySpecException();
            }
            byte[] ksDescriptografado;
            try {
                ksDescriptografado = ControleSeguranca.descriptografarAES(chave.getBytes(), ks);
            }
            catch (Exception e3) {
                ControleLogs.logar("C\u00f3digo de ativa\u00e7\u00e3o Incorreto.");
                return "18001|C\u00f3digo de ativa\u00e7\u00e3o Incorreto.||" + aviso;
            }
            final String[] keyStore = new String(ksDescriptografado).split("\\:");
            final String publica = keyStore[0];
            final String privada = keyStore[1];
            final String keystore = StringUtil.base64Encode(ControleSeguranca.criptografarAES(ControleDados.gerarHashMD5(novoCodigo).getBytes(), (String.valueOf(publica) + ":" + privada).getBytes()));
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA, keystore.toCharArray());
            ControleSeguranca.gravarCodigoAtivacao(novoCodigo);
            ControleLogs.logar("FIM TROCAR CODIGO ATIVACAO");
            return "18000|C\u00f3digo de ativa\u00e7\u00e3o alterado com sucesso.||" + aviso;
        }
        catch (InvalidKeySpecException e4) {
            ControleLogs.logar("Par de chaves corrompido.");
            return "18099||Par de chaves corrompido.||" + aviso;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "18099|Erro de desconhecido||" + aviso;
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao, final String opcao, final String novoCodigo, final String confNovoCodigo, final String aviso) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "18099|Erro desconhecido";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido..");
                return "18001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido..";
            }
            if (opcao == null || !(opcao.equals("1") ^ opcao.equals("2"))) {
                ControleLogs.logar("ERRO: op\u00e7\u00e3o invalida.");
                return "18099|Erro desconhecido";
            }
            if (novoCodigo == null || novoCodigo.length() < 6) {
                ControleLogs.logar("ERRO: Novo c\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "18099|Erro desconhecido";
            }
            if (confNovoCodigo == null || !novoCodigo.equals(confNovoCodigo)) {
                ControleLogs.logar("ERRO: Novo c\u00f3digo de ativa\u00e7\u00e3o n\u00e3o confere.");
                return "18099|Erro desconhecido";
            }
            if (confNovoCodigo == null || !novoCodigo.equals(confNovoCodigo)) {
                ControleLogs.logar("ERRO: Novo c\u00f3digo de ativa\u00e7\u00e3o n\u00e3o confere.");
                return "18099|Erro desconhecido";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "18099|Erro desconhecido";
        }
        return r;
    }
}
