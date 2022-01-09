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
 *  org.eclipse.swt.widgets.Listener
 */
package com.owon.uppersoft.patch.direct.serial;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import org.eclipse.swt.widgets.Listener;

public class SerialPortUtil
implements SerialPortEventListener {
    private Listener l;
    private CommPortIdentifier portId;
    private SerialPort SrPt;
    private InputStream is;
    private OutputStream os;

    public static List<String> loadAvailablePort() {
        ArrayList<String> avaliblePortList = new ArrayList<String>(5);
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier)portList.nextElement();
            if (portId.getPortType() != 1) continue;
            avaliblePortList.add(portId.getName());
        }
        return avaliblePortList;
    }

    public SerialPortUtil(Listener l) {
        this.l = l;
    }

    public SerialPort openPort(String port) {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier((String)port);
            this.SrPt = (SerialPort)this.portId.open("OWON_SerialPort", 1000);
            return this.SrPt;
        }
        catch (NoSuchPortException e) {
            e.printStackTrace();
        }
        catch (PortInUseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getInputStream() {
        return this.is;
    }

    public OutputStream getOutputStream() {
        return this.os;
    }

    public void loadPort(int baudRate, int dataBits, int stopBits, int parity) {
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
            this.SrPt.setSerialPortParams(baudRate, dataBits, stopBits, parity);
        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.SrPt.notifyOnDataAvailable(true);
    }

    public void closeCOM() {
        try {
            try {
                if (this.is != null) {
                    this.is.close();
                }
                if (this.os != null) {
                    this.os.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                    this.SrPt.close();
                }
            }
        }
        finally {
            if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                this.SrPt.close();
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
                this.l.handleEvent(null);
            }
        }
    }

    public void setSerialPortParams(int baudRate, int dataBits, int stopBits, int parity) {
        try {
            this.SrPt.setSerialPortParams(baudRate, dataBits, stopBits, parity);
        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
    }
}

