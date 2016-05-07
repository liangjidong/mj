package org.chudian.mj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.MjproductMapper;
import org.chudian.mj.mapper.PictureMapper;
import org.chudian.mj.mapper.VideoMapper;
import org.chudian.mj.service.MjproductService;
import org.chudian.mj.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by onglchen on 15-3-13.
 */
@Service
public class MjproductServiceImpl implements MjproductService {
	private String PICTURE_TRAIN_DIR_SERVICE = "/home/onglchen/proenv/userlib/TrainDataDir";
	public static String VIDEO_DIR="/usr/local/nginx/html/videos";

	private Log logger = LogFactory.getLog(MjproductServiceImpl.class);

	@Autowired
	private MjproductMapper mjproductMapper;
	@Autowired
	private PictureMapper pictureMapper;
	@Autowired
	private VideoMapper videoMapper;
	
	/*
	 * 将所有项目status转为已训练
	 */
	@Override
	public void updateTrainStatus(){
		mjproductMapper.updatetrainstatus();
	}
	

	/**
	 *
	 * @param
	 * @return 0 成功 1 失败
	 */
	@Override
	public int add(Mjproduct mjproduct) {
		if(mjproduct.getVideoId() == null){
			mjproduct.setVideoId(1);
		}if(mjproduct.getWebUrl() == null || mjproduct.getWebUrl().equals("")){
			mjproduct.setWebUrl("www.baidu.com");
		}
		if (mjproductMapper.insert(mjproduct) > 0) {
			logger.debug("添加魔镜产品成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	/**
	 *
	 * @param
	 * @return 0 成功 1 失败 7 不存在
	 */
	@Override
	public int update(Mjproduct mjproduct) {

		Mjproduct f = mjproductMapper.selectByPrimaryKey(mjproduct.getId());
		if (f == null) {
			logger.warn("尝试更新魔镜产品，但是魔镜产品不存在");
			return Status.NOT_EXISTS;
		}
		if (mjproductMapper.updateByPrimaryKeySelective(mjproduct) > 0) {
			logger.debug("更新魔镜产品成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	/**
	 *
	 * @param
	 * @return 0 成功 ,1 失败, 7 不存在 ,2 -- 不能删除
	 */
	@Override
    public int delete(Mjproduct mjproduct) {
        Mjproduct f = mjproductMapper.selectByPrimaryKey(mjproduct.getId());

        if (f == null) {
            logger.warn("尝试删除魔镜产品，但是魔镜产品不存在");
            return Status.NOT_EXISTS;
        }

		Video video=videoMapper.selectByPrimaryKey(f.getVideoId());
		Picture picture=pictureMapper.selectByPrimaryKey(f.getPictureId());
        if (mjproductMapper.deleteByPrimaryKey(mjproduct.getId()) > 0) {
        	try{
//        		videoMapper.deleteByPrimaryKey(f.getVideoId());
            	pictureMapper.deleteByPrimaryKey(f.getPictureId());
            	
            	//delete video file
            	String vidname=video.getName();
            	//FileUtil.deleteFile(VIDEO_DIR+"/"+vidname);
            	//delete picture file
            	String picname=picture.getName();
            	FileUtil.deleteFile(PICTURE_TRAIN_DIR_SERVICE+ "/" + picname);
        	}catch(Exception e){
        		e.printStackTrace();
				return Status.NO_DELETE;
        	}
            logger.debug("删除魔镜产品成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

	/**
	 *
	 * @param id
	 * @return file
	 */
	@Override
	public Mjproduct get(int id) {
		Mjproduct mjproduct_db = mjproductMapper.selectByPrimaryKey(id);
		if (mjproduct_db == null) {
			logger.warn("魔镜产品 ID：" + " 不存在");
		} else {
			logger.debug("魔镜产品 ID：" + " 成功");
		}
		return mjproduct_db;
	}

	/**
	 *
	 * @param queryBase
	 */
	@Override
	public void query(QueryBase queryBase) {
		if (logger.isDebugEnabled()) {
			logger.debug("根据参数 " + queryBase.getParameters() + "  查询魔镜产品");
		}

		queryBase.setResults(mjproductMapper.queryProducts(queryBase));
		queryBase.setTotalRow(mjproductMapper.countProducts(queryBase));
		System.out.println("Total Row == " + queryBase.getTotalRow());
		System.out.println("Total Page == " + queryBase.getTotalPage());
	}
	/*
	 * 
	 */
	@Override
	public void updateclicktimes(Integer id){
		mjproductMapper.updateclicktimesbyid(id);
	}

	/**
	 *   识别图片区分用户
	 * @param fileName
	 * @param path
	 * @param userId
	 * @return
	 */
	public HashMap queryARInfo(String fileName, String path, int userId){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("status","0");
		Picture picture = pictureMapper.selectByName(fileName);

		if (picture != null) {

			if(picture.getUserId()!= userId){ //用户只能识别自己的图片
				return map;
			}
			Mjproduct mjproduct = mjproductMapper.selectByPictureid(picture.getId());
			if (mjproduct != null) {
				map.put("picName", picture.getName());
				map.put("trackerUrl", path + picture.getKeepword1());
				map.put("param2", "");
				int arType = mjproduct.getArType();
				if(arType == Mjproduct.AR_TYPE_WEB){
					map.put("arType", "web");
					map.put("param", mjproduct.getWebUrl());
					map.put("status", "1");
				}else if(arType == Mjproduct.AR_TYPE_VIDEO_WEB){
					map.put("arType", "webVideo");
					map.put("param", mjproduct.getWebVideoUrl());
					map.put("status", "1");
				}else if(arType == Mjproduct.AR_TYPE_VIDEO){
					if (mjproduct.getVideoObj() != null) {
						Video video = picture.getVideoObj();
						map.put("arType", "video");
						map.put("param", path + video.getFinalUrl());
						map.put("status", "1");
					}
				}

			}
		}
		return map;
	}

	/**
	 * 		识别图片不区分用户
	 * @param fileName
	 * @param path
	 * @return
	 */
	public HashMap queryARInfo(String fileName, String path){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("status","0");
		Picture picture = pictureMapper.selectByName(fileName);

		if (picture != null) {


			Mjproduct mjproduct = mjproductMapper.selectByPictureid(picture.getId());
			if (mjproduct != null) {
				map.put("picName", picture.getName());
				map.put("trackerUrl", path + picture.getKeepword1());
				map.put("param2", "");
				int arType = mjproduct.getArType();
				if(arType == Mjproduct.AR_TYPE_WEB){
					map.put("arType", "web");
					map.put("param", mjproduct.getWebUrl());
					map.put("status", "1");
				}else if(arType == Mjproduct.AR_TYPE_VIDEO_WEB){
					map.put("arType", "webVideo");
					map.put("param", mjproduct.getWebVideoUrl());
					map.put("status", "1");
				}else if(arType == Mjproduct.AR_TYPE_VIDEO){
					if (mjproduct.getVideoObj() != null) {
						Video video = mjproduct.getVideoObj();
						map.put("arType", "video");
						map.put("param", path + video.getFinalUrl());
						map.put("status", "1");
					}
				}

			}
		}
		return map;
	}
}