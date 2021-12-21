/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPack;
import com.owon.uppersoft.tftp.TFTPdata;
import com.owon.uppersoft.tftp.TFTPerror;
import com.owon.uppersoft.tftp.TFTPread;
import com.owon.uppersoft.tftp.TFTPwrite;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TFTPpacket {
    public static int tftpPort = 69;
    public static int maxTftpPakLen = 516;
    public static int maxTftpData = 512;
    protected static final short tftpRRQ = 1;
    protected static final short tftpWRQ = 2;
    protected static final short tftpDATA = 3;
    protected static final short tftpACK = 4;
    protected static final short tftpERROR = 5;
    protected static final int opOffset = 0;
    protected static final int fileOffset = 2;
    protected static final int blkOffset = 2;
    protected static final int dataOffset = 4;
    protected static final int numOffset = 2;
    protected static final int msgOffset = 4;
    protected int length;
    protected byte[] message = new byte[maxTftpPakLen];
    protected InetAddress host;
    protected int port;

    public TFTPpacket() {
        this.length = maxTftpPakLen;
    }

    public static TFTPpacket receive(DatagramSocket sock) throws IOException {
        TFTPpacket in = new TFTPpacket();
        TFTPpacket retPak = null;
        DatagramPacket inPak = new DatagramPacket(in.message, in.length);
        sock.receive(inPak);
        switch (in.get(0)) {
            case 1: {
                retPak = new TFTPread();
                break;
            }
            case 2: {
                retPak = new TFTPwrite();
                break;
            }
            case 3: {
                retPak = new TFTPdata();
                break;
            }
            case 4: {
                retPak = new TFTPack();
                break;
            }
            case 5: {
                retPak = new TFTPerror();
            }
        }
        retPak.message = in.message;
        retPak.length = inPak.getLength();
        retPak.host = inPak.getAddress();
        retPak.port = inPak.getPort();
        return retPak;
    }

    public void send(InetAddress ip, DatagramSocket sock) throws IOException {
        sock.send(new DatagramPacket(this.message, this.length, ip, tftpPort));
    }

    public void send(InetAddress ip, int p, DatagramSocket s) throws IOException {
        s.send(new DatagramPacket(this.message, this.length, ip, p));
    }

    public InetAddress getAddress() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getLength() {
        return this.length;
    }

    protected void put(int at, short value) {
        this.message[at++] = (byte)(value >>> 8);
        this.message[at] = (byte)(value % 256);
    }

    protected void put(int at, String value, byte del) {
        value.getBytes(0, value.length(), this.message, at);
        this.message[at + value.length()] = del;
    }

    protected int get(int at) {
        return (this.message[at] & 0xFF) << 8 | this.message[at + 1] & 0xFF;
    }

    protected String get(int at, byte del) {
        StringBuffer result = new StringBuffer();
        while (this.message[at] != del) {
            result.append((char)this.message[at++]);
        }
        return result.toString();
    }
}

