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
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.MessageBox
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.EndianUtil;
import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.comm.job.CompareParm;
import com.owon.uppersoft.common.comm.job.GenerateExcel;
import com.owon.uppersoft.common.comm.job.GenerateOrder;
import com.owon.uppersoft.common.comm.job.ModifyParam;
import com.owon.uppersoft.common.comm.job.OrderContentBean;
import com.owon.uppersoft.common.comm.job.ProgressData;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.util.ReadJsonFile;
import com.owon.uppersoft.selfupgrade.HDSEntity;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class ChangeParamComposite
implements IShowMessage {
    private Button[] switchButtons;
    private Button[] languageButtons;
    private Composite com;
    private String txtPathname;
    private String excelPathname;
    private String outputPathname;
    private Text txtPathText;
    private Text orderPathText;
    private Text excelPathText;
    private Text txtVersionText;
    private Text verionText;
    private Text SnText;
    private Text oemLogoText;
    private Text idText;
    private Text logText;
    private Combo signCombo;
    private Button txtVerButton;
    private Label numLabel;
    private ModifyParam param;
    private HDSEntity hdsEntity = null;
    private CompareParm cp;
    private ProgressData progressData;
    private OrderContentBean bean;
    private boolean isCheckID = false;
    private boolean getbyte;
    private byte[] data;
    private boolean isDepart;
    private String[] lag = new String[]{"LAG.Chn", "LAG.TChn", "LAG.En", "LAG.Rus", "LAG.Gem", "LAG.Span", "LAG.Pol", "LAG.Fre", "LAG.Por", "LAG.Ita", "LAG.Jap", "LAG.Kor", "LAG.Dut", "LAG.null", "LAG.null", "LAG.null", "LAG.null", "LAG.null", "LAG.peak", "LAG.promax", "LAG.fre1", "LAG.akun", "LAG.plo1", "LAG.ablab", "LAG.eaa", "LAG.ack", "LAG.acken"};
    private String[] sw = new String[]{"XDS.logo", "XDS.tsc", "XDS.afgsingle", "XDS.afgdouble", "XDS.afg25m", "XDS.afg50m", "XDS.dmm", "XDS.vga", "XDS.hdlcd", "XDS.wifi", "XDS.can", "XDS.i2c", "XDS.lab", "XDS.ir", "XDS.avoutput", "XDS.depth", "XDS.rs232", "XDS.spi", "XDS.zoom", "XDS.voltadjust", "XDS.la", "XDS.probe", "XDS.lin"};
    private List<GenerateOrder> list;
    private List<String> func;
    private Map<Integer, int[]> mapLimit;

    public ChangeParamComposite(Composite parent, Text logText) {
        this.logText = logText;
        this.init();
        this.initTable(parent);
    }

    public Composite getComposite() {
        return this.com;
    }

    public void init() {
        this.cp = new CompareParm();
        this.param = new ModifyParam();
        this.mapLimit = new HashMap<Integer, int[]>();
        this.mapLimit.put(2, new int[]{25, 26});
        this.mapLimit.put(4, new int[]{18});
        this.mapLimit.put(18, new int[]{4});
        this.mapLimit.put(5, new int[]{19});
        this.mapLimit.put(19, new int[]{5});
        this.mapLimit.put(7, new int[]{20});
        this.mapLimit.put(20, new int[]{7});
        this.mapLimit.put(3, new int[]{21, 25, 26});
        this.mapLimit.put(21, new int[]{3, 25, 26});
        this.mapLimit.put(6, new int[]{22});
        this.mapLimit.put(22, new int[]{6});
        this.mapLimit.put(11, new int[]{24});
        this.mapLimit.put(24, new int[]{11});
        this.mapLimit.put(25, new int[]{2, 3, 21});
    }

    /*
     * Unable to fully structure code
     */
    public void initTable(Composite parent) {
        block6: {
            this.com = new Composite(parent, 0);
            this.com.setLayout((Layout)new GridLayout());
            switchGroup = this.getGroup(this.com, 70, 11, MsgCenter.getString("TC.functionsw"));
            try {
                config = ReadJsonFile.getConfig(this).getJSONObject("XDS");
                ja = config.getJSONArray("function");
                this.switchButtons = new Button[ja.size()];
                i = 0;
                while (i < ja.size()) {
                    this.switchButtons[i] = new Button((Composite)switchGroup, 32);
                    this.switchButtons[i].setText(ja.getString(i));
                    ++i;
                }
                break block6;
            }
            catch (Exception v0) {
                this.switchButtons = new Button[this.sw.length];
                i = 0;
                ** while (i < this.switchButtons.length)
            }
lbl-1000:
            // 1 sources

            {
                this.switchButtons[i] = new Button((Composite)switchGroup, 32);
                this.switchButtons[i].setText(MsgCenter.getString(this.sw[i]));
                ++i;
                continue;
            }
        }
        if (this.switchButtons.length > 15) {
            this.switchButtons[15].setEnabled(false);
            this.switchButtons[15].setSelection(true);
        }
        languageGroup = this.getGroup(this.com, 70, 9, MsgCenter.getString("TC.languagesw"));
        this.languageButtons = new Button[this.lag.length];
        i = 0;
        while (i < this.languageButtons.length) {
            this.languageButtons[i] = new Button((Composite)languageGroup, 32);
            this.languageButtons[i].setData((Object)i);
            this.languageButtons[i].setText(String.valueOf(i) + MsgCenter.getString(this.lag[i]));
            this.languageButtons[i].addSelectionListener((SelectionListener)new SelectionAdapter(){

                public void widgetSelected(SelectionEvent e) {
                    ChangeParamComposite.this.onlyOne((Integer)e.widget.getData());
                }
            });
            ++i;
        }
        this.languageButtons[13].setEnabled(false);
        this.languageButtons[14].setEnabled(false);
        this.languageButtons[15].setEnabled(false);
        this.languageButtons[16].setEnabled(false);
        this.languageButtons[17].setEnabled(false);
        this.languageButtons[26].setEnabled(false);
        browseGroup = this.getGroup(this.com, 30, 9, MsgCenter.getString("TC.others"));
        label = new Label((Composite)browseGroup, 0);
        label.setText(MsgCenter.getString("XDS.txtpath"));
        this.txtPathText = new Text((Composite)browseGroup, 2048);
        this.txtPathText.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        txtBrowsebtn = new Button((Composite)browseGroup, 0);
        txtBrowsebtn.setText(MsgCenter.getString("TC.browse"));
        orderLabel = new Label((Composite)browseGroup, 0);
        orderLabel.setText(MsgCenter.getString("XDS.xlspath"));
        this.orderPathText = new Text((Composite)browseGroup, 2048);
        this.orderPathText.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        orderBrowsebtn = new Button((Composite)browseGroup, 0);
        orderBrowsebtn.setText(MsgCenter.getString("TC.browse"));
        xlsLabel = new Label((Composite)browseGroup, 0);
        xlsLabel.setText(MsgCenter.getString("XDS.outputpath"));
        this.excelPathText = new Text((Composite)browseGroup, 2048);
        this.excelPathText.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        xlsBrowsebtn = new Button((Composite)browseGroup, 0);
        xlsBrowsebtn.setText(MsgCenter.getString("TC.browse"));
        otherGroup = this.getGroup(this.com, 85, 7, "");
        this.txtVerButton = new Button((Composite)otherGroup, 32);
        this.txtVerButton.setText(MsgCenter.getString("TC.vermessage"));
        this.txtVersionText = new Text((Composite)otherGroup, 2048);
        this.txtVersionText.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.txtVersionText.setEnabled(false);
        lbVer = new Label((Composite)otherGroup, 0);
        lbVer.setText(MsgCenter.getString("TC.ver"));
        this.verionText = new Text((Composite)otherGroup, 2048);
        this.verionText.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        lbSign = new Label((Composite)otherGroup, 0);
        lbSign.setText(MsgCenter.getString("XDS.sign"));
        this.signCombo = new Combo((Composite)otherGroup, 8);
        this.signCombo.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        developButton = new Button((Composite)otherGroup, 32);
        developButton.setText(MsgCenter.getString("XDS.department"));
        lbID = new Label((Composite)otherGroup, 0);
        lbID.setText(MsgCenter.getString("TC.id"));
        this.idText = new Text((Composite)otherGroup, 2048);
        this.idText.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        lbSn = new Label((Composite)otherGroup, 0);
        lbSn.setText(MsgCenter.getString("TC.sn"));
        this.SnText = new Text((Composite)otherGroup, 2048);
        this.SnText.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        lbLogo = new Label((Composite)otherGroup, 0);
        lbLogo.setText(MsgCenter.getString("TC.oemlogo"));
        this.oemLogoText = new Text((Composite)otherGroup, 2048);
        this.oemLogoText.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        lbSum = new Label((Composite)otherGroup, 0);
        lbSum.setText(String.valueOf(MsgCenter.getString("XDS.sum")) + "  " + 0 + "   ");
        this.numLabel = new Label((Composite)otherGroup, 0);
        this.numLabel.setText(String.valueOf(MsgCenter.getString("XDS.num")) + "  " + 0 + "   ");
        this.txtVerButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (ChangeParamComposite.this.txtVerButton.getSelection()) {
                    ChangeParamComposite.this.txtVersionText.setEnabled(true);
                } else {
                    ChangeParamComposite.this.txtVersionText.setEnabled(false);
                }
            }
        });
        developButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (developButton.getSelection()) {
                    xlsBrowsebtn.setEnabled(false);
                    ChangeParamComposite.this.orderPathText.setEnabled(false);
                    ChangeParamComposite.this.signCombo.setEnabled(false);
                    ChangeParamComposite.this.idText.setEnabled(false);
                    ChangeParamComposite.this.isDepart = true;
                } else {
                    xlsBrowsebtn.setEnabled(true);
                    ChangeParamComposite.this.orderPathText.setEnabled(true);
                    ChangeParamComposite.this.signCombo.setEnabled(true);
                    ChangeParamComposite.this.idText.setEnabled(true);
                    ChangeParamComposite.this.isDepart = false;
                }
            }
        });
        txtBrowsebtn.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(ChangeParamComposite.this.com.getShell());
                ChangeParamComposite.this.txtPathname = ChangeParamComposite.this.txtPathText.getText();
                if (!ChangeParamComposite.this.txtPathname.equals("")) {
                    fileDialog.setFilterPath(ChangeParamComposite.this.txtPathname);
                }
                ChangeParamComposite.this.txtPathname = fileDialog.open();
                if (ChangeParamComposite.this.txtPathname == null) {
                    return;
                }
                ChangeParamComposite.this.txtPathText.setText(ChangeParamComposite.this.txtPathname);
                ChangeParamComposite.this.data = ChangeParamComposite.this.param.readPathContent(ChangeParamComposite.this.txtPathname);
                ChangeParamComposite.this.hdsEntity = new HDSEntity(ChangeParamComposite.this.data);
                ChangeParamComposite.this.getbyte = true;
                ChangeParamComposite.this.setContent(ChangeParamComposite.this.data);
            }
        });
        orderBrowsebtn.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (ChangeParamComposite.this.isDepart) {
                    return;
                }
                FileDialog fileDialog = new FileDialog(ChangeParamComposite.this.com.getShell());
                ChangeParamComposite.this.list = new ArrayList();
                ChangeParamComposite.this.excelPathname = ChangeParamComposite.this.orderPathText.getText();
                if (!ChangeParamComposite.this.excelPathname.equals("")) {
                    fileDialog.setFilterPath(ChangeParamComposite.this.excelPathname);
                }
                ChangeParamComposite.this.excelPathname = fileDialog.open();
                if (ChangeParamComposite.this.excelPathname == null) {
                    return;
                }
                ChangeParamComposite.this.orderPathText.setText(ChangeParamComposite.this.excelPathname);
                ChangeParamComposite.this.progressData = new ProgressData(ChangeParamComposite.this.excelPathname, ChangeParamComposite.this.cp);
                ChangeParamComposite.this.progressData.parseExcel();
                ChangeParamComposite.this.signCombo.setItems(ChangeParamComposite.this.progressData.getString());
            }
        });
        xlsBrowsebtn.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(ChangeParamComposite.this.com.getShell());
                ChangeParamComposite.this.outputPathname = ChangeParamComposite.this.excelPathText.getText();
                if (!ChangeParamComposite.this.outputPathname.equals("")) {
                    dd.setFilterPath(ChangeParamComposite.this.outputPathname);
                }
                ChangeParamComposite.this.outputPathname = dd.open();
                if (ChangeParamComposite.this.outputPathname == null) {
                    return;
                }
                ChangeParamComposite.this.excelPathText.setText(ChangeParamComposite.this.outputPathname);
            }
        });
        this.signCombo.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                int index = ChangeParamComposite.this.signCombo.getSelectionIndex();
                ChangeParamComposite.this.bean = ChangeParamComposite.this.progressData.getBeans().get(index);
                ChangeParamComposite.this.setCheck(ChangeParamComposite.this.bean.getAttributeCode());
                lbSum.setText(String.valueOf(MsgCenter.getString("XDS.sum")) + ChangeParamComposite.this.bean.getSum());
                ChangeParamComposite.this.numLabel.setText(String.valueOf(MsgCenter.getString("XDS.num")) + ChangeParamComposite.this.bean.getNum());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        this.idText.addMouseListener(new MouseListener(){

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
                ChangeParamComposite.this.idText.selectAll();
            }
        });
        this.idText.addTraverseListener(new TraverseListener(){

            public void keyTraversed(TraverseEvent e) {
                if (e.keyCode == 0x1000050 || e.keyCode == 13) {
                    if (ChangeParamComposite.this.txtPathText.getText().isEmpty() || ChangeParamComposite.this.orderPathText.getText().isEmpty() || ChangeParamComposite.this.txtVersionText.getText().isEmpty() || ChangeParamComposite.this.verionText.getText().isEmpty() || ChangeParamComposite.this.idText.getText().isEmpty()) {
                        ChangeParamComposite.this.messageBox(MsgCenter.getString("TC.titleContent"));
                        return;
                    }
                    if (!ChangeParamComposite.this.progressData.compareTxtVer(ChangeParamComposite.this.txtVersionText.getText(), ChangeParamComposite.this.idText.getText())) {
                        ChangeParamComposite.this.messageBox(MsgCenter.getString("XDS.txtver"));
                        return;
                    }
                    if (ChangeParamComposite.this.bean != null && ChangeParamComposite.this.progressData.compareID(ChangeParamComposite.this.idText.getText(), ChangeParamComposite.this.bean.getAttributeCode())) {
                        if (ChangeParamComposite.this.bean.getNum() > 0) {
                            ChangeParamComposite.this.isCheckID = true;
                            ChangeParamComposite.this.numLabel.setText(String.valueOf(MsgCenter.getString("XDS.num")) + ChangeParamComposite.this.bean.getNum());
                            ChangeParamComposite.this.logText.append(String.valueOf(MsgCenter.getString("XDS.idsuc")) + "\n\r");
                        } else {
                            ChangeParamComposite.this.messageBox(MsgCenter.getString("XDS.numerr"));
                        }
                    } else {
                        ChangeParamComposite.this.isCheckID = false;
                        ChangeParamComposite.this.messageBox(MsgCenter.getString("XDS.iderr"));
                    }
                }
            }
        });
        this.SnText.addMouseListener(new MouseListener(){

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
                ChangeParamComposite.this.SnText.selectAll();
            }
        });
        this.SnText.addTraverseListener(new TraverseListener(){

            public void keyTraversed(TraverseEvent e) {
                if (e.keyCode == 0x1000050 || e.keyCode == 13) {
                    if (!ChangeParamComposite.this.checkAG()) {
                        return;
                    }
                    if (ChangeParamComposite.this.isDepart) {
                        ChangeParamComposite.this.develop();
                    } else {
                        ChangeParamComposite.this.product();
                    }
                }
            }
        });
    }

    private Group getGroup(Composite com, int high, int col, String name) {
        Group group = new Group(com, 0);
        GridData gridData = new GridData(4, 0x1000000, true, false);
        gridData.heightHint = high;
        group.setLayoutData((Object)gridData);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = col;
        group.setLayout((Layout)gridLayout);
        group.setText(name);
        return group;
    }

    private int getBitChange(Button[] button) {
        int functionSwitch = 0;
        int i = 0;
        while (i < button.length) {
            functionSwitch = button[i].getSelection() ? (functionSwitch |= 1 << i) : (functionSwitch &= ~(1 << i));
            ++i;
        }
        return functionSwitch;
    }

    private void setBitChange(int functionSwitch, Button[] button) {
        int i = 0;
        while (i < button.length) {
            if ((functionSwitch >> i & 1) == 1) {
                button[i].setSelection(true);
            } else {
                button[i].setSelection(false);
            }
            ++i;
        }
    }

    protected void setContent(byte[] data) {
        this.txtVersionText.setText(Integer.toString(EndianUtil.nextIntL(data, 0)));
        this.setBitChange(EndianUtil.nextIntL(data, 4), this.switchButtons);
        this.setBitChange(EndianUtil.nextIntL(data, 16), this.languageButtons);
        this.verionText.setText(EndianUtil.nextStringL(data, 20, 40));
        this.SnText.setText(EndianUtil.nextStringL(data, 60, 40).replace(",", ""));
        this.oemLogoText.setText(EndianUtil.nextStringL(data, 100, 40).replace(",", ""));
    }

    private void messageBox(String s) {
        MessageBox messageBox = new MessageBox(this.com.getShell());
        messageBox.setText(MsgCenter.getString("TC.title"));
        messageBox.setMessage(s);
        messageBox.open();
        this.logText.append(String.valueOf(s) + "\n\r");
    }

    private String readParseSn(String sn) {
        int length = sn.length();
        String machineType = sn.substring(0, length - 7);
        String batch = sn.substring(length - 7, length - 3);
        String number = sn.substring(length - 3, length);
        sn = String.valueOf(machineType) + "," + batch + "," + number;
        return sn;
    }

    private void txtlogAppend(byte[] b, Text log) {
        log.append(String.valueOf(MsgCenter.getString("TC.vermessage")) + EndianUtil.nextIntL(b, 0) + "\n\r");
        log.append(String.valueOf(MsgCenter.getString("TC.functionsw")) + EndianUtil.nextIntL(b, 4) + "\n\r");
        log.append(String.valueOf(MsgCenter.getString("TC.deflanguage")) + EndianUtil.nextIntL(b, 12) + "\n\r");
        log.append(String.valueOf(MsgCenter.getString("TC.languagesw")) + EndianUtil.nextIntL(b, 16) + "\n\r");
        log.append(String.valueOf(MsgCenter.getString("TC.ver")) + EndianUtil.nextStringL(b, 20, 40).trim() + "\n\r");
        log.append(String.valueOf(MsgCenter.getString("TC.sn")) + EndianUtil.nextStringL(b, 60, 40).trim() + "\n\r");
    }

    private boolean updateTxtContent(String str) {
        if (!this.verionText.getText().startsWith("V")) {
            return false;
        }
        if (this.getbyte) {
            this.hdsEntity.updateTXTVerMes(Integer.valueOf(this.txtVersionText.getText()));
            this.hdsEntity.updateFunctionSwitch(this.getBitChange(this.switchButtons));
            this.hdsEntity.updateFunctionSwitch2(this.getBitChange(this.switchButtons) >> 16);
            this.hdsEntity.updateDefaultLanguage(0);
            this.hdsEntity.updateLanguageSwitch(this.getBitChange(this.languageButtons));
            this.hdsEntity.updateVer(this.verionText.getText());
            this.hdsEntity.updateSn(this.readParseSn(String.valueOf(str) + this.SnText.getText()));
            this.hdsEntity.updateOemLogo(this.oemLogoText.getText());
            this.txtlogAppend(this.hdsEntity.content, this.logText);
            this.logText.append(String.valueOf(Integer.toString(this.hdsEntity.content.length)) + "\n\r");
        }
        try {
            FileOutputStream fos = new FileOutputStream(this.txtPathname);
            fos.write(this.hdsEntity.content);
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void product() {
        if (this.txtPathText.getText().isEmpty() || this.orderPathText.getText().isEmpty() || this.txtVersionText.getText().isEmpty() || this.verionText.getText().isEmpty() || this.SnText.getText().isEmpty() || this.idText.getText().isEmpty() || this.excelPathText.getText().isEmpty()) {
            this.messageBox(MsgCenter.getString("TC.titleContent"));
            return;
        }
        if (this.isCheckID) {
            int val = this.progressData.saveVersion(this.bean, this.SnText.getText());
            switch (val) {
                case 0: {
                    this.isCheckID = false;
                    this.numLabel.setText(String.valueOf(MsgCenter.getString("XDS.num")) + " " + this.bean.getNum());
                    this.updateTxtContent(this.bean.getMachineType());
                    this.addList();
                    if (!this.progressData.isHas()) {
                        this.messageBox(MsgCenter.getString("XDS.orderend"));
                        String path = String.valueOf(this.outputPathname) + "/" + this.progressData.orderNum + "-" + DateFormat.getDateInstance().format(new Date()) + ".xls";
                        GenerateExcel ge = new GenerateExcel(path);
                        ge.outputExcel(this.list);
                    }
                    this.logText.append(String.valueOf(MsgCenter.getString("XDS.snsuc")) + "\n\r");
                    break;
                }
                case 1: {
                    this.messageBox(MsgCenter.getString("XDS.snexit"));
                    break;
                }
                case 2: {
                    this.messageBox(MsgCenter.getString("XDS.numerr"));
                    break;
                }
                case 3: {
                    this.messageBox(MsgCenter.getString("XDS.snerr"));
                    break;
                }
            }
        } else {
            this.messageBox(MsgCenter.getString("XDS.noidcheck"));
        }
    }

    private void addList() {
        GenerateOrder order = new GenerateOrder();
        order.setSn(String.valueOf(this.bean.getMachineType()) + this.SnText.getText());
        order.setOrderNum(this.progressData.orderNum);
        order.setFunction(this.getFunc());
        this.list.add(order);
    }

    private void develop() {
        if (this.txtPathText.getText().isEmpty() || this.txtVersionText.getText().isEmpty() || this.verionText.getText().isEmpty() || this.SnText.getText().isEmpty()) {
            this.messageBox(MsgCenter.getString("TC.titleContent"));
            return;
        }
        this.updateTxtContent("");
    }

    public String getFunc() {
        String str = "";
        if (this.func.size() == 0) {
            return null;
        }
        String[] fun = new String[this.func.size()];
        int i = 0;
        while (i < this.func.size() - 1) {
            fun[i] = String.valueOf(this.func.get(i)) + " /";
            ++i;
        }
        fun[this.func.size() - 1] = this.func.get(this.func.size() - 1);
        i = 0;
        while (i < fun.length) {
            str = String.valueOf(str) + fun[i];
            if ((i + 1) % 3 == 0) {
                str = String.valueOf(str) + "\n";
            }
            ++i;
        }
        return str;
    }

    private boolean checkAG() {
        if (this.switchButtons[2].getSelection() && this.switchButtons[3].getSelection()) {
            this.messageBox(MsgCenter.getString("XDS.agchlerr"));
            return false;
        }
        if (this.switchButtons[4].getSelection() && this.switchButtons[5].getSelection()) {
            this.messageBox(MsgCenter.getString("XDS.agerr"));
            return false;
        }
        return true;
    }

    private void setCheck(String id) {
        String[] arrayStr = id.split("-");
        this.func = new ArrayList<String>();
        int i = 0;
        while (i < this.switchButtons.length) {
            this.switchButtons[i].setSelection(false);
            ++i;
        }
        i = arrayStr.length;
        while (i > 0) {
            String string = arrayStr[i - 1];
            if (string.equals("AG21")) {
                this.switchButtons[2].setSelection(true);
                this.switchButtons[4].setSelection(true);
                this.func.add("AWG-S25");
            } else if (string.equals("AG22")) {
                this.switchButtons[3].setSelection(true);
                this.switchButtons[4].setSelection(true);
                this.func.add("AWG-D25");
            } else if (string.equals("AG51")) {
                this.switchButtons[2].setSelection(true);
                this.switchButtons[5].setSelection(true);
                this.func.add("AWG-S50");
            } else if (string.equals("AG52")) {
                this.switchButtons[3].setSelection(true);
                this.switchButtons[5].setSelection(true);
                this.func.add("AWG-D50");
            } else if (string.equals("V")) {
                this.switchButtons[7].setSelection(true);
                this.func.add("VGA");
            } else if (string.equals("M")) {
                this.switchButtons[6].setSelection(true);
                this.func.add("DMM");
            } else if (string.equals("T")) {
                this.switchButtons[1].setSelection(true);
                this.func.add("TOUCH");
            } else if (string.equals("W")) {
                this.switchButtons[9].setSelection(true);
                this.func.add("WIFI");
            } else if (string.equals("2")) {
                this.switchButtons[11].setSelection(true);
                this.func.add("I2C");
            } else if (string.equals("S")) {
                this.switchButtons[16].setSelection(true);
                this.func.add("SPI");
            } else if (string.equals("R")) {
                this.switchButtons[17].setSelection(true);
                this.func.add("RS232");
            } else if (string.equals("C")) {
                this.switchButtons[10].setSelection(true);
                this.func.add("CAN");
            } else if (string.equals("I")) {
                this.switchButtons[8].setSelection(true);
                this.func.add("IPS");
            }
            --i;
        }
        this.switchButtons[15].setSelection(true);
    }

    private void onlyOne(int i) {
        int[] ii;
        if (i == 25) {
            this.languageButtons[i + 1].setSelection(this.languageButtons[i].getSelection());
        }
        if ((ii = this.mapLimit.get(i)) == null) {
            return;
        }
        if (this.languageButtons[i].getSelection()) {
            int[] nArray = ii;
            int n = ii.length;
            int n2 = 0;
            while (n2 < n) {
                int i1 = nArray[n2];
                this.languageButtons[i1].setSelection(false);
                ++n2;
            }
        }
    }

    @Override
    public void showInfo(String s) {
    }
}

