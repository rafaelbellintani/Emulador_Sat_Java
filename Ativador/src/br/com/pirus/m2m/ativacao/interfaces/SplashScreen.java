// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import java.awt.Graphics;
import java.awt.Window;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.net.URL;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Frame;

public final class SplashScreen extends Frame
{
    private static final long serialVersionUID = -2159892691242131213L;
    private final String fImageId;
    private MediaTracker fMediaTracker;
    private Image fImage;
    private static final ImageObserver NO_OBSERVER;
    private static final int IMAGE_ID = 0;
    
    static {
        NO_OBSERVER = null;
    }
    
    public SplashScreen(final String aImageId) {
        if (aImageId == null || aImageId.trim().length() == 0) {
            throw new IllegalArgumentException("Image Id does not have content.");
        }
        this.fImageId = aImageId;
    }
    
    public void splash() {
        this.initImageAndTracker();
        this.setSize(this.fImage.getWidth(SplashScreen.NO_OBSERVER), this.fImage.getHeight(SplashScreen.NO_OBSERVER));
        this.center();
        this.fMediaTracker.addImage(this.fImage, 0);
        try {
            this.fMediaTracker.waitForID(0);
        }
        catch (InterruptedException ex) {
            System.out.println("Cannot track image load.");
        }
        new SplashWindow(this, this.fImage);
    }
    
    @Override
    public void disable() {
        this.setVisible(false);
    }
    
    private void initImageAndTracker() {
        this.fMediaTracker = new MediaTracker(this);
        final URL imageURL = SplashScreen.class.getResource(this.fImageId);
        this.fImage = Toolkit.getDefaultToolkit().getImage(imageURL);
    }
    
    private void center() {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final Rectangle frame = this.getBounds();
        this.setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }
    
    public static void main(final String... aArgs) {
        final SplashScreen splashScreen = new SplashScreen("splash.gif");
        splashScreen.splash();
        try {
            Thread.sleep(20000L);
        }
        catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }
    
    private class SplashWindow extends Window
    {
        private static final long serialVersionUID = 2525462850892691668L;
        private Image fImage;
        
        SplashWindow(final Frame aParent, final Image aImage) {
            super(aParent);
            this.fImage = aImage;
            this.setSize(this.fImage.getWidth(SplashScreen.NO_OBSERVER), this.fImage.getHeight(SplashScreen.NO_OBSERVER));
            final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            final Rectangle window = this.getBounds();
            this.setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
            this.setVisible(true);
        }
        
        @Override
        public void paint(final Graphics graphics) {
            if (this.fImage != null) {
                graphics.drawImage(this.fImage, 0, 0, this);
            }
        }
    }
}
