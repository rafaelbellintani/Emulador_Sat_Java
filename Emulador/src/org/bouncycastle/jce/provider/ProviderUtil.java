// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.ProviderConfigurationPermission;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.bouncycastle.jce.provider.asymmetric.ec.EC5Util;
import org.bouncycastle.jce.spec.ECParameterSpec;
import java.security.Permission;

public class ProviderUtil
{
    private static final long MAX_MEMORY;
    private static Permission BC_EC_LOCAL_PERMISSION;
    private static Permission BC_EC_PERMISSION;
    private static ThreadLocal threadSpec;
    private static volatile ECParameterSpec ecImplicitCaParams;
    
    static void setParameter(final String s, final Object o) {
        final SecurityManager securityManager = System.getSecurityManager();
        if (s.equals("threadLocalEcImplicitlyCa")) {
            if (securityManager != null) {
                securityManager.checkPermission(ProviderUtil.BC_EC_LOCAL_PERMISSION);
            }
            ECParameterSpec convertSpec;
            if (o instanceof ECParameterSpec || o == null) {
                convertSpec = (ECParameterSpec)o;
            }
            else {
                convertSpec = EC5Util.convertSpec((java.security.spec.ECParameterSpec)o, false);
            }
            if (convertSpec == null) {
                ProviderUtil.threadSpec.remove();
            }
            else {
                ProviderUtil.threadSpec.set(convertSpec);
            }
        }
        else if (s.equals("ecImplicitlyCa")) {
            if (securityManager != null) {
                securityManager.checkPermission(ProviderUtil.BC_EC_PERMISSION);
            }
            if (o instanceof ECParameterSpec || o == null) {
                ProviderUtil.ecImplicitCaParams = (ECParameterSpec)o;
            }
            else {
                ProviderUtil.ecImplicitCaParams = EC5Util.convertSpec((java.security.spec.ECParameterSpec)o, false);
            }
        }
    }
    
    public static ECParameterSpec getEcImplicitlyCa() {
        final ECParameterSpec ecParameterSpec = ProviderUtil.threadSpec.get();
        if (ecParameterSpec != null) {
            return ecParameterSpec;
        }
        return ProviderUtil.ecImplicitCaParams;
    }
    
    static int getReadLimit(final InputStream inputStream) throws IOException {
        if (inputStream instanceof ByteArrayInputStream) {
            return inputStream.available();
        }
        if (ProviderUtil.MAX_MEMORY > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)ProviderUtil.MAX_MEMORY;
    }
    
    static {
        MAX_MEMORY = Runtime.getRuntime().maxMemory();
        ProviderUtil.BC_EC_LOCAL_PERMISSION = new ProviderConfigurationPermission("BC", "threadLocalEcImplicitlyCa");
        ProviderUtil.BC_EC_PERMISSION = new ProviderConfigurationPermission("BC", "ecImplicitlyCa");
        ProviderUtil.threadSpec = new ThreadLocal();
    }
}
