package com.example.file.dao;

import com.example.file.model.AccessModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessDao extends JpaRepository<AccessModel,Integer> {
    AccessModel findByAccessKey(String AccessKey);
}
