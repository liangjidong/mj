package org.chudian.mj.bean;

public class Gruop {
    private Integer id;

    private String groupName;

    private Integer permission;

    private Integer status;

    private String keepword1;

    private String keepword2;

    private String keepword3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeepword1() {
        return keepword1;
    }

    public void setKeepword1(String keepword1) {
        this.keepword1 = keepword1 == null ? null : keepword1.trim();
    }

    public String getKeepword2() {
        return keepword2;
    }

    public void setKeepword2(String keepword2) {
        this.keepword2 = keepword2 == null ? null : keepword2.trim();
    }

    public String getKeepword3() {
        return keepword3;
    }

    public void setKeepword3(String keepword3) {
        this.keepword3 = keepword3 == null ? null : keepword3.trim();
    }
}