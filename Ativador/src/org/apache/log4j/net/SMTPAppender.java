// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import javax.mail.Part;
import java.util.Hashtable;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Element;
import org.apache.log4j.helpers.OptionConverter;
import javax.mail.Multipart;
import javax.mail.Transport;
import java.util.Date;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Layout;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.log4j.spi.LoggingEvent;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Session;
import org.apache.log4j.spi.OptionHandler;
import javax.mail.MessagingException;
import org.apache.log4j.helpers.LogLog;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import javax.mail.Message;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.apache.log4j.AppenderSkeleton;

public class SMTPAppender extends AppenderSkeleton implements UnrecognizedElementHandler
{
    private String to;
    private String cc;
    private String bcc;
    private String from;
    private String subject;
    private String smtpHost;
    private String smtpUsername;
    private String smtpPassword;
    private boolean smtpDebug;
    private int bufferSize;
    private boolean locationInfo;
    protected CyclicBuffer cb;
    protected Message msg;
    protected TriggeringEventEvaluator evaluator;
    
    public SMTPAppender() {
        this(new DefaultEvaluator());
    }
    
    public SMTPAppender(final TriggeringEventEvaluator evaluator) {
        this.smtpDebug = false;
        this.bufferSize = 512;
        this.locationInfo = false;
        this.cb = new CyclicBuffer(this.bufferSize);
        this.evaluator = evaluator;
    }
    
    public void activateOptions() {
        final Session session = this.createSession();
        this.msg = (Message)new MimeMessage(session);
        try {
            this.addressMessage(this.msg);
            if (this.subject != null) {
                this.msg.setSubject(this.subject);
            }
        }
        catch (MessagingException e) {
            LogLog.error("Could not activate SMTPAppender options.", (Throwable)e);
        }
        if (this.evaluator instanceof OptionHandler) {
            ((OptionHandler)this.evaluator).activateOptions();
        }
    }
    
