/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPack;
import com.owon.uppersoft.tftp.TFTPdata;
import com.owon.uppersoft.tftp.TFTPerror;
import com.owon.uppersoft.tftp.TFTPpacket;
import com.owon.uppersoft.tftp.TFTPread;
import com.owon.uppersoft.tftp.TftpException;
import com.owon.uppersoft.tftp.UseException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class tftpClient {
    public static boolean getFile(String host, String fileName, String out) {
        try {
            InetAddress server = InetAddress.getByName(host);
            DatagramSocket sock = new DatagramSocket();
            System.out.print(host);
            FileOutputStream outFile = new FileOutputStream(out);
            TFTPread reqPak = new TFTPread(fileName);
            reqPak.send(server, sock);
            int bytesOut = 512;
            while (bytesOut == 512) {
                TFTPpacket p;
                TFTPpacket inPak = TFTPpacket.receive(sock);
                if (inPak instanceof TFTPerror) {
                    p = (TFTPerror)inPak;
                    throw new TftpException(((TFTPerror)p).message());
                }
                if (inPak instanceof TFTPdata) {
                    p = (TFTPdata)inPak;
                    int blockNum = ((TFTPdata)p).blockNumber();
                    bytesOut = ((TFTPdata)p).write(outFile);
                    TFTPack ack = new TFTPack(blockNum);
                    ack.send(p.getAddress(), p.getPort(), sock);
                    continue;
                }
                throw new TftpException("Unexpected response from server");
            }
            outFile.close();
            sock.close();
            return true;
        }
        catch (UnknownHostException unknownHostException) {
            System.out.println("Unknown host " + host);
            return false;
        }
        catch (IOException iOException) {
            System.out.println("IO error, transfer aborted");
            return false;
        }
        catch (TftpException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] argv) throws TftpException, UseException {
        tftpClient.getFile("127.0.0.1", "TDS7102/u-boot.bin", "boot.bin");
    }
}

