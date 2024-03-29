package top.yqingyu.common.utils;


import top.yqingyu.common.qydata.DataList;
import top.yqingyu.common.qydata.DataMap;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.yqingyu.common.utils.UnameUtil.isWindows;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.ResourceUtil
 * @Description TODO
 * @createTime 2022年09月29日 10:09:00
 */
public class ResourceUtil {
    /*
     * Copyright 2002-2018 the original author or authors.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      https://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */


    /**
     * Pseudo URL prefix for loading from the class path: "classpath:".
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * URL prefix for loading from the file system: "file:".
     */
    public static final String FILE_URL_PREFIX = "file:";

    /**
     * URL prefix for loading from a jar file: "jar:".
     */
    public static final String JAR_URL_PREFIX = "jar:";

    /**
     * URL prefix for loading from a war file on Tomcat: "war:".
     */
    public static final String WAR_URL_PREFIX = "war:";

    /**
     * URL protocol for a file in the file system: "file".
     */
    public static final String URL_PROTOCOL_FILE = "file";

    /**
     * URL protocol for an entry from a jar file: "jar".
     */
    public static final String URL_PROTOCOL_JAR = "jar";

    /**
     * URL protocol for an entry from a war file: "war".
     */
    public static final String URL_PROTOCOL_WAR = "war";

    /**
     * URL protocol for an entry from a zip file: "zip".
     */
    public static final String URL_PROTOCOL_ZIP = "zip";

    /**
     * URL protocol for an entry from a WebSphere jar file: "wsjar".
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /**
     * URL protocol for an entry from a JBoss jar file: "vfszip".
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";

    /**
     * URL protocol for a JBoss file system resource: "vfsfile".
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    /**
     * URL protocol for a general JBoss VFS resource: "vfs".
     */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /**
     * File extension for a regular jar file: ".jar".
     */
    public static final String JAR_FILE_EXTENSION = ".jar";

    /**
     * Separator between JAR URL and file path within the JAR: "!/".
     */
    public static final String JAR_URL_SEPARATOR = "!/";

    /**
     * Special separator between WAR URL and jar part on Tomcat.
     */
    public static final String WAR_URL_SEPARATOR = "*/";


    /**
     * Return whether the given resource location is a URL:
     * either a special "classpath" pseudo URL or a standard URL.
     *
     * @param resourceLocation the location String to check
     * @return whether the location qualifies as a URL
     * @see #CLASSPATH_URL_PREFIX
     * @see java.net.URL
     */
    public static boolean isUrl(String resourceLocation) {
        if (resourceLocation == null) {
            return false;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     * 仅适用于Jar包内、外小文件,本方法将会把文件全量读到内存中
     * 包外文件为绝对路径 内文件相对路径(top.yqingyu.xxxx.txt ->> classpath:top/yqingyu/xxxx.txt)
     *
     * @param resourceLocation 资源路径
     * @return 流的字节数组
     * @author YYJ
     */
    @SuppressWarnings("all")
    public static byte[] getResourceAsBytes(String resourceLocation) throws IOException {
        InputStream stream;
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = ClazzUtil.getDefaultClassLoader();
            URL resource = cl.getResource(path);
            stream = (cl != null ? cl.getResourceAsStream(path) : ClassLoader.getSystemResourceAsStream(path));
        } else {
            stream = new FileInputStream(resourceLocation);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024 * 4];
            int length = 0;
            while ((length = stream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }

        } catch (Exception e) {
            throw new RuntimeException("can not  resolve the File to byte array  because file : " + resourceLocation + "is not fount", e);
        } finally {
            outputStream.close();
            stream.close();
        }
        return outputStream.toByteArray();
    }

    /**
     * @param cfgFileName 配置文件名称
     * @param target_Regx 目标文件正则表达式
     * @return 目标文件list
     * @author YYJ
     * @description 运行路径中的文件
     */
    public static HashMap<String, File> getConfigFileOuter(String rootPath, String cfgFileName, String target_Regx) {

        File file = new File(rootPath);

        AtomicInteger atomicInteger = new AtomicInteger();

        Pattern tgPattern = Pattern.compile(target_Regx);


        File[] files = file.listFiles((dir, name) -> {
            if (dir.isDirectory())
                return true;
            Matcher matcher = tgPattern.matcher(name);
            return matcher.find();
        });

        HashMap<String, File> map = new HashMap<>();
        LinkedList<File> queue = new LinkedList<>(Arrays.asList(files));
        do {
            File poll = queue.poll();
            if (poll != null) {

                File[] listFiles = null;
                if (poll.isDirectory()) {
                    listFiles = poll.listFiles((dir, name) -> {
                        if (dir.isDirectory())
                            return true;
                        Matcher matcher = tgPattern.matcher(name);
                        return matcher.find();
                    });
                }
                if (listFiles != null && listFiles.length > 0)
                    queue.addAll(Arrays.asList(listFiles));

                if (poll.isFile()) {
                    String name = poll.getName();
                    Matcher matcher = tgPattern.matcher(name);
                    if (name.contains(cfgFileName) && matcher.find()) {
                        if (map.get(name) != null)
                            map.put(name + "_" + atomicInteger.getAndIncrement(), poll);
                        else
                            map.put(name, poll);
                    }
                }
            }
        } while (queue.size() > 0);

        return map;
    }


    public static DataList getConfigFileInner(String cfgFileName, String target_Regx) throws IOException {


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("/");
        Iterator<URL> urlIterator = resources.asIterator();

        DataList list = new DataList();

        while (urlIterator.hasNext()) {
            URL url = urlIterator.next();
            String protocol = url.getProtocol();
            if (protocol.equals("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.contains(cfgFileName) && jarEntryName.matches(target_Regx)) {
                        DataMap dataMap = new DataMap();
                        dataMap.put("path", jarEntryName);
                        String[] split = jarEntryName.split("/");
                        dataMap.put("name", split[split.length - 1]);
                        list.add(dataMap);
                    }
                }
            }
        }
        return list;
    }

