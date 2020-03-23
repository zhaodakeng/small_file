package com.example.file.dao;

import com.example.file.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhaojf
 * @Title: 文件上传
 * @Description: ${todo}
 * @date 2018/8/2211:50
 */
public interface FileDao extends JpaRepository<FileModel,Integer> {
    /**
     * 通过个人ID获取到文件
     * @param PersonalId 个人ID
     * @return
     */
    List<FileModel> findByPersonalIdOrderBySortAsc(Integer PersonalId);

    /**
     * 通过包ID获取到文件
     * @param Packageid
     * @return
     */
    List<FileModel> findByPackageidOrderBySortAsc(String Packageid);

    /**
     * uuidname和包ID 匹配数据
     * @param uuidname
     * @param Packageid
     * @return
     */
    List<FileModel> findByPackageidAndUuidnameOrderBySortAsc(String Packageid,String uuidname);

    /**
     * 通过UUID查询文件信息
     * @param uuid
     * @return
     */
    List<FileModel> findByUuidnameOrderBySortAsc(String uuid);
    FileModel findByUuidname(String uuidname);
}
