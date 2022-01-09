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
 */
package com.owon.uppersoft.common.commjob.instance;

import com.owon.uppersoft.common.commjob.instance.ICommunication;
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

public class SerialCommunication
implements ICommunication,
SerialPortEventListener {
    public static final int OpenPortTimeout = 2000;
    private InputStream is;
    private OutputStream os;
    private SerialPort SrPt;
    private CommPortIdentifier portId;
    boolean dataAvai = false;

    public boolean openPort(String port) {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier((String)port);
            this.SrPt = (SerialPort)this.portId.open(port, 2000);
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read(byte[] arr, int beg, int len) {
        try {
            int i = 0;
            do {
                if (this.dataAvai) {
                    this.dataAvai = false;
                    return this.is.read(arr, beg, len);
                }
                this.slp(50);
            } while (i++ <= 200);
            return -1;
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int write(byte[] arr, int beg, int len) {
        try {
            this.os.write(arr, beg, len);
            this.os.flush();
            return len - beg;
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void slp(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent event) {
        System.out.println(event.getEventType());
        switch (event.getEventType()) {
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: {
                break;
            }
            case 1: {
                if (this.dataAvai) {
                    return;
                }
                this.slp(100);
                this.dataAvai = true;
            }
        }
    }

    @Override
    public boolean open(Object[] para) {
        String port = (String)para[0];
        int baudRate = Integer.parseInt((String)para[1]);
        if (this.openPort(port)) {
            this.loadPort(baudRate, 8, 1, 0);
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        if (this.SrPt != null) {
            this.SrPt.close();
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

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
}

