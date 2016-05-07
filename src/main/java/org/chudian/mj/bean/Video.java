package org.chudian.mj.bean;

import java.util.Date;

public class Video {
    private Integer id;

    private Integer userId;

    private String name;

    private String size;

    private String originUrl;

    private String finalUrl;

    private String videoFormat;

    private String audioFormat;

    private String uploadWay;

    private String codeRate;

    private String duration;

    private Integer status;

    private String keepword1;

    private String keepword2;

    private String keepword3;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl == null ? null : originUrl.trim();
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl == null ? null : finalUrl.trim();
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat == null ? null : videoFormat.trim();
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat == null ? null : audioFormat.trim();
    }

    public String getUploadWay() {
        return uploadWay;
    }

    public void setUploadWay(String uploadWay) {
        this.uploadWay = uploadWay == null ? null : uploadWay.trim();
    }

    public String getCodeRate() {
        return codeRate;
    }

    public void setCodeRate(String codeRate) {
        this.codeRate = codeRate == null ? null : codeRate.trim();
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}