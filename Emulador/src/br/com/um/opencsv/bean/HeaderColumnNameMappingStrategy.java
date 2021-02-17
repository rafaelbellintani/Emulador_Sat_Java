// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.opencsv.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.HashMap;
import java.beans.IntrospectionException;
import java.io.IOException;
import br.com.um.opencsv.CSVReader;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class HeaderColumnNameMappingStrategy<T> implements MappingStrategy<T>
{
    protected String[] header;
    protected Map<String, PropertyDescriptor> descriptorMap;
    protected Class<T> type;
    
    public HeaderColumnNameMappingStrategy() {
        this.descriptorMap = null;
    }
    
    @Override
    public void captureHeader(final CSVReader reader) throws IOException {
        this.header = reader.readNext();
    }
    
    @Override
    public PropertyDescriptor findDescriptor(final int col) throws IntrospectionException {
        final String columnName = this.getColumnName(col);
        return (columnName != null && columnName.trim().length() > 0) ? this.findDescriptor(columnName) : null;
    }
    
    protected String getColumnName(final int col) {
        return (this.header != null && col < this.header.length) ? this.header[col] : null;
    }
    
    protected PropertyDescriptor findDescriptor(final String name) throws IntrospectionException {
        if (this.descriptorMap == null) {
            this.descriptorMap = this.loadDescriptorMap(this.getType());
        }
        return this.descriptorMap.get(name.toUpperCase().trim());
    }
    
    protected boolean matches(final String name, final PropertyDescriptor desc) {
        return desc.getName().equals(name.trim());
    }
    
    protected Map<String, PropertyDescriptor> loadDescriptorMap(final Class<T> cls) throws IntrospectionException {
        final Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        final PropertyDescriptor[] descriptors = this.loadDescriptors(this.getType());
        PropertyDescriptor[] array;
        for (int length = (array = descriptors).length, i = 0; i < length; ++i) {
            final PropertyDescriptor descriptor = array[i];
            map.put(descriptor.getName().toUpperCase().trim(), descriptor);
        }
        return map;
    }
    
    private PropertyDescriptor[] loadDescriptors(final Class<T> cls) throws IntrospectionException {
        final BeanInfo beanInfo = Introspector.getBeanInfo(cls);
        return beanInfo.getPropertyDescriptors();
    }
    
    @Override
    public T createBean() throws InstantiationException, IllegalAccessException {
        return this.type.newInstance();
    }
    
    public Class<T> getType() {
        return this.type;
    }
    
    public void setType(final Class<T> type) {
        this.type = type;
    }
}
