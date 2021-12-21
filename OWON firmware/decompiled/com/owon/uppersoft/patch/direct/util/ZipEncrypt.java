/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ZipEncrypt {
    private void directoryZip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(String.valueOf(base) + "/"));
            base = base.length() == 0 ? "" : String.valueOf(base) + "/";
            int i = 0;
            while (i < fl.length) {
                this.directoryZip(out, fl[i], String.valueOf(base) + fl[i].getName());
                ++i;
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            byte[] bb = new byte[2048];
            int aa = 0;
            while ((aa = in.read(bb)) != -1) {
                out.write(bb, 0, aa);
            }
            in.close();
        }
    }

    private void fileZip(ZipOutputStream zos, File file) throws Exception {
        if (file.isFile()) {
            zos.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fis = new FileInputStream(file);
            byte[] bb = new byte[2048];
            int aa = 0;
            while ((aa = fis.read(bb)) != -1) {
                zos.write(bb, 0, aa);
            }
            fis.close();
            System.out.println(file.getName());
        } else {
            this.directoryZip(zos, file, "");
        }
    }

    private void fileUnZip(ZipInputStream zis, File file) throws Exception {
        ZipEntry zip = zis.getNextEntry();
        if (zip == null) {
            return;
        }
        String name = zip.getName();
        File f = new File(String.valueOf(file.getAbsolutePath()) + "/" + name);
        if (zip.isDirectory()) {
            f.mkdirs();
            this.fileUnZip(zis, file);
        } else {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] b = new byte[2048];
            int aa = 0;
            while ((aa = zis.read(b)) != -1) {
                fos.write(b, 0, aa);
            }
            fos.close();
            this.fileUnZip(zis, file);
        }
    }

    private void zip(File directory, File zipFile) {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            this.fileZip(zos, directory);
            zos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unZip(File directory, File zipFile) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            directory.mkdirs();
            this.fileUnZip(zis, directory);
            zis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Key createKey(File f) throws Exception {
        SecureRandom sr = new SecureRandom();
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        SecretKey key = kg.generateKey();
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key.getEncoded());
        fos.close();
        return key;
    }

    public Key getKey(File keyPath) throws Exception {
        FileInputStream fis = new FileInputStream(keyPath);
        byte[] b = new byte[16];
        fis.read(b);
        SecretKeySpec dks = new SecretKeySpec(b, "AES");
        fis.close();
        return dks;
    }

    public Cipher createCipher(Key privateKey, int opmode) throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec spec = new IvParameterSpec(privateKey.getEncoded());
        cipher.init(opmode, privateKey, spec, sr);
        return cipher;
    }

    public void encrypt(File srcFile, File destFile, Cipher cipher) throws Exception {
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] b = new byte[2048];
        while (fis.read(b) != -1) {
            byte[] bb = cipher.doFinal(b);
            System.out.println(bb.length);
            fos.write(bb);
        }
        fos.close();
        fis.close();
    }

    public void decrypt(File srcFile, File destFile, Cipher ciphers) throws Exception {
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] b = new byte[2064];
        while (fis.read(b) != -1) {
            byte[] bb = ciphers.doFinal(b);
            System.out.println(bb.length);
            fos.write(bb);
        }
        fos.close();
        fis.close();
    }

    private void encryptZip(File srcFile, File destfile, Cipher cipher) throws Exception {
        File temp = new File(String.valueOf(UUID.randomUUID().toString()) + ".zip");
        temp.deleteOnExit();
        this.zip(srcFile, temp);
        this.encrypt(temp, destfile, cipher);
        temp.delete();
    }

    private void decryptUnzip(File srcfile, File destfile, Cipher cipher) throws Exception {
        File temp = new File(String.valueOf(UUID.randomUUID().toString()) + ".zip");
        temp.deleteOnExit();
        this.decrypt(srcfile, temp, cipher);
        this.unZip(destfile, temp);
        temp.delete();
    }

    public static void main(String[] args) throws Exception {
        long a = System.currentTimeMillis();
        ZipEncrypt ze = new ZipEncrypt();
        Key key = ze.createKey(new File("i:/comXXX/public.key"));
        Cipher ec = ze.createCipher(key, 1);
        ze.encryptZip(new File("i:/com"), new File("i:/comXXX/page.zip"), ec);
        System.out.println(System.currentTimeMillis() - a);
        a = System.currentTimeMillis();
        Cipher dc = ze.createCipher(key, 2);
        ze.decryptUnzip(new File("i:/comXXX/page.zip"), new File("i:/comxxx"), dc);
        System.out.println(System.currentTimeMillis() - a);
    }
}

