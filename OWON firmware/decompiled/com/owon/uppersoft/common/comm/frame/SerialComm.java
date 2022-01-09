/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.CommPortIdentifier
 *  gnu.io.NoSuchPortException
 *  gnu.io.PortInUseException
 *  gnu.io.SerialPort
 *  gnu.io.SerialPortEvent
 *  gnu.io.SerialPortEventListener
 *  gnu.io.UnsupportedCommOperationException
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import org.eclipse.swt.widgets.Text;

public class SerialComm
implements SerialPortEventListener {
    private Text txtLog;
    private CommPortIdentifier portId;
    private SerialPort SrPt;
    private InputStream is;
    private OutputStream os;
    private Thread t;
    private StringBuffer sb;

    public SerialComm(Text txtLog) {
        this.txtLog = txtLog;
    }

    public boolean openPort(String port) {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier((String)port);
            this.SrPt = (SerialPort)this.portId.open("OWON_SerialPort", 1000);
            return true;
        }
        catch (NoSuchPortException e) {
            e.printStackTrace();
        }
        catch (PortInUseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void init() {
        try {
            this.is = this.SrPt.getInputStream();
            this.os = this.SrPt.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.SrPt.addEventListener((SerialPortEventListener)this);
        }
        catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.SrPt.setSerialPortParams(115200, 8, 1, 0);
        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.SrPt.notifyOnDataAvailable(true);
    }

    public String comm(byte[] send) {
        try {
            this.os.write(send);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.sb = new StringBuffer();
        int timeout = 50;
        do {
            this.waitToReceiveAll(100);
            int start = -1;
            int end = 0;
            int i = 0;
            while (i < this.sb.length()) {
                if (this.sb.charAt(i) == '$') {
                    start = i;
                }
                if (this.sb.charAt(i) == '#' && start > -1) {
                    end = i;
                    return this.sb.substring(start, end + 1);
                }
                ++i;
            }
        } while (timeout-- >= 0);
        return "";
    }

    public void release() {
        block12: {
            try {
                try {
                    if (this.os != null) {
                        this.os.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                        this.SrPt.close();
                    }
                    break block12;
                }
            }
            catch (Throwable throwable) {
                if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                    this.SrPt.close();
                }
                throw throwable;
            }
            if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                this.SrPt.close();
            }
        }
        if (this.t != null) {
            try {
                this.t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case 10: {
                break;
            }
            case 7: {
                break;
            }
            case 9: {
                break;
            }
            case 8: {
                break;
            }
            case 6: {
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                break;
            }
            case 2: {
                break;
            }
            case 1: {
                this.responseOnDataAvaible();
            }
        }
    }

    private void responseOnDataAvaible() {
        int bufferSize = 1024;
        byte[] b = new byte[bufferSize];
        int size = 0;
        try {
            size = this.is.read(b);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.sb.append(new String(b, 0, size));
    }

    private void waitToReceiveAll(int delay) {
        int delayRead = delay;
        try {
            Thread.sleep(delayRead);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

