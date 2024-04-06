package com.fjs.erp.datasource.entities;
import lombok.Data;

@Data
public class User {
    private Long id;

    private String username;

    private String loginame;

    private String password;

    private String position;

    private String department;

    private String email;

    private String phonenum;

    private Byte ismanager;

    private Byte isystem;

    private Byte status;

    private String description;

    private String remark;

    private Long tenantId;

}
