// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.OptionHandler;
import java.util.StringTokenizer;
import java.util.Enumeration;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.OptionConverter;
import java.net.URL;
import org.apache.log4j.helpers.LogLog;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggerFactory;
import java.util.Hashtable;
import org.apache.log4j.spi.Configurator;

public class PropertyConfigurator implements Configurator
{
    protected Hashtable registry;
    protected LoggerFactory loggerFactory;
    static final String CATEGORY_PREFIX = "log4j.category.";
    static final String LOGGER_PREFIX = "log4j.logger.";
    static final String FACTORY_PREFIX = "log4j.factory";
    static final String ADDITIVITY_PREFIX = "log4j.additivity.";
    static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
    static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
    static final String APPENDER_PREFIX = "log4j.appender.";
    static final String RENDERER_PREFIX = "log4j.renderer.";
    static final String THRESHOLD_PREFIX = "log4j.threshold";
    public static final String LOGGER_FACTORY_KEY = "log4j.loggerFactory";
    private static final String RESET_KEY = "log4j.reset";
    private static final String INTERNAL_ROOT_NAME = "root";
    
    public PropertyConfigurator() {
        this.registry = new Hashtable(11);
        this.loggerFactory = new DefaultCategoryFactory();
    }
    
    public void doConfigure(final String configFileName, final LoggerRepository hierarchy) {
        final Properties props = new Properties();
        FileInputStream istream = null;
        try {
            istream = new FileInputStream(configFileName);
            props.load(istream);
            istream.close();
        }
        catch (Exception e) {
            LogLog.error("Could not read configuration file [" + configFileName + "].", e);
            LogLog.error("Ignoring configuration file [" + configFileName + "].");
            return;
        }
        finally {
            if (istream != null) {
                try {
                    istream.close();
                }
                catch (Throwable t) {}
            }
        }
        this.doConfigure(props, hierarchy);
    }
    
    public static void configure(final String configFilename) {
        new PropertyConfigurator().doConfigure(configFilename, LogManager.getLoggerRepository());
    }
    
    public static void configure(final URL configURL) {
        new PropertyConfigurator().doConfigure(configURL, LogManager.getLoggerRepository());
    }
    
    public static void configure(final Properties properties) {
        new PropertyConfigurator().doConfigure(properties, LogManager.getLoggerRepository());
    }
    
    public static void configureAndWatch(final String configFilename) {
        configureAndWatch(configFilename, 60000L);
    }
    
    public static void configureAndWatch(final String configFilename, final long delay) {
        final PropertyWatchdog pdog = new PropertyWatchdog(configFilename);
        pdog.setDelay(delay);
        pdog.start();
    }
    
