package com.fjs.erp.datasource.entities;

import java.util.Date;

public class Tenant {
    private Long id;

    private Long tenantId;

    private String loginName;

    private Integer userNumLimit;

    private Integer billsNumLimit;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public Integer getUserNumLimit() {
        return userNumLimit;
    }

    public void setUserNumLimit(Integer userNumLimit) {
        this.userNumLimit = userNumLimit;
    }

    public Integer getBillsNumLimit() {
        return billsNumLimit;
    }

    public void setBillsNumLimit(Integer billsNumLimit) {
        this.billsNumLimit = billsNumLimit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}