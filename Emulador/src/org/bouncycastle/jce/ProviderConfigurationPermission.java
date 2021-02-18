// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import java.security.Permission;
import java.util.StringTokenizer;
import org.bouncycastle.util.Strings;
import java.security.BasicPermission;

public class ProviderConfigurationPermission extends BasicPermission
{
    private static final int THREAD_LOCAL_EC_IMPLICITLY_CA = 1;
    private static final int EC_IMPLICITLY_CA = 2;
    private static final int ALL = 3;
    private static final String THREAD_LOCAL_EC_IMPLICITLY_CA_STR = "threadlocalecimplicitlyca";
    private static final String EC_IMPLICITLY_CA_STR = "ecimplicitlyca";
    private static final String ALL_STR = "all";
    private final String actions;
    private final int permissionMask;
    
    public ProviderConfigurationPermission(final String name) {
        super(name);
        this.actions = "all";
        this.permissionMask = 3;
    }
    
    public ProviderConfigurationPermission(final String name, final String s) {
        super(name, s);
        this.actions = s;
        this.permissionMask = this.calculateMask(s);
    }
    
    private int calculateMask(final String s) {
        final StringTokenizer stringTokenizer = new StringTokenizer(Strings.toLowerCase(s), " ,");
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            final String nextToken = stringTokenizer.nextToken();
            if (nextToken.equals("threadlocalecimplicitlyca")) {
                n |= 0x1;
            }
            else if (nextToken.equals("ecimplicitlyca")) {
                n |= 0x2;
            }
            else {
                if (!nextToken.equals("all")) {
                    continue;
                }
                n |= 0x3;
            }
        }
        if (n == 0) {
            throw new IllegalArgumentException("unknown permissions passed to mask");
        }
        return n;
    }
    
    @Override
    public String getActions() {
        return this.actions;
    }
    
    @Override
    public boolean implies(final Permission permission) {
        if (!(permission instanceof ProviderConfigurationPermission)) {
            return false;
        }
        if (!this.getName().equals(permission.getName())) {
            return false;
        }
        final ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)permission;
        return (this.permissionMask & providerConfigurationPermission.permissionMask) == providerConfigurationPermission.permissionMask;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ProviderConfigurationPermission) {
            final ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)o;
            return this.permissionMask == providerConfigurationPermission.permissionMask && this.getName().equals(providerConfigurationPermission.getName());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode() + this.permissionMask;
    }
}
