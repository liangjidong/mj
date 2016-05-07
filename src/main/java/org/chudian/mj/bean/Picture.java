package org.chudian.mj.bean;

import java.util.Date;

public class Picture {
	private Integer id;

	private Integer userId;

	private Integer videoId;

	private Integer belongUserId;

	private Integer belongGroupId;

	private String name;

	private String size;

	private Integer quality;

	private Integer similitude;
	
	private Video videoObj;

	private String url;

	private String type;

	private Integer status;

	private String keepword1;

	private String keepword2;

	private String keepword3;

	private Date createtime;
	
	private Integer threedId;
	
	public Integer getThreedId() {
		return threedId;
	}

	public void setThreedId(Integer threedId) {
		this.threedId = threedId;
	}

	public Integer getRealityType() {
		return realityType;
	}

	public void setRealityType(Integer realityType) {
		this.realityType = realityType;
	}

	private Integer realityType;

	private String trackUrl;
	
	static public String CACHE_URL = "picture_cache";
	
	static public String CACHE_SAVE_URL = "/home/onglchen/proenv/userlib/picture_cache";

	static public String GENSET_SAVE_url = "/home/onglchen/proenv/userlib/tracker";

	static public String GENSET_url = "tracker/";

	public String getTrackUrl() {
		return trackUrl;
	}

	public void setTrackUrl(String trackUrl) {
		this.trackUrl = trackUrl;
	}

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

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getBelongUserId() {
		return belongUserId;
	}

	public void setBelongUserId(Integer belongUserId) {
		this.belongUserId = belongUserId;
	}

	public Integer getBelongGroupId() {
		return belongGroupId;
	}

	public void setBelongGroupId(Integer belongGroupId) {
		this.belongGroupId = belongGroupId;
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

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getSimilitude() {
		return similitude;
	}

	public void setSimilitude(Integer similitude) {
		this.similitude = similitude;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
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

	public Video getVideoObj() {
		return videoObj;
	}

	public void setVideoObj(Video videoObj) {
		this.videoObj = videoObj;
	}
}