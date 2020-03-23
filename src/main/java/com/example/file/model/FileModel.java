package com.example.file.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 文件上传
 */
@Data
@Entity
@Table(name = "t_file")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //包id
    private String packageid;
    //上传人ID
    private Integer personalId;
    //uuid name
    private String uuidname;
    //文件名
    private String filename;
    //路径
    private String path;
    //上传时间
    private Long dt1;
    //备注
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
}
