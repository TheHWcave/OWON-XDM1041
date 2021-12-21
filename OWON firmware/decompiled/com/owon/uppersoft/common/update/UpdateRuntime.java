/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.update.CheckUpdateFrame;
import com.owon.uppersoft.common.update.DefaultUpdatable;
import com.owon.uppersoft.common.update.IUpdatable;
import com.owon.uppersoft.common.utils.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class UpdateRuntime {
    private IUpdatable iu;

    public static void main(String[] args) {
        DefaultUpdatable iu = new DefaultUpdatable();
        UpdateRuntime ur = new UpdateRuntime(iu);
        ur.checkUpdate();
    }

    public UpdateRuntime(IUpdatable iu) {
        this.iu = iu;
    }

    public void clearUp() {
        File clearupXML = new File(this.iu.getRelativePath());
        if (!clearupXML.exists()) {
            return;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new FileInputStream(clearupXML));
            is.setEncoding("UTF-8");
            Document doc = builder.parse(is);
            NodeList files = doc.getElementsByTagName("delFile");
            int l = files.getLength();
            int i = 0;
            while (i < l) {
                Element file = (Element)files.item(i);
                String s = file.getTextContent().trim();
                FileUtil.deleteFile(s);
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        clearupXML.delete();
    }

    public void checkUpdate() {
        new CheckUpdateFrame(this.iu);
    }
}

