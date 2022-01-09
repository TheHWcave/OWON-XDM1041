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

public class ZipEncrypt_Org {
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

    private Key getKey(File keyPath) throws Exception {
        FileInputStream fis = new FileInputStream(keyPath);
        byte[] b = new byte[16];
        fis.read(b);
        SecretKeySpec dks = new SecretKeySpec(b, "AES");
        fis.close();
        return dks;
    }

    private void encrypt(File srcFile, File destFile, Key privateKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec spec = new IvParameterSpec(privateKey.getEncoded());
        cipher.init(1, privateKey, spec, sr);
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] b = new byte[2048];
        while (fis.read(b) != -1) {
            fos.write(cipher.doFinal(b));
        }
        fos.close();
        fis.close();
    }

    private void decrypt(File srcFile, File destFile, Key privateKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher ciphers = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec spec = new IvParameterSpec(privateKey.getEncoded());
        ciphers.init(2, privateKey, spec, sr);
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] b = new byte[2064];
        while (fis.read(b) != -1) {
            fos.write(ciphers.doFinal(b));
        }
        fos.close();
        fis.close();
    }

    public Key genKey(File f) throws Exception {
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
        return key;
    }

    public void encryptZip(File srcFile, File destfile, File keyfile) throws Exception {
        Key key = this.genKey(keyfile);
        File temp = new File(String.valueOf(UUID.randomUUID().toString()) + ".zip");
        temp.deleteOnExit();
        this.zip(srcFile, temp);
        this.encrypt(temp, destfile, key);
        temp.delete();
    }

    public void decryptUnzip(File srcfile, File destfile, File keyfile) throws Exception {
        File temp = new File(String.valueOf(UUID.randomUUID().toString()) + ".zip");
        temp.deleteOnExit();
        this.decrypt(srcfile, temp, this.getKey(keyfile));
        this.unZip(destfile, temp);
        temp.delete();
    }

    public static void main(String[] args) throws Exception {
        long a = System.currentTimeMillis();
        new ZipEncrypt_Org().encryptZip(new File("e:/com"), new File("e:/comXXX/page.zip"), new File("e:/comXXX/public.key"));
        System.out.println(System.currentTimeMillis() - a);
        a = System.currentTimeMillis();
        new ZipEncrypt_Org().decryptUnzip(new File("e:/comXXX/page.zip"), new File("e:/comxxx"), new File("e:/comXXX/public.key"));
        System.out.println(System.currentTimeMillis() - a);
    }
}

