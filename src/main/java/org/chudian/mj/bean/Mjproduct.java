package org.chudian.mj.bean;

import java.util.Date;

public class Mjproduct {
	private Integer id;

	private Integer pictureId;

	private Picture pictureObj;

	private Integer videoId;
	
	private Video videoObj;

	private Integer audioId;

	private Audio audioObj;

	private Integer userId;

	private String industry;

	private Integer isPrivate;

	private String fetchCode;

	private Integer productionPhase;

	private Integer status;

	private Date makeTime;

	private Date publishTime;

	private String description;

	private String keepword1;

	private String keepword2;

	private String keepword3;

	private String title;

	private Date createtime;

	private Integer clicktimes;

	private Integer threedId;

	private Integer arType; //ar类型, 1--网页, 2--自传视频, 3--网络视频, 4--3d

	public static int AR_TYPE_WEB = 1;
	public static int AR_TYPE_VIDEO = 2;
	public static int AR_TYPE_VIDEO_WEB = 3;
	public static int AR_TYPE_3D = 4;


	private String webUrl;

	private String webVideoUrl;

	public Integer getAudioId() {
		return audioId;
	}

	public void setAudioId(Integer audioId) {
		this.audioId = audioId;
	}

	public Audio getAudioObj() {
		return audioObj;
	}

	public void setAudioObj(Audio audioObj) {
		this.audioObj = audioObj;
	}

	public String getWebVideoUrl() {
		return webVideoUrl;
	}

	public void setWebVideoUrl(String webVideoUrl) {
		this.webVideoUrl = webVideoUrl;
	}

	public Integer getThreedId() {
		return threedId;
	}

	public void setThreedId(Integer threedId) {
		this.threedId = threedId;
	}

	public Integer getArType() {
		return arType;
	}

	public void setArType(Integer arType) {
		this.arType = arType;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
	}

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry == null ? null : industry.trim();
	}

	public Integer getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Integer isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getFetchCode() {
		return fetchCode;
	}

	public void setFetchCode(String fetchCode) {
		this.fetchCode = fetchCode == null ? null : fetchCode.trim();
	}

	public Integer getProductionPhase() {
		return productionPhase;
	}

	public void setProductionPhase(Integer productionPhase) {
		this.productionPhase = productionPhase;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(Date makeTime) {
		this.makeTime = makeTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getClicktimes() {
		return clicktimes;
	}

	public void setClicktimes(Integer clicktimes) {
		this.clicktimes = clicktimes;
	}

	public Picture getPictureObj() {
		return pictureObj;
	}

	public void setPictureObj(Picture pictureObj) {
		this.pictureObj = pictureObj;
	}

	public Video getVideoObj() {
		return videoObj;
	}

	public void setVideoObj(Video videoObj) {
		this.videoObj = videoObj;
	}
}