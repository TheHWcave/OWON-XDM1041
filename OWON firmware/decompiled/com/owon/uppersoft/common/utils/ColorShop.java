/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.RGB
 *  org.eclipse.swt.graphics.Resource
 *  org.eclipse.swt.widgets.Display
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.aspect.Disposable;
import com.owon.uppersoft.common.utils.DisposeUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

public class ColorShop
implements Disposable {
    private Map<RGB, Color> colorMap;
    private Display display;

    public ColorShop(Display display) {
        this.display = display;
        this.colorMap = new HashMap<RGB, Color>();
    }

    public Color getColor(RGB rgb) {
        Color c = this.colorMap.get(rgb);
        if (c == null) {
            c = new Color((Device)this.display, rgb);
            this.colorMap.put(rgb, c);
        }
        return c;
    }

    @Override
    public void dispose() {
        Set<Map.Entry<RGB, Color>> set = this.colorMap.entrySet();
        for (Map.Entry<RGB, Color> m : set) {
            DisposeUtil.tryDispose((Resource)m.getValue());
        }
        this.colorMap.clear();
    }
}

