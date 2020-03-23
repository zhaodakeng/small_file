package com.example.file.controller;

import com.example.file.config.LoginRequired;
import com.example.file.model.FileModel;
import com.example.file.service.FileServiceImpl;
import com.example.file.tool.Img2Base64Util;
import com.example.file.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhaojf
 * @Title: 上传接口
 * @Description: 上传文件统一接口
 * @date 2018/8/2210:11
 */
@Controller
@RequestMapping(value = {"", "file"})
public class FileController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    FileServiceImpl fileService;
    @Autowired
    HttpServletRequest request;
    /**
     * 获取UUid
     *
     * @return
     */
    @GetMapping("uuid")
    @ResponseBody
    public String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 包 文件列表
     *
     * @param paid
     * @return
     */
    @LoginRequired
    @GetMapping("filelist/{packageid}")
    @ResponseBody
    public List<FileModel> fileList(@PathVariable(name = "packageid") String paid) {
        return fileService.findPackageid(paid);
    }
    @LoginRequired
    @GetMapping("test")
    @ResponseBody
    public Object test() {
        request.getHeader("accesstoken");
        return Tool.getPath();
    }

    /**
     * 实现文件上传
     * packageid 包ID
     * describe 备注
     */
    @LoginRequired
    @RequestMapping(value = {"", "fileUpload"})
    @ResponseBody
    public FileModel fileUpload(@RequestParam("file") MultipartFile file,
                                @RequestParam(required = false, defaultValue = "") String packageid,
                                @RequestParam(required = false, defaultValue = "") String describe,
                                @RequestParam(required = false, defaultValue = "0") Integer sort) {
        if (file.isEmpty()) {
            return null;
        }
        //文件名
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        log.info(fileName + "-->" + size);
        Tool.newFileData newFileData = Tool.newFilePath(request,fileName);
        File dest = new File(newFileData.path);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        FileModel fileModel = new FileModel();
        //包ID
        fileModel.setPackageid(packageid.equals("") ? newFileData.uuid + System.currentTimeMillis() : packageid);
        //备注
        fileModel.setRemark(describe);
        fileModel.setSort(sort);
        //上传日期 时间戳
        fileModel.setDt1(System.currentTimeMillis());
        //文件名
        fileModel.setFilename(fileName);
        //路径
        fileModel.setPath(newFileData.path);
        //uuidname
        fileModel.setUuidname(newFileData.fileName);
        try {
            //保存文件
            file.transferTo(dest);
            return fileService.save(fileModel);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传base 64
     *
     * @return
     */
    @LoginRequired
    @PostMapping("base")
    @ResponseBody
    public FileModel base(@RequestBody Map<String, Object> reqMap) {
        String base = reqMap.get("base").toString();
        String packageid = "", describe = "";
        Integer sort = 0;
        //验证包ID
        if (reqMap.get("packageid") != null) {
            packageid = reqMap.get("packageid").toString();
        }
        //备注
        if (reqMap.get("describe") != null) {
            describe = reqMap.get("describe").toString();
        }
        //排序
        if (reqMap.get("sort") != null) {
            sort = Integer.valueOf(reqMap.get("describe").toString());
        }
        if (base.isEmpty()) {
            return null;
        }
        String uuidname = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        FileModel fileModel = new FileModel();
        //包ID
        fileModel.setPackageid(packageid.equals("") ? uuidname + System.currentTimeMillis() : packageid);
        //备注
        fileModel.setRemark(describe);
        //上传日期 时间戳
        fileModel.setDt1(System.currentTimeMillis());
        //文件名
        fileModel.setFilename("");
        fileModel.setSort(sort);
        //路径
        fileModel.setPath(Tool.getPath());
        //uuidname
        fileModel.setUuidname(uuidname + ".jpg");
        Img2Base64Util.generateImage(base, Tool.getPath() + uuidname + ".jpg");
        return fileService.save(fileModel);
    }

    /**
     * 修改排序
     * 接口使用UUidname作为特征查询
     * @param fileModels
     * @return
     */
    @LoginRequired
    @PostMapping(value = "updateSorts", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String updateSort(@RequestBody List<FileModel> fileModels) {
        fileModels.forEach(fileModel -> {
            FileModel model = fileService.findUuidname(fileModel.getUuidname());
            model.setSort(fileModel.getSort());
            fileService.save(model);
        });
        return "修改成功";
    }

    /**
     * 修改排序
     * @param hashMap
     * @return
     */
    @LoginRequired
    @PostMapping("updateSort")
    @ResponseBody
    public String updateSort(@RequestBody HashMap<String, Object> hashMap) {
        FileModel fileModel = fileService.findUuidname(hashMap.get("uuidname").toString());
        fileModel.setSort(Integer.valueOf(hashMap.get("sort").toString()));
        fileService.save(fileModel);
        return "修改成功";

    }

    /**
     * 实现多文件上传
     * 在系统中可以不用，只用单个文件上传就好了，lay机制只会一个一个上传
     */
    @LoginRequired
    @RequestMapping(value = "multifileUpload", method = RequestMethod.POST)
    @ResponseBody
    public String multifileUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");

        if (files.isEmpty()) {
            return "false";
        }
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

            if (file.isEmpty()) {
                return "false";
            } else {
                File dest = new File(Tool.getPath()+ "/" + fileName);
                if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "true";
    }

    @RequestMapping("download.htm")
    public String downLoad(HttpServletResponse response, @RequestParam Integer id) throws UnsupportedEncodingException {
        FileModel tjrFile = fileService.findOne(id);
        //文件名
        String filename = tjrFile.getFilename();
        File file = new File(tjrFile.getPath());
        return downloadTool(response, file, filename);
    }

    @RequestMapping("downloadName.htm")
    public String downloadName(HttpServletResponse response, @RequestParam String name) throws UnsupportedEncodingException {
        File file = new File(Tool.getPath() + name);
        return downloadTool(response, file, name);
    }

    /**
     * 下载
     *
     * @param response
     * @param file     文件流
     * @param name     文件名
     * @return
     */
    public String downloadTool(HttpServletResponse response, File file, String name) throws UnsupportedEncodingException {
        //判断文件父目录是否存在
        if (file.exists()) {
            response.setContentType("application/force-download");
            //修改下载文件的文件名
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(name.getBytes("gb2312"), "ISO8859-1"));

            byte[] buffer = new byte[1024];
            //文件输入流
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            //输出流
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.info("=========================文件下载:" + name);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除
     * 判断uuid是否存在 如果不存在则吧包下所有图片删除
     *
     * @param reqMap
     * @return
     */
    @LoginRequired
    @PostMapping("del")
    @ResponseBody
    public String delfile(@RequestBody Map<String, Object> reqMap) {
        String packageid = reqMap.get("packageid").toString();
        var uuidname = reqMap.get("uuidname");
        FileModel fileModel;
        List<FileModel> fileModels;
        String bool = "true";
        if (uuidname == null) {
            fileModels = fileService.findPackageid(packageid);
            for (int i = 0; i < fileModels.size(); i++) {
                if (!del(fileModels.get(i))) {
                    bool = "false";
                }
            }
        } else {
            fileModel = fileService.findPackageidAndUuidname(packageid, uuidname.toString());
            del(fileModel);
        }
        return bool;
    }

    public Boolean del(FileModel fileModel) {
        try {
            fileService.del(fileModel.getId());
            File file = new File(Tool.getPath() + fileModel.getUuidname());
            if (file.delete()) {
                log.info(file.getName() + "is deleted");
                return true;
            } else {
                log.info("Delete failed.");
                return false;
            }
        } catch (Exception e) {
            log.info("Exception occured");
            e.printStackTrace();
            return false;
        }
    }


}
