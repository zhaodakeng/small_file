package com.example.file.controller;

import com.example.file.dao.AccessDao;
import com.example.file.model.AccessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * 密钥
 * @author zjf
 */
@Controller
@RequestMapping("access")
public class AccessController {
    @Autowired
    AccessDao accessDao;

    @GetMapping("add")
    @ResponseBody
    public Boolean add(){
        String url = "127.0.0.1,localhost";
        AccessModel accessModel = new AccessModel(UUID.randomUUID().toString().replace("-", "").toLowerCase(),url);
        return accessDao.save(accessModel)!=null;
    }
}
