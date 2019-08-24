package com.aaa.lee.shiro.model;

import java.io.Serializable;

public class Permission implements Serializable {
    private Long id;

    private String permissionName;

    private String permissionChineseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getPermissionChineseName() {
        return permissionChineseName;
    }

    public void setPermissionChineseName(String permissionChineseName) {
        this.permissionChineseName = permissionChineseName == null ? null : permissionChineseName.trim();
    }
}