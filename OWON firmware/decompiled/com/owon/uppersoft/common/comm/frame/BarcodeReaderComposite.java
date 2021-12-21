/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.MouseEvent
 *  org.eclipse.swt.events.MouseListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.events.TraverseEvent
 *  org.eclipse.swt.events.TraverseListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.frame.MainFrm;
import com.owon.uppersoft.common.comm.job.model.OSCDateItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.selfupgrade.Entity;
import com.owon.uppersoft.selfupgrade.USBCommunication;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class BarcodeReaderComposite {
    private Text txtPath;
    private Composite com;
    private Text txtVer;
    private Text txtSn;
    private Text txtType;
    private Composite composite;
    private USBCommunication usbCom;
    private String path;
    private Label lbVer;
    private Label lbType;
    private Label lbSn;
    Button[] button;
    static Boolean[] saveSW = new Boolean[12];
    Button lockButton;
    String[] SW = new String[]{"@1ChineseSW", "@1TChineseSW", "@1EnglishSW", "@1FrenchSW", "@1RussianSW", "@1SpanishSW", "@1GemanSW", "@1PolanSW", "@1PortugueseSW", "@1ItalianSW", "@1JapaneseSW", "@1KoreanSW"};
    String[] DispSW = new String[]{"@1ChineseDispSW", "@1TChineseDispSW", "@1EnglishDispSW", "@1FrenchDispSW", "@1RussianDispSW", "@1SpanishDispSW", "@1GemanDispSW", "@1PolanDispSW", "@1PortugueseDispSW", "@1ItalianDispSW", "@1JapaneseDispSW", "@1KoreanDispSW"};

    public Composite getComposite() {
        return this.com;
    }

    public BarcodeReaderComposite(Composite parent) {
        this.button = new Button[12];
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        String[] lag = new String[]{"LAG.Chn", "LAG.TChn", "LAG.En", "LAG.Fre", "LAG.Rus", "LAG.Span", "LAG.Gem", "LAG.Pol", "LAG.Por", "LAG.Ita", "LAG.Jap", "LAG.Kor"};
        Group buttonGroup = new Group(this.com, 0);
        GridData gd_butGroup = new GridData(4, 0x1000000, true, false);
        gd_butGroup.heightHint = 75;
        buttonGroup.setLayoutData((Object)gd_butGroup);
        GridLayout bt_gridLayout = new GridLayout();
        bt_gridLayout.marginWidth = 10;
        bt_gridLayout.marginHeight = 10;
        bt_gridLayout.numColumns = 6;
        buttonGroup.setLayout((Layout)bt_gridLayout);
        buttonGroup.setText(MsgCenter.getString("BAR.language"));
        int i = 0;
        while (i < this.button.length) {
            this.button[i] = new Button((Composite)buttonGroup, 32);
            this.button[i].setText(MsgCenter.getString(lag[i]));
            ++i;
        }
        this.lockButton = new Button((Composite)buttonGroup, 32);
        this.lockButton.setText(MsgCenter.getString("TC.Lock"));
        Group barcodeGroup = new Group(this.com, 0);
        GridData gd_barcodeGroup = new GridData(4, 0x1000000, true, false);
        gd_barcodeGroup.heightHint = 108;
        barcodeGroup.setLayoutData((Object)gd_barcodeGroup);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 10;
        gridLayout.marginHeight = 10;
        gridLayout.numColumns = 3;
        barcodeGroup.setLayout((Layout)gridLayout);
        barcodeGroup.setText(MsgCenter.getString("BAR.changetxt"));
        Label label = new Label((Composite)barcodeGroup, 0);
        label.setText(MsgCenter.getString("BAR.path"));
        this.txtPath = new Text((Composite)barcodeGroup, 2048);
        GridData gd_txtPath = new GridData(4, 0x1000000, true, false);
        this.txtPath.setLayoutData((Object)gd_txtPath);
        Button btnBrowse = new Button((Composite)barcodeGroup, 0);
        btnBrowse.setText(MsgCenter.getString("EC.browse"));
        this.composite = new Composite((Composite)barcodeGroup, 0);
        this.composite.setLayout((Layout)new GridLayout(6, false));
        GridData gd_composite = new GridData(4, 0x1000000, false, false, 3, 1);
        gd_composite.heightHint = 25;
        this.composite.setLayoutData((Object)gd_composite);
        this.lbVer = new Label(this.composite, 0);
        this.lbVer.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbVer.setText(MsgCenter.getString("TC.ver"));
        this.txtVer = new Text(this.composite, 2048);
        this.txtVer.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.lbSn = new Label(this.composite, 0);
        this.lbSn.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbSn.setText(MsgCenter.getString("TC.sn"));
        this.txtSn = new Text(this.composite, 2048);
        this.txtSn.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        btnBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(BarcodeReaderComposite.this.com.getShell());
                BarcodeReaderComposite.this.path = BarcodeReaderComposite.this.txtPath.getText();
                if (!BarcodeReaderComposite.this.path.equals("")) {
                    dd.setFilterPath(BarcodeReaderComposite.this.path);
                }
                BarcodeReaderComposite.this.path = dd.open();
                if (BarcodeReaderComposite.this.path == null) {
                    return;
                }
                BarcodeReaderComposite.this.txtPath.setText(BarcodeReaderComposite.this.path);
            }
        });
        this.txtSn.addMouseListener(new MouseListener(){

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
                BarcodeReaderComposite.this.txtSn.selectAll();
            }
        });
        this.txtSn.addTraverseListener(new TraverseListener(){

            public void keyTraversed(TraverseEvent e) {
                if (e.keyCode == 0x1000050 || e.keyCode == 13) {
                    BarcodeReaderComposite.this.barcodeReader();
                }
            }
        });
        this.lockButton.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                if (BarcodeReaderComposite.this.lockButton.getSelection()) {
                    int i = 0;
                    while (i < BarcodeReaderComposite.this.button.length) {
                        BarcodeReaderComposite.this.button[i].setEnabled(false);
                        ++i;
                    }
                } else {
                    int i = 0;
                    while (i < BarcodeReaderComposite.this.button.length) {
                        BarcodeReaderComposite.this.button[i].setEnabled(true);
                        ++i;
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    public void getValue() {
        int i = 0;
        while (i < this.button.length) {
            BarcodeReaderComposite.saveSW[i] = this.button[i].getSelection();
            ++i;
        }
    }

    public String getFilePath() {
        return this.txtPath.getText();
    }

    public Text getTxt() {
        return this.txtSn;
    }

    private boolean barcodeReader() {
        this.usbCom = new USBCommunication();
        final MainFrm mf = new MainFrm();
        if (this.usbCom.doGetTxt()) {
            if (!this.txtVer.getText().startsWith("V")) {
                return false;
            }
            final Entity entity = this.usbCom.getEntity();
            entity.init();
            this.getValue();
            entity.udateSer(this.txtSn.getText());
            int i = 0;
            while (i < this.SW.length) {
                entity.udatelanguageSw(this.SW[i], this.DispSW[i], saveSW[i]);
                ++i;
            }
            this.txtSn.selectAll();
            entity.updateVer(this.txtVer.getText());
            mf.getbr().oneKeyDownload(new Runnable(){

                @Override
                public void run() {
                    OSCDateItem di = new OSCDateItem("tx", 2, entity, 16384);
                    BarcodeReaderComposite.this.usbCom.setDataItem(di);
                    boolean b = BarcodeReaderComposite.this.usbCom.getData();
                    if (b) {
                        di.setStatus(1);
                    } else {
                        di.setStatus(2);
                    }
                    mf.getbr().txtreboot();
                    try {
                        Thread.sleep(20000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mf.getbr().barcode(BarcodeReaderComposite.this.path, ".txt");
                }
            });
        }
        return false;
    }
}

