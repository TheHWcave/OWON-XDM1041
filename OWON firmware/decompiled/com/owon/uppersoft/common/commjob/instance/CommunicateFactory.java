/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.commjob.instance;

import com.owon.uppersoft.common.commjob.instance.ICommunication;
import com.owon.uppersoft.common.commjob.instance.LANCommunication;
import com.owon.uppersoft.common.commjob.instance.SerialCommunication;
import com.owon.uppersoft.common.commjob.instance.USBCommunication;

public class CommunicateFactory {
    public static ICommunication getComm(String type) {
        if (type.equalsIgnoreCase("USB")) {
            return new USBCommunication();
        }
        if (type.equalsIgnoreCase("LAN")) {
            return new LANCommunication();
        }
        return new SerialCommunication();
    }
}

