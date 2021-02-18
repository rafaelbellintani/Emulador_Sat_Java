// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Button;
import java.awt.TextField;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.FileOutputStream;
import java.awt.Panel;
import java.awt.Checkbox;
import java.awt.Frame;

class Configure extends Frame
{
    Checkbox[] cb;
    Panel p1;
    static final int PORT_SERIAL = 1;
    static final int PORT_PARALLEL = 2;
    int PortType;
    String EnumMessage;
    
    private void saveSpecifiedPorts() {
        final String s = new String(System.getProperty("java.home"));
        final String property = System.getProperty("path.separator", ":");
        final String property2 = System.getProperty("file.separator", "/");
        final String property3 = System.getProperty("line.separator");
        String s2;
        if (this.PortType == 1) {
            s2 = new String(s + property2 + "lib" + property2 + "gnu.io.rxtx.SerialPorts");
        }
        else {
            if (this.PortType != 2) {
                System.out.println("Bad Port Type!");
                return;
            }
            s2 = new String(s + "gnu.io.rxtx.ParallelPorts");
        }
        System.out.println(s2);
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(s2);
            for (int i = 0; i < 128; ++i) {
                if (this.cb[i].getState()) {
                    fileOutputStream.write(new String(this.cb[i].getLabel() + property).getBytes());
                }
            }
            fileOutputStream.write(property3.getBytes());
            fileOutputStream.close();
        }
        catch (IOException ex) {
            System.out.println("IOException!");
        }
    }
    
    void addCheckBoxes(final String str) {
        for (int i = 0; i < 128; ++i) {
            if (this.cb[i] != null) {
                this.p1.remove(this.cb[i]);
            }
        }
        for (int j = 1; j < 129; ++j) {
            this.cb[j - 1] = new Checkbox(str + j);
            this.p1.add("NORTH", this.cb[j - 1]);
        }
    }
    
    public Configure() {
        this.PortType = 1;
        this.EnumMessage = new String("gnu.io.rxtx.properties has not been detected.\n\nThere is no consistant means of detecting ports on this operating System.  It is necessary to indicate which ports are valid on this system before proper port enumeration can happen.  Please check the ports that are valid on this system and select Save");
        final int n = 640;
        final int height = 480;
        this.cb = new Checkbox[128];
        final Frame frame = new Frame("Configure gnu.io.rxtx.properties");
        String text;
        if (System.getProperty("file.separator", "/").compareTo("/") != 0) {
            text = "COM";
        }
        else {
            text = "/dev/";
        }
        frame.setBounds(100, 50, n, height);
        frame.setLayout(new BorderLayout());
        (this.p1 = new Panel()).setLayout(new GridLayout(16, 4));
        final ActionListener actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equals("Save")) {
                    Configure.this.saveSpecifiedPorts();
                }
            }
        };
        this.addCheckBoxes(text);
        final TextArea comp = new TextArea(this.EnumMessage, 5, 50, 3);
        comp.setSize(50, n);
        comp.setEditable(false);
        final Panel comp2 = new Panel();
        comp2.add(new Label("Port Name:"));
        final TextField comp3 = new TextField(text, 8);
        comp3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                Configure.this.addCheckBoxes(actionEvent.getActionCommand());
                frame.setVisible(true);
            }
        });
        comp2.add(comp3);
        comp2.add(new Checkbox("Keep Ports"));
        final Button[] array = new Button[6];
        for (int n2 = 0, i = 4; i < 129; i *= 2, ++n2) {
            (array[n2] = new Button("1-" + i)).addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent actionEvent) {
                    for (int int1 = Integer.parseInt(actionEvent.getActionCommand().substring(2)), i = 0; i < int1; ++i) {
                        Configure.this.cb[i].setState(!Configure.this.cb[i].getState());
                        frame.setVisible(true);
                    }
                }
            });
            comp2.add(array[n2]);
        }
        final Button comp4 = new Button("More");
        final Button comp5 = new Button("Save");
        comp4.addActionListener(actionListener);
        comp5.addActionListener(actionListener);
        comp2.add(comp4);
        comp2.add(comp5);
        frame.add("South", comp2);
        frame.add("Center", this.p1);
        frame.add("North", comp);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
    
    public static void main(final String[] array) {
        new Configure();
    }
}
