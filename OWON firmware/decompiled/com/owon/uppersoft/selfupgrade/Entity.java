/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public abstract class Entity
implements InputProvider {
    protected String sn;
    protected String ver;
    protected byte[] content;

    public Entity(ByteBuffer bb) {
        this.content = bb.array();
    }

    public Entity(byte[] content) {
        this.content = content;
    }

    public abstract void init();

    public abstract void updateVer(String var1);

    protected abstract boolean supportOSReboot();

    public String getSn() {
        return this.sn.replace(",", "");
    }

    public void setSn(String sn) {
        this.sn = sn;
        System.out.println(sn);
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDownloadfile() {
        if (this.sn == null) {
            return "errnofound";
        }
        String os = "errnoupdate";
        File clearupXML = new File("config.xml");
        if (!clearupXML.exists()) {
            return "errnoXML";
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new FileInputStream(clearupXML));
            is.setEncoding("UTF-8");
            Document doc = builder.parse(is);
            NodeList files = doc.getElementsByTagName("series");
            int l = files.getLength();
            String seris = this.sn.replaceAll("/", "");
            int i = 0;
            while (i < l) {
                Element file = (Element)files.item(i);
                String s = file.getAttribute("type");
                System.out.println(s);
                if (seris.startsWith(s)) {
                    String oldVer = file.getAttribute("oldVer").trim();
                    System.out.println(oldVer);
                    System.out.println(this.ver);
                    if (this.ver.equalsIgnoreCase(oldVer)) {
                        os = String.valueOf(s) + "\\" + file.getAttribute("osName");
                        this.ver = file.getAttribute("newVer");
                        this.updateVer(this.ver);
                    }
                }
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return os;
    }

    @Override
    public void closeInput() {
    }

    @Override
    public long getFileLength() {
        return this.content.length;
    }

    @Override
    public InputStream openInput() {
        return new ByteArrayInputStream(this.content);
    }

    public void udateSer(String string) {
    }

    public void udatelanguageSw(String string, String string2, Boolean boolean1) {
    }
}

