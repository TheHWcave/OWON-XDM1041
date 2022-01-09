/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataPacket {
    private short type = (short)2;
    private byte bcc;
    private byte dummy = 0;
    private int size;
    private byte[] data = new byte[50];

    public DataPacket(FileInputStream fis) {
        try {
            this.size = fis.read(this.data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.bcc();
    }

    private void bcc() {
        this.bcc = this.data[0];
        int i = 1;
        while (i < this.size) {
            this.bcc = (byte)(this.bcc ^ this.data[i]);
            ++i;
        }
    }

    public byte[] getPacket() {
        ByteBuffer bb = ByteBuffer.allocate(8 + this.size);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(this.type);
        bb.put(this.bcc);
        bb.put(this.dummy);
        bb.putInt(this.size);
        bb.put(this.data, 0, this.size);
        return bb.array();
    }

    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("f:/u-boot.bin");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] b = new DataPacket(fis).getPacket();
        int i = 0;
        while (i < b.length) {
            System.out.print(String.valueOf(b[i]) + " ");
            ++i;
        }
    }
}

