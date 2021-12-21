/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.USBCommunication;
import com.owon.uppersoft.common.comm.job.SimpleRequestThread;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.comm.job.model.FileDataFileInputProvider;
import java.io.File;

public class SendFile
implements DLListener {
    private DLListener dl;
    private DataItem df;

    public SendFile(DLListener dl) {
        this.dl = dl;
    }

    public boolean sendFile(DataItem df) {
        this.df = df;
        USBCommunication usbCom = new USBCommunication(this);
        usbCom.setDataItem(df);
        boolean b = usbCom.getData();
        return b;
    }

    public void reboot() {
        CommonCommand cc = new CommonCommand(this);
        new SimpleRequestThread(cc, 20).start();
    }

    public void eraseFLASH() {
        CommonCommand cc = new CommonCommand(this);
        new SimpleRequestThread(cc, 25).start();
    }

    public void eraseBOOT() {
        CommonCommand cc = new CommonCommand(this);
        new SimpleRequestThread(cc, 24).run();
    }

    public void eraseTABLE() {
        CommonCommand cc = new CommonCommand(this);
        new SimpleRequestThread(cc, 27).run();
    }

    public void eraseBACKUP() {
        CommonCommand cc = new CommonCommand(this);
        new SimpleRequestThread(cc, 28).run();
    }

    @Override
    public void handleEvent(DLEvent e) {
        if (e.type == 1 && this.df != null) {
            e.data = this.df.getType();
        }
        this.dl.handleEvent(e);
    }

    public static void main(String[] args) {
        Thread t = new Thread(){

            @Override
            public void run() {
                SendFile sf = new SendFile(new DL());
                String fp = "fp";
                sf.sendFile(new FileDataFile(fp, 1, new FileDataFileInputProvider(new File(fp)), 16384));
            }
        };
        t.start();
    }

    static class DL
    implements DLListener {
        @Override
        public void handleEvent(DLEvent e) {
            int value = e.intValue;
            switch (e.type) {
                case 1: {
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    System.out.println(String.valueOf(value) + "kB/s");
                    break;
                }
                case 5: {
                    System.out.print(e.data + " ");
                    break;
                }
                case 6: {
                    byte[] commands = (byte[])e.data;
                    int length = commands.length;
                    int i = 0;
                    while (i < length) {
                        System.out.print(String.valueOf(Integer.toHexString(commands[i] & 0xFF)) + " ");
                        ++i;
                    }
                    System.out.println();
                }
            }
        }
    }
}

