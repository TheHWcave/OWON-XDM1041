/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct.util;

import com.owon.uppersoft.patch.direct.util.LZSSInputStream;
import com.owon.uppersoft.patch.direct.util.LZSSOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class pack {
    public static final byte[] F_PACK_MAGIC = new byte[]{115, 108, 104, 33};
    public static final byte[] F_NOPACK_MAGIC = new byte[]{115, 108, 104, 46};

    private static void usage() {
        System.out.print("\nFile compression utility for Allegro 3.12");
        System.out.print("\nBy Shawn Hargreaves, Aug 1999. Java version by Radim Kolar, Dec 1999\n\n");
        System.out.print("Usage: 'pack <in> <out>' to pack a file\n");
        System.out.print("       'pack u <in> <out>' to unpack a file\n");
        System.exit(1);
    }

    public static void main(String[] argv) throws IOException {
        int i;
        boolean bad;
        argv = new String[2];
        argv[1] = "C:\\Users\\z\\Desktop\\packXDS3xx2_Help_V1.9.0_20180101.bin";
        argv[0] = "C:\\Users\\z\\Desktop\\uppackXDS3xx2_Help_V1.9.0_20180101.txt";
        String t = "";
        String f1 = "";
        String f2 = "";
        boolean pack2 = true;
        if (argv.length == 2) {
            f1 = argv[0];
            f2 = argv[1];
            t = "Pack";
        } else if (argv.length == 3 && argv[0].length() == 1 && (argv[0].charAt(0) == 'u' || argv[0].charAt(0) == 'U')) {
            f1 = argv[1];
            f2 = argv[2];
            t = "Unpack";
            pack2 = false;
        } else {
            pack.usage();
        }
        if (f1.equals(f2)) {
            System.out.println("\nError: Files must be different.");
            System.exit(1);
        }
        InputStream in = null;
        try {
            in = new FileInputStream(f1);
        }
        catch (IOException iOException) {
            System.out.println("\nError: " + f1 + " - Can't open.");
            System.exit(1);
        }
        long s1 = new File(f1).length();
        if (!pack2 && !(bad = false)) {
            in = new LZSSInputStream(in);
        }
        OutputStream out = new FileOutputStream(f2);
        if (pack2) {
            out = new LZSSOutputStream(out);
        }
        System.out.println(String.valueOf(t) + "ing " + f1 + " into " + f2 + "...");
        byte[] b = new byte[512];
        while ((i = in.read(b)) != -1) {
            out.write(b, 0, i);
        }
        in.close();
        out.close();
        if (s1 > 0L) {
            long s2 = new File(f2).length();
            System.out.println("\nInput size: " + s1 + "\nOutput size: " + s2 + "\n" + (s2 * 100L + (s1 >> 1)) / s1 + "%");
        }
    }
}

