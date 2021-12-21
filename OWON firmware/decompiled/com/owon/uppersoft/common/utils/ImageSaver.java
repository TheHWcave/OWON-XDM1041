/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Drawable
 *  org.eclipse.swt.graphics.GC
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.graphics.ImageData
 *  org.eclipse.swt.graphics.ImageLoader
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.utils.FileUtil;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ImageSaver {
    public static void doSave(Shell shell, Image image, String infoDefault, String prompt) {
        FileDialog fd = new FileDialog(shell, 8192);
        String wildcard = "*.";
        String[] images = new String[]{String.valueOf(wildcard) + "bmp", String.valueOf(wildcard) + "png", String.valueOf(wildcard) + "gif"};
        fd.setFilterExtensions(images);
        fd.setFilterNames(images);
        String path = fd.open();
        if (path == null) {
            return;
        }
        String ext = FileUtil.getExtension(path);
        int format = -1;
        if (ext.equalsIgnoreCase("bmp")) {
            format = 0;
        }
        if (ext.equalsIgnoreCase("png")) {
            format = 5;
        }
        if (ext.equalsIgnoreCase("gif")) {
            format = 0;
        }
        if (format == -1) {
            System.err.println("IMAGE_UNDEFINED");
            format = 0;
            path = String.valueOf(path) + ".bmp";
        }
        ImageLoader il = new ImageLoader();
        il.data = new ImageData[]{image.getImageData()};
        il.save(path, format);
        MessageDialog.openInformation((Shell)shell, (String)infoDefault, (String)(String.valueOf(prompt) + path));
    }

    public static void doSave(Shell shell, BufferedImage image, String infoDefault, String prompt) {
        FileDialog fd = new FileDialog(shell, 8192);
        String wildcard = "*.";
        String[] images = new String[]{String.valueOf(wildcard) + "bmp", String.valueOf(wildcard) + "png", String.valueOf(wildcard) + "gif"};
        fd.setFilterExtensions(images);
        fd.setFilterNames(images);
        String path = fd.open();
        if (path == null) {
            return;
        }
        String ext = FileUtil.getExtension(path);
        try {
            ImageIO.write((RenderedImage)image, ext, new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        MessageDialog.openInformation((Shell)shell, (String)infoDefault, (String)(String.valueOf(prompt) + path));
    }

    public static void main_hide(String[] args) {
        Display display = Display.getDefault();
        Image image = new Image((Device)display, 600, 400);
        GC gc = new GC((Drawable)image);
        gc.setForeground(display.getSystemColor(9));
        gc.drawLine(10, 10, 50, 50);
        ImageLoader il = new ImageLoader();
        il.data = new ImageData[]{image.getImageData()};
        il.save("c:/test.gif", 0);
    }
}