    /**
     * @param rootPath 文件映射
     *                 eg  /a
     *                 |
     *                 ----c
     *                 |
     *                 ---d.txt
     *                 |
     *                 ---e.exe
     *                 getFilePathMapping("/a")
     *                 return
     *                 /c/d.txt     /a/c/d.txt
     *                 /c/e.txt      /a/c/e.txt
     * @return 目标文件list
     * @author YYJ
     */
    public static HashMap<String, String> getFilePathMapping(String rootPath) {
        String file_separator = FileSystems.getDefault().getSeparator();

        if (isWindows() && rootPath.indexOf("/") == rootPath.length() - 1)
            rootPath = StringUtil.removeEnd(rootPath, "/");

        if (rootPath.endsWith(file_separator) && rootPath.length() > 1)
            rootPath = StringUtil.removeEnd(rootPath, file_separator);

        File file = new File(rootPath);
        File[] files = file.listFiles();
        HashMap<String, String> map = new HashMap<>();

        assert files != null;
        LinkedList<File> queue = new LinkedList<>(Arrays.asList(files));
        do {
            File poll = queue.poll();
            if (poll == null) {
                break;
            }
            File[] listFiles = null;
            if (poll.isDirectory()) {
                listFiles = poll.listFiles();
            }
            if (listFiles != null && listFiles.length > 0)
                queue.addAll(Arrays.asList(listFiles));

            if (poll.isFile()) {
                String path = poll.getPath();
                String key = path.replace(rootPath, "");

                if (isWindows()) {
                    key = key.replaceAll("\\\\", "/");
                    if (key.indexOf("/") != 0)
                        key = "/" + key;
                }

                map.put(key, path);
            }

        } while (!queue.isEmpty());

        return map;
    }

    private final static ConcurrentHashMap<String, byte[]> FILE_RESOURCE_CACHE = new ConcurrentHashMap<>();

