/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.KeyEvent
 *  org.eclipse.swt.events.KeyListener
 *  org.eclipse.swt.events.VerifyEvent
 *  org.eclipse.swt.events.VerifyListener
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.MessageBox
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class IPTextField
extends Composite
implements VerifyListener,
KeyListener {
    private Text text_1;
    private Text text_2;
    private Text text_3;
    private Text text_4;
    private boolean listen;

    public IPTextField(Composite parent, int style) {
        super(parent, style);
        this.setBackground(this.getDisplay().getSystemColor(1));
        this.createContent();
    }

    private void createContent() {
        GridLayout gridLayout = new GridLayout(7, false);
        gridLayout.marginRight = 2;
        gridLayout.marginTop = 1;
        gridLayout.marginBottom = 1;
        gridLayout.marginLeft = 3;
        gridLayout.verticalSpacing = 0;
        gridLayout.numColumns = 7;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        this.setLayout((Layout)gridLayout);
        this.text_1 = this.internalGetText();
        this.internalGetLabel();
        this.text_2 = this.internalGetText();
        this.internalGetLabel();
        this.text_3 = this.internalGetText();
        this.internalGetLabel();
        this.text_4 = this.internalGetText();
        this.text_1.setFocus();
        this.listen = true;
    }

    public void keyPressed(KeyEvent e) {
        if (!this.listen) {
            return;
        }
        Text text = (Text)e.getSource();
        int position = text.getCaretPosition();
        int tlen = text.getText().length();
        if (e.stateMask != 131072) {
            switch (e.keyCode) {
                case 0x1000001: 
                case 0x1000003: {
                    if (position != 0) break;
                    this.previousFocus(text);
                    break;
                }
                case 0x1000002: 
                case 0x1000004: {
                    if (tlen != position) break;
                    this.nextFocus(text);
                    break;
                }
                case 8: {
                    if (text.getSelectionText().length() != 0 || position != 0) break;
                    this.previousFocus(text);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void verifyText(VerifyEvent e) {
        if (!this.listen) {
            return;
        }
        Text text = (Text)e.getSource();
        String text_string = text.getText();
        char c = e.character;
        if (text_string.length() >= text.getTextLimit()) {
            return;
        }
        if ("0123456789".indexOf(e.text) < 0) {
            e.doit = false;
            return;
        }
        if (e.keyCode == 8) {
            e.doit = true;
            return;
        }
        if (e.keyCode == 127) {
            e.doit = true;
            return;
        }
        text_string = text_string.substring(0, text.getSelection().x);
        int position = text.getCaretPosition();
        StringBuilder buffer = new StringBuilder(text_string);
        text_string = buffer.insert(position, e.text).toString();
        if (text == this.text_1) {
            if (Integer.parseInt(text_string) > 233) {
                this.errorMessageBox(text, text_string, "233");
                return;
            }
        } else if (Integer.parseInt(text_string) > 255) {
            this.errorMessageBox(text, text_string, "255");
            return;
        }
        if (text_string.length() >= text.getTextLimit() && c != '\b') {
            this.nextFocus(text);
        }
    }

    private void nextFocus(Text text) {
        if (text == this.text_4) {
            return;
        }
        if (text == this.text_1) {
            this.text_2.setFocus();
            this.toFirst(this.text_2);
        } else if (text == this.text_2) {
            this.text_3.setFocus();
            this.toFirst(this.text_3);
        } else if (text == this.text_3) {
            this.text_4.setFocus();
            this.toFirst(this.text_4);
        }
    }

    private void previousFocus(Text text) {
        if (text == this.text_1) {
            return;
        }
        if (text == this.text_2) {
            this.text_1.setFocus();
            this.toLast(this.text_1);
        } else if (text == this.text_3) {
            this.text_2.setFocus();
            this.toLast(this.text_2);
        } else if (text == this.text_4) {
            this.text_3.setFocus();
            this.toLast(this.text_3);
        }
    }

    private void toLast(Text text) {
        int len = text.getText().length();
        text.setSelection(len);
    }

    private void toFirst(Text text) {
        text.setSelection(0);
    }

    public void setText(String text) throws Exception {
        if (!this.isValid(text)) {
            throw new Exception("IP Address invalid!");
        }
        String[] ip = text.split("\\.");
        this.listen = false;
        this.text_1.setText(ip[0]);
        this.text_2.setText(ip[1]);
        this.text_3.setText(ip[2]);
        this.text_4.setText(ip[3]);
        this.listen = true;
    }

    protected void setText(Text text, String text_string) {
        this.listen = false;
        text.setText(text_string);
        text.setFocus();
        this.listen = true;
    }

    private Text internalGetText() {
        Text text = new Text((Composite)this, 0x1000000);
        text.setTextLimit(3);
        GridData gridData = new GridData(32, -1);
        text.setLayoutData((Object)gridData);
        text.addVerifyListener((VerifyListener)this);
        text.addKeyListener((KeyListener)this);
        return text;
    }

    private Label internalGetLabel() {
        Label label = new Label((Composite)this, 4);
        label.setBackground(this.getDisplay().getSystemColor(1));
        label.setText(".");
        return label;
    }

    private void errorMessageBox(Text text, String error_string, String right_string) {
        MessageBox mbox = new MessageBox(this.getParent().getShell(), 65536);
        mbox.setText("Error");
        mbox.setMessage(String.valueOf(error_string) + "Not a valid number, please enter a number between 1 and" + right_string);
        int r = mbox.open();
        if (r == 32) {
            this.setText(text, right_string);
            return;
        }
    }

    public String getText() {
        return String.valueOf(this.text_1.getText()) + "." + this.text_2.getText() + "." + this.text_3.getText() + "." + this.text_4.getText();
    }

    public String toString() {
        return "IP: " + this.getText();
    }

    private boolean isValid(String source) {
        String reg = "^(22[0-3]|2[0-1]\\d|1\\d{2}|[1-9]\\d|[1-9])\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source);
        return m.find();
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.setSize(170, 50);
        Display display = shell.getDisplay();
        shell.setLayout((Layout)new FillLayout());
        new IPTextField((Composite)shell, 0);
        shell.layout();
        shell.open();
        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
    }
}

