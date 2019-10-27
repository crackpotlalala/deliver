package com.deliver.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * FTP 工具类
 */
@Slf4j
public class FtpUtil {

    // 被动模式
    public static final String PASSIVE_MODE = "passive";
    // 主动模式
    public static final String ACTIVE_MODE = "active";

    private FTPClient ftpClient = null;
    private String server;
    private int port;
    private String userName;
    private String userPassword;
    private boolean sslEnable = true;
    private boolean implicitEnable = true;
    private String mode = PASSIVE_MODE;

    public FtpUtil(String server, int port, String userName, String userPassword, boolean sslEnable,
                   boolean implicitEnable, String mode) {
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.userPassword = userPassword;
        this.sslEnable = sslEnable;
        this.implicitEnable = implicitEnable;
        this.mode = mode;
    }

    public static void main(String[] args) {
        FtpUtil ftp = new FtpUtil("yq01-aip-aikefu09.yq01.baidu.com", 8881, "ftpuser", "qatest", false, true,
                PASSIVE_MODE);
        ftp.open();
//        InputStream in = ftp.get("${HOME}" + "/dialogue/33350/image/111.png");
        InputStream in = ftp.get("${HOME}" + "/dialogue/33350/image/111.png");
        System.out.println("in is " + in);

//        ftp.upload(new File("/Users/zhangjiuwei/Documents/train-platform.properties"), "train-platform.properties",
//                "rule-dicts-2431-unicom/0/");

    }

    public boolean open() {
        return sslEnable ? openFtps() : openFtp();
    }

    public boolean openFtp() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        try {

            FTPClient ftpClient = new FTPClient();
            // 连接
            ftpClient.connect(this.server, this.port);
            ftpClient.login(this.userName, this.userPassword);
            setFtpClient(ftpClient);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.close();
                log.info("FTP server refused connection.");
                return false;
            }
            log.info("open FTP success:" + this.server + ";port:" + this.port + ";name:" + this
                    .userName + ";pwd:" + this.userPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置上传模式.binally  or ascii
            return true;
        } catch (Exception ex) {
            this.close();
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 连接服务器
     *
     * @return 连接成功与否 true:成功， false:失败
     */
    public boolean openFtps() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        try {
            FTPSClient ftpClient = new FTPSClient(implicitEnable);
            // 连接
            ftpClient.connect(this.server, this.port);
            ftpClient.login(this.userName, this.userPassword);
            setFtpClient(ftpClient);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.close();
                log.info("FTP server refused connection.");
                return false;
            }
            log.info("open FTP success:" + this.server + ";port:" + this.port + ";name:" + this
                    .userName + ";pwd:" + this.userPassword);
            // 设置上传模式.binally  or ascii
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            // 设置主动模式、被动模式
            if (StringUtils.equals(mode, PASSIVE_MODE)) {
                ftpClient.enterLocalPassiveMode();
                ftpClient.execPROT("P");
            } else if (StringUtils.equals(mode, ACTIVE_MODE)) {
                ftpClient.enterLocalActiveMode();
            }
            return true;
        } catch (Exception ex) {
            this.close();
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 切换到父目录
     *
     * @return 切换结果 true：成功， false：失败
     */
    private boolean changeToParentDir() {
        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 改变当前目录到指定目录
     *
     * @param dir 目的目录
     * @return 切换结果 true：成功，false：失败
     */
    private boolean cd(String dir) {
        try {
            return ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取目录下所有的文件名称
     *
     * @param filePath 指定的目录
     * @return 文件列表, 或者null
     */
    private FTPFile[] getFileList(String filePath) {
        try {
            return ftpClient.listFiles(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 层层切换工作目录
     *
     * @param ftpPath 目的目录
     * @return 切换结果
     */
    public boolean changeDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "utf-8"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "utf-8"));
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
     *
     * @param ftpPath 需要创建的目录
     * @return
     */
    public boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            log.debug("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "utf-8"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "utf-8"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "utf-8"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "utf-8"));
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param ftpFileName  上传到服务器的文件名
     * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录
     * @return
     */
    public boolean upload(File srcFile, String ftpFileName, String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        boolean flag = false;
        if (ftpClient != null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(srcFile);
                // 创建目录
                this.mkDir(ftpDirectory);
                ftpClient.setBufferSize(100000);
                ftpClient.setControlEncoding("UTF-8");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                // 上传
                flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), "utf-8"), fis);
            } catch (Exception e) {
                this.close();
                log.error(e.getMessage(), e);
                log.error("上传文件失败", e);
                return false;
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        log.info("上传文件成功： " + ftpFileName + "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
        return flag;
    }

    public boolean upload(InputStream in, String ftpFileName, String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        boolean flag = false;
        if (ftpClient != null) {
            try {
                // 创建目录
                this.mkDir(ftpDirectory);
                ftpClient.setBufferSize(100000);
                ftpClient.setControlEncoding("UTF-8");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                // 上传
                flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), StandardCharsets.UTF_8), in);
            } catch (Exception e) {
                this.close();
                log.error("上传文件失败", e);
                return false;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    log.error("close inputstream fail.", e);
                }
            }
        }
        log.info("上传文件成功： " + ftpFileName + "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
        return flag;
    }

    /**
     * 从FTP服务器上下载文件
     *
     * @param ftpDirectoryAndFileName   ftp服务器文件路径，以/dir形式开始
     * @param localDirectoryAndFileName 保存到本地的目录
     * @return
     */
    public boolean get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpDirectoryAndFileName.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpDirectoryAndFileName = sbStr.toString();
            String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
            String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
            this.changeDir(filePath);
            ftpClient.retrieveFile(new String(fileName.getBytes(), "utf-8"),
                    new FileOutputStream(localDirectoryAndFileName)); // download
            // file
            log.info(ftpClient.getReplyString()); // check result
            log.info("从ftp服务器上下载文件：" + ftpDirectoryAndFileName + "， 保存到：" + localDirectoryAndFileName);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 返回FTP目录下的文件列表
     *
     * @param pathName
     * @return
     */
    public String[] getFileNameList(String pathName) {
        try {
            return ftpClient.listNames(pathName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 删除FTP上的文件
     *
     * @param ftpDirAndFileName 路径开头不能加/，比如应该是test/filename1
     * @return
     */
    public boolean deleteFile(String ftpDirAndFileName) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.deleteFile(ftpDirAndFileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除FTP目录
     *
     * @param ftpDirectory
     * @return
     */
    public boolean deleteDirectory(String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.removeDirectory(ftpDirectory);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            log.info("成功关闭连接，服务器ip:" + this.server + ", 端口:" + this.port);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public InputStream get(String ftpDirectoryAndFileName) {
        if (!ftpClient.isConnected()) {
            return null;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpDirectoryAndFileName.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpDirectoryAndFileName = sbStr.toString();
            String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
            String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
            this.changeDir(filePath);
            return ftpClient.retrieveFileStream(new String(fileName.getBytes(), "utf-8"));
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}