    public static byte[] getFileFromCache(String resourceLocation) throws IOException {
        if (FILE_RESOURCE_CACHE.containsKey(resourceLocation)) {
            return FILE_RESOURCE_CACHE.get(resourceLocation);
        }
        byte[] resourceAsBytes = getResourceAsBytes(resourceLocation);
        FILE_RESOURCE_CACHE.put(resourceLocation, resourceAsBytes);
        return resourceAsBytes;

    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     * <p>Does not check whether the URL actually exists; simply returns
     * the URL that the given location would correspond to.
     *
     * @param resourceLocation the resource location to resolve: either a
     *                         "classpath:" pseudo URL, a "file:" URL, or a plain file path
     * @return a corresponding URL object
     * @throws FileNotFoundException if the resource cannot be resolved to a URL
     */
    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        AssertUtil.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = ClazzUtil.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description +
                        " cannot be resolved to URL because it does not exist");
            }
            return url;
        }
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException("Resource location [" + resourceLocation +
                        "] is neither a URL not a well-formed file path");
            }
        }
    }

    /**
     * Resolve the given resource location to a {@code java.io.File},
     * i.e. to a file in the file system.
     * <p>Does not check whether the file actually exists; simply returns
     * the File that the given location would correspond to.
     *
     * @param resourceLocation the resource location to resolve: either a
     *                         "classpath:" pseudo URL, a "file:" URL, or a plain file path
     * @return a corresponding File object
     * @throws FileNotFoundException if the resource cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(String resourceLocation) throws FileNotFoundException {
        AssertUtil.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClazzUtil.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description +
                        " cannot be resolved to absolute file path because it does not exist");
            }
            return getFile(url, description);
        }
        try {
            // try URL
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to resolve
     * @param description a description of the original resource that
     *                    the URL was created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        AssertUtil.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     * @since 2.5
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to resolve
     * @param description a description of the original resource that
     *                    the URI was created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     * @since 2.5
     */
    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        AssertUtil.notNull(resourceUri, "Resource URI must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    /**
     * Determine whether the given URL points to a resource in the file system,
     * i.e. has protocol "file", "vfsfile" or "vfs".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a file system URL
     */
    public static boolean isFileURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFS.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a resource in a jar file.
     * i.e. has protocol "jar", "war, ""zip", "vfszip" or "wsjar".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR URL
     */
    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol) ||
                URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_VFSZIP.equals(protocol) ||
                URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a jar file itself,
     * that is, has protocol "file" and ends with the ".jar" extension.
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR file URL
     * @since 4.1
     */
    public static boolean isJarFileURL(URL url) {
        return (URL_PROTOCOL_FILE.equals(url.getProtocol()) &&
                url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
    }

    /**
     * Extract the URL for the actual jar file from the given URL
     * (which may point to a resource in a jar file or to a jar file itself).
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throws MalformedURLException if no valid jar file URL could be extracted
     */
    public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                // Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
                // This usually indicates that the jar file resides in the file system.
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * Extract the URL for the outermost archive from the given jar/war URL
     * (which may point to a resource in a jar file or to a jar file itself).
     * <p>In the case of a jar file nested within a war file, this will return
     * a URL to the war file since that is the one resolvable in the file system.
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throws MalformedURLException if no valid jar file URL could be extracted
     * @see #extractJarFileURL(URL)
     * @since 4.1.8
     */
    public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();

        int endIndex = urlFile.indexOf(WAR_URL_SEPARATOR);
        if (endIndex != -1) {
            // Tomcat's "war:file:...mywar.war*/WEB-INF/lib/myjar.jar!/myentry.txt"
            String warFile = urlFile.substring(0, endIndex);
            if (URL_PROTOCOL_WAR.equals(jarUrl.getProtocol())) {
                return new URL(warFile);
            }
            int startIndex = warFile.indexOf(WAR_URL_PREFIX);
            if (startIndex != -1) {
                return new URL(warFile.substring(startIndex + WAR_URL_PREFIX.length()));
            }
        }

        // Regular "jar:file:...myjar.jar!/myentry.txt"
        return extractJarFileURL(jarUrl);
    }

    /**
     * Create a URI instance for the given URL,
     * replacing spaces with "%20" URI encoding first.
     *
     * @param url the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the URL wasn't a valid URI
     * @see java.net.URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String,
     * replacing spaces with "%20" URI encoding first.
     *
     * @param location the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtil.replace(location, " ", "%20"));
    }

    /**
     * Set the {@link URLConnection#setUseCaches "useCaches"} flag on the
     * given connection, preferring {@code false} but leaving the
     * flag at {@code true} for JNLP based resources.
     *
     * @param con the URLConnection to set the flag on
     */
    public static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }


}

