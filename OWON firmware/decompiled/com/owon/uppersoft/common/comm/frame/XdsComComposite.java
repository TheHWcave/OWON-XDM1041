/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.commjob.instance.SerialCommunication;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import java.util.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class XdsComComposite {
    private Composite com;
    private Text text;
    private Text text_1;
    private Combo combo;

    public Composite getComposite() {
        return this.com;
    }

    public XdsComComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        Group encGroup = new Group(this.com, 0);
        GridData gd_encGroup = new GridData(4, 0x1000000, true, false);
        gd_encGroup.heightHint = 108;
        encGroup.setLayoutData((Object)gd_encGroup);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 10;
        gridLayout.marginHeight = 10;
        gridLayout.numColumns = 2;
        encGroup.setLayout((Layout)gridLayout);
        this.combo = new Combo((Composite)encGroup, 8);
        this.combo.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        Button btnBrowse = new Button((Composite)encGroup, 0);
        btnBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                String s = XdsComComposite.this.text.getText();
                if (s.trim().equalsIgnoreCase("")) {
                    return;
                }
                String port = XdsComComposite.this.combo.getText();
                SerialCommunication com = new SerialCommunication();
                com.open(new String[]{port, "115200"});
                byte[] b = s.getBytes();
                com.write(b, 0, b.length);
                b = new byte[1024];
                try {
                    Thread.sleep(500L);
                }
                catch (Exception exception) {}
                int rn = com.read(b, 0, b.length);
                if (rn > 0) {
                    XdsComComposite.this.text_1.setText(new String(b, 0, rn));
                }
                com.close();
            }
        });
        btnBrowse.setText(MsgCenter.getString("XDSCOM.start"));
        Label lbSend = new Label((Composite)encGroup, 0);
        lbSend.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbSend.setText(MsgCenter.getString("XDSCOM.send"));
        this.text = new Text((Composite)encGroup, 2048);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.text.setText(":SET?");
        Label lbRecv = new Label((Composite)encGroup, 0);
        lbRecv.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbRecv.setText(MsgCenter.getString("XDSCOM.recv"));
        this.text_1 = new Text((Composite)encGroup, 2048);
        this.text_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.getComName();
    }

    private void getComName() {
        List<String> list = SerialPortUtil.loadAvailablePort();
        String[] coms = new String[list.size()];
        this.combo.setItems(list.toArray(coms));
        this.combo.select(0);
    }
}