    public void doConfigure(final Properties properties, final LoggerRepository hierarchy) {
        String value = properties.getProperty("log4j.debug");
        if (value == null) {
            value = properties.getProperty("log4j.configDebug");
            if (value != null) {
                LogLog.warn("[log4j.configDebug] is deprecated. Use [log4j.debug] instead.");
            }
        }
        if (value != null) {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(value, true));
        }
        final String reset = properties.getProperty("log4j.reset");
        if (reset != null && OptionConverter.toBoolean(reset, false)) {
            hierarchy.resetConfiguration();
        }
        final String thresholdStr = OptionConverter.findAndSubst("log4j.threshold", properties);
        if (thresholdStr != null) {
            hierarchy.setThreshold(OptionConverter.toLevel(thresholdStr, Level.ALL));
            LogLog.debug("Hierarchy threshold set to [" + hierarchy.getThreshold() + "].");
        }
        this.configureRootCategory(properties, hierarchy);
        this.configureLoggerFactory(properties);
        this.parseCatsAndRenderers(properties, hierarchy);
        LogLog.debug("Finished configuring.");
        this.registry.clear();
    }
    
    public void doConfigure(final URL configURL, final LoggerRepository hierarchy) {
        final Properties props = new Properties();
        LogLog.debug("Reading configuration from URL " + configURL);
        InputStream istream = null;
        try {
            istream = configURL.openStream();
            props.load(istream);
        }
        catch (Exception e) {
            LogLog.error("Could not read configuration file from URL [" + configURL + "].", e);
            LogLog.error("Ignoring configuration file [" + configURL + "].");
            return;
        }
        finally {
            if (istream != null) {
                try {
                    istream.close();
                }
                catch (Exception ex) {}
            }
        }
        this.doConfigure(props, hierarchy);
    }
    
    protected void configureLoggerFactory(final Properties props) {
        final String factoryClassName = OptionConverter.findAndSubst("log4j.loggerFactory", props);
        if (factoryClassName != null) {
            LogLog.debug("Setting category factory to [" + factoryClassName + "].");
            PropertySetter.setProperties(this.loggerFactory = (LoggerFactory)OptionConverter.instantiateByClassName(factoryClassName, LoggerFactory.class, this.loggerFactory), props, "log4j.factory.");
        }
    }
    
    void configureRootCategory(final Properties props, final LoggerRepository hierarchy) {
        String effectiveFrefix = "log4j.rootLogger";
        String value = OptionConverter.findAndSubst("log4j.rootLogger", props);
        if (value == null) {
            value = OptionConverter.findAndSubst("log4j.rootCategory", props);
            effectiveFrefix = "log4j.rootCategory";
        }
        if (value == null) {
            LogLog.debug("Could not find root logger information. Is this OK?");
        }
        else {
            final Logger root = hierarchy.getRootLogger();
            synchronized (root) {
                this.parseCategory(props, root, effectiveFrefix, "root", value);
            }
        }
    }
    
    protected void parseCatsAndRenderers(final Properties props, final LoggerRepository hierarchy) {
        final Enumeration enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            if (key.startsWith("log4j.category.") || key.startsWith("log4j.logger.")) {
                String loggerName = null;
                if (key.startsWith("log4j.category.")) {
                    loggerName = key.substring("log4j.category.".length());
                }
                else if (key.startsWith("log4j.logger.")) {
                    loggerName = key.substring("log4j.logger.".length());
                }
                final String value = OptionConverter.findAndSubst(key, props);
                final Logger logger = hierarchy.getLogger(loggerName, this.loggerFactory);
                synchronized (logger) {
                    this.parseCategory(props, logger, key, loggerName, value);
                    this.parseAdditivityForLogger(props, logger, loggerName);
                }
            }
            else {
                if (!key.startsWith("log4j.renderer.")) {
                    continue;
                }
                final String renderedClass = key.substring("log4j.renderer.".length());
                final String renderingClass = OptionConverter.findAndSubst(key, props);
                if (!(hierarchy instanceof RendererSupport)) {
                    continue;
                }
                RendererMap.addRenderer((RendererSupport)hierarchy, renderedClass, renderingClass);
            }
        }
    }
    
    void parseAdditivityForLogger(final Properties props, final Logger cat, final String loggerName) {
        final String value = OptionConverter.findAndSubst("log4j.additivity." + loggerName, props);
        LogLog.debug("Handling log4j.additivity." + loggerName + "=[" + value + "]");
        if (value != null && !value.equals("")) {
            final boolean additivity = OptionConverter.toBoolean(value, true);
            LogLog.debug("Setting additivity for \"" + loggerName + "\" to " + additivity);
            cat.setAdditivity(additivity);
        }
    }
    
    void parseCategory(final Properties props, final Logger logger, final String optionKey, final String loggerName, final String value) {
        LogLog.debug("Parsing for [" + loggerName + "] with value=[" + value + "].");
        final StringTokenizer st = new StringTokenizer(value, ",");
        if (!value.startsWith(",") && !value.equals("")) {
            if (!st.hasMoreTokens()) {
                return;
            }
            final String levelStr = st.nextToken();
            LogLog.debug("Level token is [" + levelStr + "].");
            if ("inherited".equalsIgnoreCase(levelStr) || "null".equalsIgnoreCase(levelStr)) {
                if (loggerName.equals("root")) {
                    LogLog.warn("The root logger cannot be set to null.");
                }
                else {
                    logger.setLevel(null);
                }
            }
            else {
                logger.setLevel(OptionConverter.toLevel(levelStr, Level.DEBUG));
            }
            LogLog.debug("Category " + loggerName + " set to " + logger.getLevel());
        }
        logger.removeAllAppenders();
        while (st.hasMoreTokens()) {
            final String appenderName = st.nextToken().trim();
            if (appenderName != null) {
                if (appenderName.equals(",")) {
                    continue;
                }
                LogLog.debug("Parsing appender named \"" + appenderName + "\".");
                final Appender appender = this.parseAppender(props, appenderName);
                if (appender == null) {
                    continue;
                }
                logger.addAppender(appender);
            }
        }
    }
    
    Appender parseAppender(final Properties props, final String appenderName) {
        Appender appender = this.registryGet(appenderName);
        if (appender != null) {
            LogLog.debug("Appender \"" + appenderName + "\" was already parsed.");
            return appender;
        }
        final String prefix = "log4j.appender." + appenderName;
        final String layoutPrefix = prefix + ".layout";
        appender = (Appender)OptionConverter.instantiateByKey(props, prefix, Appender.class, null);
        if (appender == null) {
            LogLog.error("Could not instantiate appender named \"" + appenderName + "\".");
            return null;
        }
        appender.setName(appenderName);
        if (appender instanceof OptionHandler) {
            if (appender.requiresLayout()) {
                final Layout layout = (Layout)OptionConverter.instantiateByKey(props, layoutPrefix, Layout.class, null);
                if (layout != null) {
                    appender.setLayout(layout);
                    LogLog.debug("Parsing layout options for \"" + appenderName + "\".");
                    PropertySetter.setProperties(layout, props, layoutPrefix + ".");
                    LogLog.debug("End of parsing for \"" + appenderName + "\".");
                }
            }
            PropertySetter.setProperties(appender, props, prefix + ".");
            LogLog.debug("Parsed \"" + appenderName + "\" options.");
        }
        this.registryPut(appender);
        return appender;
    }
    
    void registryPut(final Appender appender) {
        this.registry.put(appender.getName(), appender);
    }
    
    Appender registryGet(final String name) {
        return this.registry.get(name);
    }
}
