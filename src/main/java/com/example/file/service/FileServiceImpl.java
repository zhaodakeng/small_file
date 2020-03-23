package com.example.file.service;

import com.example.file.dao.FileDao;
import com.example.file.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhaojf
 * @Title: 文件数据库
 * @Description: ${todo}
 * @date 2018/8/2214:39
 */
@Service
public class FileServiceImpl {
    @Autowired
    FileDao fileDao;
    /**
     * 通过个人ID获取到文件
     * @param PersonalId 个人ID
     * @return
     */
    public List<FileModel> PersonalId(Integer PersonalId) {
        return fileDao.findByPersonalIdOrderBySortAsc(PersonalId);
    }
    /**
     * 通过包ID获取到文件
     * @param Packageid
     * @return
     */
    public List<FileModel> findPackageid(String Packageid) {
        return fileDao.findByPackageidOrderBySortAsc(Packageid);
    }
    /**
     * id和包ID 匹配数据
     * @param uuidname
     * @param Packageid
     * @return
     */
    public FileModel findPackageidAndUuidname(String Packageid,String uuidname) {
        List<FileModel> fs=fileDao.findByPackageidAndUuidnameOrderBySortAsc(Packageid,uuidname);
        if(fs.size()>0)
        {
            return fs.get(0);

        }else{
            return null;
        }
    }

    public FileModel findOne(Integer id) {
        return fileDao.findById(id).get();
    }

    /**
     * 通过UUid获取
     * @param uuid
     * @return
     */
    public FileModel findUuid(String uuid) {
        List<FileModel> tjrFileModel = fileDao.findByUuidnameOrderBySortAsc(uuid);
        if (tjrFileModel.size()>0){
            return tjrFileModel.get(0);
        }
        return null;
    }

    /**
     * 通过uuidname获取数据
     * @param uuidname
     * @return
     */
    public FileModel findUuidname(String uuidname){
        return fileDao.findByUuidname(uuidname);
    }

    public FileModel save(FileModel model) {
        return fileDao.save(model);
    }

    public void del(Integer id) {
        fileDao.deleteById(id);
    }
}
