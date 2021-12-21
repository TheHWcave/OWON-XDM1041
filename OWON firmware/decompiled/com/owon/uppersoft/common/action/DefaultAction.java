/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 *  org.eclipse.swt.widgets.ToolBar
 *  org.eclipse.swt.widgets.ToolItem
 */
package com.owon.uppersoft.common.action;

import com.owon.uppersoft.common.action.AbstractItem;
import com.owon.uppersoft.common.action.EventType;
import com.owon.uppersoft.common.action.ItemChangeSupport;
import com.owon.uppersoft.common.action.ItemListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class DefaultAction
extends AbstractItem
implements Runnable,
Listener {
    private Image image;
    private MenuItem mi;
    private ToolItem ti;
    private boolean check;
    private boolean enabled;
    private ItemChangeSupport ics = new ItemChangeSupport();

    public DefaultAction(String id) {
        this(id, 8);
    }

    public DefaultAction(String id, int type) {
        super(id, type);
    }

    public MenuItem fill(Menu m) {
        Image img;
        this.mi = new MenuItem(m, this.getType());
        this.mi.addListener(13, (Listener)this);
        this.ics.addItemListener(new ItemListener(){

            @Override
            public void handle(EventType type, Object value) {
                switch (type) {
                    case ImageEvent: {
                        DefaultAction.this.mi.setImage((Image)value);
                        break;
                    }
                    case TextEvent: {
                        DefaultAction.this.mi.setText((String)value);
                        break;
                    }
                    case EnabledEvent: {
                        DefaultAction.this.mi.setEnabled(((Boolean)value).booleanValue());
                        break;
                    }
                    case SelectEvent: {
                        DefaultAction.this.mi.setSelection(((Boolean)value).booleanValue());
                        break;
                    }
                }
            }
        });
        if ((this.getType() & 0x10) != 0) {
            this.ics.addItemListener(new ItemListener(){

                @Override
                public void handle(EventType type, Object value) {
                    if (type.equals((Object)EventType.SelectEvent) && ((Boolean)value).booleanValue()) {
                        Menu m = DefaultAction.this.mi.getParent();
                        MenuItem[] menuItemArray = m.getItems();
                        int n = menuItemArray.length;
                        int n2 = 0;
                        while (n2 < n) {
                            MenuItem mm = menuItemArray[n2];
                            if (mm != DefaultAction.this.mi && (mm.getStyle() & 0x10) != 0) {
                                mm.setSelection(false);
                            }
                            ++n2;
                        }
                    }
                }
            });
        }
        if ((img = this.getImage()) != null) {
            this.mi.setImage(img);
        }
        return this.mi;
    }

    public ToolItem fill(ToolBar tb) {
        Image img;
        this.ti = new ToolItem(tb, this.getType());
        this.ti.addListener(13, (Listener)this);
        this.ics.addItemListener(new ItemListener(){

            @Override
            public void handle(EventType type, Object value) {
                switch (type) {
                    case ImageEvent: {
                        DefaultAction.this.ti.setImage((Image)value);
                        break;
                    }
                    case TextEvent: {
                        DefaultAction.this.ti.setToolTipText((String)value);
                        break;
                    }
                    case EnabledEvent: {
                        DefaultAction.this.ti.setEnabled(((Boolean)value).booleanValue());
                        break;
                    }
                    case SelectEvent: {
                        DefaultAction.this.ti.setSelection(((Boolean)value).booleanValue());
                        break;
                    }
                }
            }
        });
        if ((this.getType() & 0x10) != 0) {
            this.ics.addItemListener(new ItemListener(){

                @Override
                public void handle(EventType type, Object value) {
                    if (type.equals((Object)EventType.SelectEvent) && ((Boolean)value).booleanValue()) {
                        ToolBar t = DefaultAction.this.ti.getParent();
                        ToolItem[] toolItemArray = t.getItems();
                        int n = toolItemArray.length;
                        int n2 = 0;
                        while (n2 < n) {
                            ToolItem tt = toolItemArray[n2];
                            if (tt != DefaultAction.this.ti && (tt.getStyle() & 0x10) != 0) {
                                tt.setSelection(false);
                            }
                            ++n2;
                        }
                    }
                }
            });
        }
        if ((img = this.getImage()) != null) {
            this.ti.setImage(img);
        }
        return this.ti;
    }

    public void handleEvent(Event event) {
        if (event.type != 13) {
            return;
        }
        this.setCheck(true);
        this.run();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.ics.fireItemListener(EventType.TextEvent, text);
    }

    public Image getImage() {
        return this.image;
    }

    public boolean isCheck() {
        return this.check;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setImage(Image image) {
        this.image = image;
        this.ics.fireItemListener(EventType.ImageEvent, image);
    }

    public void setCheck(boolean check) {
        this.check = check;
        this.ics.fireItemListener(EventType.SelectEvent, check);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.ics.fireItemListener(EventType.EnabledEvent, enabled);
    }

    @Override
    public void run() {
    }
}

