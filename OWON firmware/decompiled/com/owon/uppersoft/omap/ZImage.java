/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.patch.direct.util.CRC;
import java.nio.ByteBuffer;

public class ZImage {
    private final int ih_magic = 654645590;
    private int ih_hcrc;
    private int ih_time;
    private int ih_size;
    private final int ih_load = -2139095040;
    private final int ih_ep = -2139095040;
    private int ih_dcrc;
    private final byte ih_os = (byte)5;
    private final byte ih_arch = (byte)2;
    private final byte ih_type = (byte)6;
    private final byte ih_comp = 0;
    private String ih_name = "autoscr script";
    private byte[] body;

    public ZImage(byte[] body) {
        this.body = body;
        this.ih_size = body.length;
        this.ih_dcrc = (int)CRC.doCRC32(body);
    }

    public byte[] getHeader() {
        ByteBuffer bb = ByteBuffer.allocate(64);
        bb.putInt(654645590);
        bb.putInt(0);
        this.ih_time = (int)(System.currentTimeMillis() / 1000L);
        bb.putInt(this.ih_time);
        bb.putInt(this.ih_size);
        bb.putInt(-2139095040);
        bb.putInt(-2139095040);
        bb.putInt(this.ih_dcrc);
        bb.put((byte)5);
        bb.put((byte)2);
        bb.put((byte)6);
        bb.put((byte)0);
        bb.put(this.ih_name.getBytes());
        this.ih_hcrc = (int)CRC.doCRC32(bb.array());
        bb.position(4);
        bb.putInt(this.ih_hcrc);
        return bb.array();
    }

    public ByteBuffer getImage() {
        ByteBuffer bb = ByteBuffer.allocate(64 + this.body.length);
        bb.put(this.getHeader());
        bb.put(this.body);
        return bb;
    }
}

