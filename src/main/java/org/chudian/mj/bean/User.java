package org.chudian.mj.bean;

public class User {

    public static int ROLE_USER = 1;

    public static int ROLE_ADMIN = 2;


    private Integer id;

    private Integer groupId;

    private String headImg;

    private String phoneNumber;

    private String nickName;

    private String name;

    private String adress;

    private String password;

    private Integer role;

    private Integer status;

    private String paymentInformation;

    private String qqNumber;

    private String keepword1;

    private String keepword2;

    private String email;

    private String keepword3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress == null ? null : adress.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation == null ? null : paymentInformation.trim();
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber == null ? null : qqNumber.trim();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getKeepword3() {
        return keepword3;
    }

    public void setKeepword3(String keepword3) {
        this.keepword3 = keepword3 == null ? null : keepword3.trim();
    }
}