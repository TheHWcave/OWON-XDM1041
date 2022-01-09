/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bridj.Pointer
 *  org.bridj.Pointer$StringType
 *  visa32.Visa32Library
 */
package com.owon.uppersoft.common.comm.frame.vds;

import java.util.ArrayList;
import java.util.List;
import org.bridj.Pointer;
import visa32.Visa32Library;

public class VisaCommunication {
    private long defaultRM;
    private long vipSession;

    public String readString() {
        byte[] b = new byte[1024];
        int i = this.read(b, 0, 1024);
        if (i < 0) {
            return "";
        }
        return new String(b, 0, i);
    }

    public int read(byte[] arr, int beg, int len) {
        Pointer pReadBuffer = Pointer.NULL;
        Pointer pReturnCount = Pointer.NULL;
        try {
            pReadBuffer = Pointer.allocateBytes((long)len);
            pReturnCount = Pointer.allocateLong();
            long visaStatus = Visa32Library.viRead((long)this.vipSession, (Pointer)pReadBuffer, (long)len, (Pointer)pReturnCount);
            if (visaStatus == 0L) {
                long returnCount = (Long)pReturnCount.get();
                if (returnCount == 0L) {
                    System.err.println("Error: Did not receive a response.");
                } else {
                    pReadBuffer.getBytes(arr);
                    int n = (int)returnCount;
                    return n;
                }
            }
            try {
                System.out.println("err");
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        finally {
            if (pReadBuffer != Pointer.NULL) {
                pReadBuffer.release();
            }
            if (pReturnCount != Pointer.NULL) {
                pReturnCount.release();
            }
        }
        return -1;
    }

    public int writeString(String s) {
        byte[] b = s.getBytes();
        return this.write(b, 0, b.length);
    }

    public int write(byte[] arr, int beg, int len) {
        String command = new String(arr, beg, len);
        Pointer pBuffer = Pointer.NULL;
        Pointer pReturnCount = Pointer.allocateLong();
        long returnCount = 0L;
        try {
            pBuffer = Pointer.allocateBytes((long)len);
            pBuffer.setBytes(arr);
            long visaStatus = Visa32Library.viWrite((long)this.vipSession, (Pointer)pBuffer, (long)len, (Pointer)pReturnCount);
            if (visaStatus != 0L) {
                System.err.println(String.format("Error: Could not write %s.", command));
                returnCount = -1L;
            } else {
                returnCount = (Long)pReturnCount.get();
                if (returnCount != (long)len) {
                    System.err.println(String.format("Error: Could only write %d instead of %d bytes.", returnCount, len));
                    returnCount = -1L;
                }
            }
            int n = (int)returnCount;
            return n;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pBuffer != Pointer.NULL) {
                pBuffer.release();
            }
            if (pReturnCount != Pointer.NULL) {
                pReturnCount.release();
            }
        }
        return -1;
    }

    public boolean open(String s) {
        Pointer instr;
        block3: {
            try {
                Visa32Library.viClose((long)this.vipSession);
                instr = Pointer.allocateLong();
                Pointer pReadBuffer = Pointer.allocateBytes((long)1024L);
                pReadBuffer.setString(s, Pointer.StringType.C);
                long status = Visa32Library.viOpen((long)this.defaultRM, (Pointer)pReadBuffer, (long)0L, (long)0L, (Pointer)instr);
                if (status >= 0L) break block3;
                System.out.println("status:" + status);
                return false;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        this.vipSession = instr.getCLong();
        Visa32Library.viSetAttribute((long)this.vipSession, (long)1073676314L, (long)2000L);
        return true;
    }

    public void close() {
        long visaStatus = Visa32Library.viClose((long)this.vipSession);
        visaStatus = Visa32Library.viClose((long)this.defaultRM);
        System.out.println("close:" + visaStatus);
    }

    public List<String> findSrc() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Pointer pViSession = Pointer.allocateLong();
            long status = Visa32Library.viOpenDefaultRM((Pointer)pViSession);
            if (status < 0L) {
                System.out.println("Failed to initialise NI-VISA system.\n");
            }
            this.defaultRM = pViSession.getCLong();
            Pointer pBuffer = Pointer.NULL;
            String s = "USB?*";
            pBuffer = Pointer.pointerToCString((String)s);
            pBuffer.setString(s, Pointer.StringType.C);
            Pointer pReadBuffer = Pointer.NULL;
            pReadBuffer = Pointer.allocateBytes((long)1024L);
            Pointer vi = Pointer.allocateLong();
            Pointer retCnt = Pointer.allocateLong();
            status = Visa32Library.viFindRsrc((long)this.defaultRM, (Pointer)pBuffer, (Pointer)vi, (Pointer)retCnt, (Pointer)pReadBuffer);
            long i = retCnt.getLong();
            while (i > 0L) {
                list.add(pReadBuffer.getString(Pointer.StringType.C));
                Visa32Library.viFindNext((long)vi.getCLong(), (Pointer)pReadBuffer);
                --i;
            }
        }
        catch (Exception exception) {}
        return list;
    }

    public static void main(String[] args) {
        VisaCommunication visa = new VisaCommunication();
        List<String> list = visa.findSrc();
        for (String s : list) {
            System.out.println(s);
            if (s.equalsIgnoreCase("")) continue;
            visa.open(s);
            byte[] arr = "*IDN?\n".getBytes();
            visa.write(arr, 0, arr.length);
            byte[] rearr = new byte[512];
            int rn = visa.read(rearr, 0, rearr.length);
            System.out.println(rn);
            if (rn <= 0) continue;
            System.out.println(new String(rearr, 0, rn));
        }
        visa.close();
    }
}

