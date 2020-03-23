package com.example.file.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_access")
public class AccessModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accessKey;
    private String url;

    public AccessModel(String accessKey, String url) {
        this.accessKey = accessKey;
        this.url = url;
    }

    public AccessModel(String accessKey) {
        this.accessKey = accessKey;
    }
    public AccessModel() {
    }
}
