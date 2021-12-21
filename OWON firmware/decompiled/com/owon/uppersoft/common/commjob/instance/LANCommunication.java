/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.commjob.instance;

import com.owon.uppersoft.common.commjob.instance.ICommunication;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LANCommunication
implements ICommunication {
    private Socket sck;

    private boolean connect(String add, int port) {
        try {
            this.sck = new Socket(add, port);
            this.sck.setSoTimeout(5000);
            return this.sck.isConnected();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int read(byte[] arr, int beg, int len) {
        InputStream is = null;
        if (this.sck == null || this.sck.isClosed() || !this.sck.isConnected()) {
            return -1;
        }
        try {
            is = this.sck.getInputStream();
            return is.read(arr, beg, len);
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int write(byte[] arr, int beg, int len) {
        block3: {
            try {
                if (this.sck != null && !this.sck.isClosed() && this.sck.isConnected()) break block3;
                return -1;
            }
            catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        OutputStream os = this.sck.getOutputStream();
        os.write(arr, beg, len);
        os.flush();
        return len;
    }

    @Override
    public boolean open(Object[] para) {
        String host = (String)para[0];
        int port = Integer.parseInt((String)para[1]);
        return this.connect(host, port);
    }

    @Override
    public void close() {
        try {
            if (this.sck != null) {
                this.sck.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return this.sck.isConnected();
    }
}

