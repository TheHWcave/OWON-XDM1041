/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MsgPacket {
    private short pkt_type;
    private short msg_type;
    private int[] msg_params;

    public MsgPacket() {
        this.pkt_type = 0;
        this.msg_type = 0;
        this.msg_params = new int[2];
    }

    public MsgPacket(ByteBuffer bb) {
        bb.order(ByteOrder.LITTLE_ENDIAN);
        this.pkt_type = bb.getShort();
        this.msg_type = bb.getShort();
        this.msg_params = new int[2];
        this.msg_params[0] = bb.getInt();
        this.msg_params[1] = bb.getInt();
    }

    public short getPkt_type() {
        return this.pkt_type;
    }

    public void setPkt_type(short pkt_type) {
        this.pkt_type = pkt_type;
    }

    public short getMsg_type() {
        return this.msg_type;
    }

    public void setMsg_type(short msg_type) {
        this.msg_type = msg_type;
    }

    public int[] getMsg_params() {
        return this.msg_params;
    }

    public void setMsg_params(int[] msg_params) {
        this.msg_params = msg_params;
    }

    public byte[] getPacket() {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(this.msg_type);
        bb.putShort(this.pkt_type);
        bb.putInt(this.msg_params[0]);
        bb.putInt(this.msg_params[1]);
        return bb.array();
    }
}

