/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.update.DownloadFile;
import com.owon.uppersoft.common.update.IUpdatable;
import com.owon.uppersoft.common.utils.FileUtil;
import com.owon.uppersoft.common.utils.SystemPropertiesUtil;
import com.owon.uppersoft.common.utils.ZipUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UpdateDetection {
    private IUpdatable iu;
    private File localUpdateXML;
    private byte[] downloadBuffer = new byte[10240];
    private File downloadTempDir;
    public static final String String_bundle = "bundle";
    public static final String String_productType = "productType";
    public static final String String_os = "os";
    public static final String String_typeVersion = "typeVersion";
    public static final String String_eclipseproduct = "eclipseproduct";
    public static final String String_typeVersion3_3 = "3.3";
    public static final String String_productVersionStart = "productVersionStart";
    public static final String String_productVersionEnd = "productVersionEnd";
    public static final String String_urlbranch = "urlbranch";
    public static final String String_dir = "dir";
    public static final String String_localName = "localName";
    public static final String String_remoteName = "remoteName";
    public static final String String_updateType = "updateType";
    public static final String String_file = "file";
    public static final String String_plugins = "plugins";
    public static final String String_remote = "remote";
    public static final String String_id = "id";
    public static final String String_version = "version";
    public static final String String_delFile = "delFile";
    public static final String String_zip = "zip";
    public static final String String_format = "format";
    public static final String String_replace = "replace";
    public static final String String_execName = "execName";
    private List<DownloadFile> urls;
    private String execName = "";
    private File localDir;
    private String[] localFileNames;

    public UpdateDetection(IUpdatable iu) {
        this.iu = iu;
        this.downloadTempDir = new File(iu.getConfigurationDir(), "downloadTemp");
        FileUtil.deleteFile(this.downloadTempDir);
        this.downloadTempDir.mkdirs();
        this.localUpdateXML = new File(this.downloadTempDir, iu.getRelativePath());
        FileUtil.checkPath(this.localUpdateXML);
    }

    public File getLocalUpdateXML() {
        return this.localUpdateXML;
    }

    public IUpdatable getUpdatable() {
        return this.iu;
    }

    public File getDownloadTempDir() {
        return this.downloadTempDir;
    }

    public void filesUpdate(List<DownloadFile> files) {
        for (DownloadFile df : files) {
            File lf = df.getLocalTempFile();
            String destFile = df.getDestFile();
            if (df.isZip()) {
                File f = new File(destFile);
                if (destFile.length() == 0) {
                    try {
                        f = f.getCanonicalFile();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    FileUtil.deleteFile(f);
                }
                ZipUtil.unzip(lf, f);
                FileUtil.deleteFile(lf);
                continue;
            }
            FileUtil.replaceFile(lf, new File(destFile));
        }
        File updateXml = new File(this.iu.getRelativePath());
        FileUtil.replaceFile(this.localUpdateXML, updateXml);
    }

    public String detectServers() {
        List<String> servers = this.iu.getUpdatableServers();
        for (String url : servers) {
            File file = this.getFile(url, this.localUpdateXML);
            if (file != null) {
                return url;
            }
            int delay = 0;
            if (delay <= 0) continue;
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public File getFile(String destUrl, File file) {
        try {
            FileOutputStream fos = null;
            BufferedInputStream bis = null;
            int size = 0;
            URL url = new URL(new URL(destUrl), this.iu.getRelativePath());
            HttpURLConnection hurl = (HttpURLConnection)url.openConnection();
            hurl.setConnectTimeout(1000);
            hurl.setReadTimeout(1000);
            hurl.connect();
            bis = new BufferedInputStream(hurl.getInputStream());
            fos = new FileOutputStream(file);
            while ((size = bis.read(this.downloadBuffer)) != -1) {
                fos.write(this.downloadBuffer, 0, size);
            }
            fos.close();
            bis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public String getExecName() {
        return this.execName;
    }

    public List<DownloadFile> getDownloadFileURLs() {
        return this.urls;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isUpdate() {
        if (this.urls == null) {
            this.urls = new LinkedList<DownloadFile>();
        } else {
            this.urls.clear();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new FileInputStream(this.localUpdateXML));
            is.setEncoding("UTF-8");
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            Node bundle = null;
            String urlbranch = "";
            Node rootChild = root.getFirstChild();
            while (rootChild != null) {
                if (rootChild.getNodeType() == 1 && rootChild.getNodeName().equals(String_bundle)) {
                    boolean isBundle;
                    Element e = (Element)rootChild;
                    String productType = e.getAttribute(String_productType);
                    String os = e.getAttribute(String_os);
                    String typeVersion = e.getAttribute(String_typeVersion);
                    String productVersionStart = e.getAttribute(String_productVersionStart);
                    String productVersionEnd = e.getAttribute(String_productVersionEnd);
                    urlbranch = e.getAttribute(String_urlbranch);
                    int compStart = UpdateDetection.compareVersion(productVersionStart, this.iu.getProductVersion());
                    int compEnd = UpdateDetection.compareVersion(productVersionEnd, this.iu.getProductVersion());
                    boolean bl = isBundle = productType.equals(String_eclipseproduct) && typeVersion.equals(String_typeVersion3_3) && os.equals(SystemPropertiesUtil.getPlatfromType()) && compStart <= 0 && compEnd >= 0;
                    if (isBundle) {
                        if (compEnd == 0) {
                            return true;
                        }
                        bundle = e;
                        break;
                    }
                }
                rootChild = rootChild.getNextSibling();
            }
            if (bundle == null) {
                return false;
            }
            Element delElement = null;
            Node bundleChild = bundle.getFirstChild();
            while (true) {
                if (bundleChild == null) {
                    if (delElement == null) return true;
                    this.transFormerDoc(doc);
                    return true;
                }
                if (bundleChild.getNodeType() == 1 && bundleChild.getNodeName().equals(String_dir)) {
                    Element dirElement = (Element)bundleChild;
                    String remoteName = dirElement.getAttribute(String_remoteName);
                    String localName = dirElement.getAttribute(String_localName);
                    if (urlbranch.length() != 0) {
                        remoteName = String.valueOf(urlbranch) + "/" + remoteName;
                    }
                    if (dirElement.getAttribute(String_format).equals(String_zip) && dirElement.getAttribute(String_updateType).equals(String_replace)) {
                        String eN = dirElement.getAttribute(String_execName);
                        if (localName.length() == 0) {
                            this.execName = eN;
                        }
                        DownloadFile df = new DownloadFile(localName, remoteName);
                        this.urls.add(df);
                        df.setZip(true);
                    } else {
                        this.localDir = new File(localName);
                        this.localDir.mkdirs();
                        this.localFileNames = this.localDir.list();
                        Node dirChild = bundleChild.getFirstChild();
                        while (dirChild != null) {
                            String fileVersion;
                            Element fileElement;
                            String fileId;
                            String localFileName;
                            String nodeName;
                            if (dirChild.getNodeType() == 1 && !(nodeName = dirChild.getNodeName()).equals(String_file) && (localFileName = this.getLocalVer(fileId = (fileElement = (Element)dirChild).getAttribute(String_id), fileVersion = fileElement.getAttribute(String_version))) != null) {
                                String fileRemote = fileElement.getTextContent().trim();
                                boolean isZip = nodeName.equals(String_dir) && fileElement.getAttribute(String_format).equals(String_zip);
                                DownloadFile df = new DownloadFile(String.valueOf(localName) + "/" + (isZip ? fileRemote.substring(0, fileRemote.lastIndexOf(".")) : fileRemote), String.valueOf(remoteName) + "/" + fileRemote);
                                this.urls.add(df);
                                df.setZip(isZip);
                                if (localFileName.length() != 0) {
                                    delElement = doc.createElement(String_delFile);
                                    fileElement.appendChild(delElement);
                                    delElement.setTextContent(localFileName);
                                }
                            }
                            dirChild = dirChild.getNextSibling();
                        }
                    }
                }
                bundleChild = bundleChild.getNextSibling();
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        catch (SAXException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int transFormerDoc(Node node) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            DOMSource source = new DOMSource(node);
            StreamResult result = new StreamResult(this.localUpdateXML);
            transformer.transform(source, result);
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return 3;
        }
        catch (TransformerException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

    private String getLocalVer(String id, String version) {
        String[] stringArray = this.localFileNames;
        int n = this.localFileNames.length;
        int n2 = 0;
        while (n2 < n) {
            String path = stringArray[n2];
            int index = path.indexOf(id);
            if (index >= 0) {
                String currentVersion;
                int lastI = path.lastIndexOf(".jar");
                if (lastI < 0) {
                    lastI = path.length();
                }
                if (UpdateDetection.compareVersion(version, currentVersion = path.substring(id.length() + 1, lastI)) > 0) {
                    return String.valueOf(this.localDir.getName()) + File.separator + path;
                }
                return null;
            }
            ++n2;
        }
        return "";
    }

    public static int compareVersion(String v1, String v2) {
        StringTokenizer t1 = new StringTokenizer(v1, ".");
        StringTokenizer t2 = new StringTokenizer(v2, ".");
        while (t1.hasMoreTokens()) {
            int n2;
            if (!t2.hasMoreTokens()) {
                return 1;
            }
            int n1 = Integer.parseInt(t1.nextToken());
            int d = n1 - (n2 = Integer.parseInt(t2.nextToken()));
            if (d == 0) continue;
            return d;
        }
        return t2.hasMoreTokens() ? -1 : 0;
    }

    public static void main(String[] args) {
        System.out.println("sdfs.fds".lastIndexOf(".jar"));
    }
}