    protected void addressMessage(final Message msg) throws MessagingException {
        if (this.from != null) {
            msg.setFrom((Address)this.getAddress(this.from));
        }
        else {
            msg.setFrom();
        }
        if (this.to != null && this.to.length() > 0) {
            msg.setRecipients(Message.RecipientType.TO, (Address[])this.parseAddress(this.to));
        }
        if (this.cc != null && this.cc.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, (Address[])this.parseAddress(this.cc));
        }
        if (this.bcc != null && this.bcc.length() > 0) {
            msg.setRecipients(Message.RecipientType.BCC, (Address[])this.parseAddress(this.bcc));
        }
    }
    
    protected Session createSession() {
        Properties props = null;
        try {
            props = new Properties(System.getProperties());
        }
        catch (SecurityException ex) {
            props = new Properties();
        }
        if (this.smtpHost != null) {
            ((Hashtable<String, String>)props).put("mail.smtp.host", this.smtpHost);
        }
        Authenticator auth = null;
        if (this.smtpPassword != null && this.smtpUsername != null) {
            ((Hashtable<String, String>)props).put("mail.smtp.auth", "true");
            auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTPAppender.this.smtpUsername, SMTPAppender.this.smtpPassword);
                }
            };
        }
        final Session session = Session.getInstance(props, auth);
        if (this.smtpDebug) {
            session.setDebug(this.smtpDebug);
        }
        return session;
    }
    
    public void append(final LoggingEvent event) {
        if (!this.checkEntryConditions()) {
            return;
        }
        event.getThreadName();
        event.getNDC();
        event.getMDCCopy();
        if (this.locationInfo) {
            event.getLocationInformation();
        }
        this.cb.add(event);
        if (this.evaluator.isTriggeringEvent(event)) {
            this.sendBuffer();
        }
    }
    
    protected boolean checkEntryConditions() {
        if (this.msg == null) {
            super.errorHandler.error("Message object not configured.");
            return false;
        }
        if (this.evaluator == null) {
            super.errorHandler.error("No TriggeringEventEvaluator is set for appender [" + super.name + "].");
            return false;
        }
        if (super.layout == null) {
            super.errorHandler.error("No layout set for appender named [" + super.name + "].");
            return false;
        }
        return true;
    }
    
    public synchronized void close() {
        super.closed = true;
    }
    
    InternetAddress getAddress(final String addressStr) {
        try {
            return new InternetAddress(addressStr);
        }
        catch (AddressException e) {
            super.errorHandler.error("Could not parse address [" + addressStr + "].", (Exception)e, 6);
            return null;
        }
    }
    
    InternetAddress[] parseAddress(final String addressStr) {
        try {
            return InternetAddress.parse(addressStr, true);
        }
        catch (AddressException e) {
            super.errorHandler.error("Could not parse address [" + addressStr + "].", (Exception)e, 6);
            return null;
        }
    }
    
    public String getTo() {
        return this.to;
    }
    
    public boolean requiresLayout() {
        return true;
    }
    
    protected void sendBuffer() {
        try {
            final MimeBodyPart part = new MimeBodyPart();
            final StringBuffer sbuf = new StringBuffer();
            String t = super.layout.getHeader();
            if (t != null) {
                sbuf.append(t);
            }
            for (int len = this.cb.length(), i = 0; i < len; ++i) {
                final LoggingEvent event = this.cb.get();
                sbuf.append(super.layout.format(event));
                if (super.layout.ignoresThrowable()) {
                    final String[] s = event.getThrowableStrRep();
                    if (s != null) {
                        for (int j = 0; j < s.length; ++j) {
                            sbuf.append(s[j]);
                            sbuf.append(Layout.LINE_SEP);
                        }
                    }
                }
            }
            t = super.layout.getFooter();
            if (t != null) {
                sbuf.append(t);
            }
            part.setContent((Object)sbuf.toString(), super.layout.getContentType());
            final Multipart mp = (Multipart)new MimeMultipart();
            mp.addBodyPart((BodyPart)part);
            ((Part)this.msg).setContent(mp);
            this.msg.setSentDate(new Date());
            Transport.send(this.msg);
        }
        catch (Exception e) {
            LogLog.error("Error occured while sending e-mail notification.", e);
        }
    }
    
    public String getEvaluatorClass() {
        return (this.evaluator == null) ? null : this.evaluator.getClass().getName();
    }
    
    public String getFrom() {
        return this.from;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public void setSubject(final String subject) {
        this.subject = subject;
    }
    
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
        this.cb.resize(bufferSize);
    }
    
    public void setSMTPHost(final String smtpHost) {
        this.smtpHost = smtpHost;
    }
    
    public String getSMTPHost() {
        return this.smtpHost;
    }
    
    public void setTo(final String to) {
        this.to = to;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public void setEvaluatorClass(final String value) {
        this.evaluator = (TriggeringEventEvaluator)OptionConverter.instantiateByClassName(value, TriggeringEventEvaluator.class, this.evaluator);
    }
    
    public void setLocationInfo(final boolean locationInfo) {
        this.locationInfo = locationInfo;
    }
    
    public boolean getLocationInfo() {
        return this.locationInfo;
    }
    
    public void setCc(final String addresses) {
        this.cc = addresses;
    }
    
    public String getCc() {
        return this.cc;
    }
    
    public void setBcc(final String addresses) {
        this.bcc = addresses;
    }
    
    public String getBcc() {
        return this.bcc;
    }
    
    public void setSMTPPassword(final String password) {
        this.smtpPassword = password;
    }
    
    public void setSMTPUsername(final String username) {
        this.smtpUsername = username;
    }
    
    public void setSMTPDebug(final boolean debug) {
        this.smtpDebug = debug;
    }
    
    public String getSMTPPassword() {
        return this.smtpPassword;
    }
    
    public String getSMTPUsername() {
        return this.smtpUsername;
    }
    
    public boolean getSMTPDebug() {
        return this.smtpDebug;
    }
    
    public final void setEvaluator(final TriggeringEventEvaluator trigger) {
        if (trigger == null) {
            throw new NullPointerException("trigger");
        }
        this.evaluator = trigger;
    }
    
    public final TriggeringEventEvaluator getEvaluator() {
        return this.evaluator;
    }
    
    public boolean parseUnrecognizedElement(final Element element, final Properties props) throws Exception {
        if ("triggeringPolicy".equals(element.getNodeName())) {
            final Object triggerPolicy = DOMConfigurator.parseElement(element, props, TriggeringEventEvaluator.class);
            if (triggerPolicy instanceof TriggeringEventEvaluator) {
                this.setEvaluator((TriggeringEventEvaluator)triggerPolicy);
            }
            return true;
        }
        return false;
    }
}
