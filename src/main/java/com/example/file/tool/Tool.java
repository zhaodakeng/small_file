package com.example.file.tool;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;


public class Tool {
    /**
     * 获取jar所在路径
     * @return
     */
    public static String getPath() {
        String path = Tool.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
            //判断需要剪切长度 开发环境和编译后环境有不同并且兼容windows和linux
            path = path.substring(path.indexOf("file:")>-1?5:1,path.length());
            if(path.contains("jar"))
            {
                path = path.substring(0,path.lastIndexOf("."));
                return path.substring(0,path.lastIndexOf("/"));
            }
            return path.replace("target/classes/", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 获取一个新的文件地址
     * @param fileName 文件名
     * @return [UUID,新的文件地址]
     */
    public static newFileData newFilePath(HttpServletRequest request,String fileName){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        //地址拼接
        String path = Tool.getPath() +
                "file" +
                System.getProperty("file.separator") +
                request.getHeader("AccessKey") +
                System.getProperty("file.separator") +
                c.get(Calendar.YEAR) +
                "-" +
                c.get(Calendar.MONTH) + 1 +
                System.getProperty("file.separator")  +
                c.get(Calendar.DAY_OF_MONTH) +
                System.getProperty("file.separator") + uuid + "_" + fileName;
        return new newFileData(uuid,path,uuid + "_" + fileName);
    }

    public static class newFileData{
        public String uuid;
        public String path;
        public String fileName;

        public newFileData(String uuid, String path, String fileName) {
            this.uuid = uuid;
            this.path = path;
            this.fileName = fileName;
        }

        public newFileData() {
        }
    }
}


