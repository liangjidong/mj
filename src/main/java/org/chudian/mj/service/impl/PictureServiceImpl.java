package org.chudian.mj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.Match;
import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.jni.GenAssert;
import org.chudian.mj.mapper.MjproductMapper;
import org.chudian.mj.mapper.PictureMapper;
import org.chudian.mj.mapper.ThreeDMapper;
import org.chudian.mj.mapper.VideoMapper;
import org.chudian.mj.service.PictureService;
import org.chudian.mj.service.UserService;
import org.chudian.mj.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by onglchen on 15-3-13.
 */
@Service
public class PictureServiceImpl implements PictureService {

	private Log logger = LogFactory.getLog(PictureServiceImpl.class);

	@Autowired
	private VideoMapper videoMapper;
	@Autowired
	private PictureMapper pictureMapper;
	@Autowired
	private MjproductMapper mjproductMapper;
	@Autowired
	private ThreeDMapper threeDMapper;
	@Autowired
	private UserService userService;

	private String TRAIN_DIR = "/home/onglchen/proenv/userlib/TrainDataDir";
	private String UPLOAD_DIR = "/home/onglchen/proenv/userlib/upload/";
	private String PICTURE_UPLOAD_DIR_SERVICE = "/usr/local/nginx/html";
	private String PICTURE_TRAIN_DIR_SERVICE = "/home/onglchen/proenv/userlib/TrainDataDir";
	private String TOMCAT_PICTURE_PATH = "pictures";

	/**
	 *
	 * @param picture
	 * @return 0 鎴愬姛 1 澶辫触
	 */
	@Override
	public int add(Picture picture) {
		if (pictureMapper.insert(picture) > 0) {
			logger.debug("娣诲姞鏂囦欢鎴愬姛");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	/**
	 *
	 * @param picture
	 * @return 0 鎴愬姛 1 澶辫触 7 涓嶅瓨鍦�
	 */
	@Override
	public int update(Picture picture) {
		int status;
		Picture f = pictureMapper.selectByPrimaryKey(picture.getId());
		// 用pic id查找对应产品，修改其视频id
		Mjproduct mjproduct = mjproductMapper.selectByPictureid(f.getId());
		if (f == null) {
			logger.warn("尝试更新图片，但是图片不存在");
			return Status.NOT_EXISTS;
		}
		if (mjproduct == null) {
			if (videoMapper.selectByPrimaryKey(picture.getVideoId()) == null) {
				return Status.ERROR;
			}
			Mjproduct mjproduct_new = new Mjproduct();
			mjproduct_new.setPictureId(f.getId());
			mjproduct_new.setVideoId(picture.getVideoId());
			mjproduct_new.setUserId(picture.getUserId());
			mjproduct_new.setCreatetime(new Date());
			mjproduct_new.setDescription("just for test");
			mjproduct_new.setTitle("test");
			mjproduct_new.setStatus(1);
			mjproduct_new.setKeepword1("www.baidu.com");
			mjproduct_new.setClicktimes(0);
			if (mjproductMapper.insert(mjproduct_new) > 0) {
				logger.debug("添加魔镜产品成功");
				status = Status.SUCCESS;
			}
			status = Status.ERROR;
			// create picture only and edit its picture information about video

			logger.warn("尝试更新图片，但是产品不存在");
			return Status.NOT_EXISTS;
		}
		if (pictureMapper.updateByPrimaryKeySelective(picture) > 0) {
			mjproduct.setVideoId(picture.getVideoId());
			mjproductMapper.updateByPrimaryKey(mjproduct);
			logger.debug("更新图片成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	/**
	 *
	 * @param picture
	 * @return 0 成功 1 失败 7 不存在
	 */
	@Override
	public int updateOnly(Picture picture) {
		Picture f = pictureMapper.selectByPrimaryKey(picture.getId());
		if (f == null) {
			logger.warn("尝试更新图片，但是图片不存在");
			return Status.NOT_EXISTS;
		}
		if (pictureMapper.updateByPrimaryKeySelective(picture) > 0) {
			logger.debug("更新图片成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	/**
	 *
	 * @param picture
	 * @return 0 成功 1 失败 7 不存在 3 不能删除（存在关联产品）
	 */
	@Override
	public int delete(Picture picture) {
		Picture f = pictureMapper.selectByPrimaryKey(picture.getId());
		if (f == null) {
			logger.warn("尝试删除图片，但是文图片不存在");
			return Status.NOT_EXISTS;
		}
		if (mjproductMapper.isExistsByPictureId(f.getId())) {
			return Status.NO_DELETE;
		}
		if (pictureMapper.deleteByPrimaryKey(f.getId()) > 0) {
			String picname = f.getName();
			FileUtil.deleteFile(PICTURE_TRAIN_DIR_SERVICE + "/" + picname);
			logger.debug("删除图片成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	public int copyPictureToTrainWeb(String srcFile, String fileName,
			Picture picture) {
		// String oldPath = this.UPLOAD_DIR_SERVICE + "/" + fileName;
		String oldPath = srcFile;
		String rename = FileUtil.rename(fileName);
		String newPath2 = TOMCAT_PICTURE_PATH + "/" + rename;
		String newPath = this.TRAIN_DIR + "/" + rename;
		int status = 0;
		status = genGrayJpgTreed(oldPath, rename, picture);
		FileUtil.Copy(oldPath, newPath);
		// System.out.println("路径问题");
		// FileUtil.renameFile(UPLOAD_DIR_SERVICE, fileName, rename);
		// picture.setRealityType(1);
		// picture.setThreedId(threeD.getId());
		picture.setName(rename);
		picture.setUrl(newPath2);
		return status;
	}

	public int moveToPictureToTrain(String srcFile, String fileName,
			Picture picture) {
		// String oldPath = this.UPLOAD_DIR + "/" + fileName;

		String oldPath = srcFile;
		String rename = FileUtil.rename(fileName);
		String newPath2 = TOMCAT_PICTURE_PATH + "/" + rename;
		String newPath = this.TRAIN_DIR + "/" + rename;
		System.out.println("Mvoe File ++++++++++++++");
		genGrayJpg(oldPath, rename);
		FileUtil.move(oldPath, newPath);

		// FileUtil.renameFile(UPLOAD_DIR_SERVICE, fileName, rename);

		picture.setName(rename);
		picture.setUrl(newPath2);
		return 1;

	}

	/**
	 * 生成灰度图，3d模型
	 *
	 * @param srcurl
	 * @param fileName
	 * @return
	 */

	public int genGrayJpgTreed(String srcurl, String fileName, Picture picture) {
		int status;
		String fileName_gray = fileName + ".jpg";
		String gensetDir = Picture.GENSET_SAVE_url + "/"
				+ FileUtil.getNameWithoutExtensions(fileName) + ".asset";
		Match match = new Match();
		String desurl = Picture.CACHE_SAVE_URL + "/" + fileName_gray;
		status = 1;
		// status = match.genGrayJpg_java(srcurl, desurl);
		// Genset.getInstance().pictureGenset(desurl, gensetDir + "/model" );

		// try {
		// ZipUtil2.zip(gensetDir,FileUtil.getBackPath(gensetDir),FileUtil.getNameWithoutExtensions(fileName)
		// + ".zip" );
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ZipUtil.compress(gensetDir, gensetDir + ".zip");
		// FileUtil.delAllFile(gensetDir);
		// FileUtil.deleteFile(gensetDir);

		// threeD.setUrl(gensetDir + ".zip");
		// threeD.setName(fileName + "zip");
		// threeDMapper.insert(threeD);

		if (status == 1) {
			GenAssert.Train(srcurl, gensetDir, "test");
			picture.setKeepword1(Picture.GENSET_url + "/"
					+ FileUtil.getNameWithoutExtensions(fileName) + ".asset");
			picture.setTrackUrl(Picture.GENSET_url + "/"
					+ FileUtil.getNameWithoutExtensions(fileName) + ".asset");
			System.out.println("杞垚鐏板害鍥炬垚鍔");
			return Status.SUCCESS;
		} else {
			System.out.println("杞伆搴﹀浘澶辫触");
			return Status.ERROR;
		}

	}

	public int genGrayJpg(String srcurl, String fileName) {
		int status;
		String fileName_gray = fileName + ".jpg";
		String gensetDir = Picture.GENSET_SAVE_url + "/" + fileName;
		Match match = new Match();
		String desurl = Picture.CACHE_SAVE_URL + "/" + fileName_gray;
		status = match.genGrayJpg_java(srcurl, desurl);

		if (status == 1) {
			System.out.println("杞垚鐏板害鍥炬垚鍔");
			return Status.SUCCESS;
		} else {
			System.out.println("杞伆搴﹀浘澶辫触");
			return Status.ERROR;
		}

	}

	public int copyPictureToTrain(String fileName, Picture picture) {
		String oldPath = this.PICTURE_UPLOAD_DIR_SERVICE + "/" + fileName;
		String rename = FileUtil.rename(fileName);
		String newPath2 = TOMCAT_PICTURE_PATH + "/" + rename;
		String newPath = this.TRAIN_DIR + "/" + rename;
		genGrayJpgTreed(oldPath, rename, picture);
		FileUtil.Copy(oldPath, newPath);

		// FileUtil.renameFile(PICTURE_UPLOAD_DIR_SERVICE, fileName, rename);
		// picture.setRealityType(1);
		// picture.setThreedId(threeD.getId());
		picture.setName(rename);
		picture.setUrl(newPath2);
		return this.add(picture);

	}

	/**
	 *
	 * @param id
	 * @return file
	 */
	@Override
	public Picture get(int id) {
		Picture picture = pictureMapper.selectByPrimaryKey(id);
		if (picture == null) {
			logger.warn("鏂囦欢 ID锛" + " 涓嶅瓨鍦");
		} else {
			logger.debug("鏂囦欢 ID锛" + " 鎴愬姛");
		}
		return picture;
	}

	/**
	 *
	 * @param fileName
	 * @return file
	 */
	@Override
	public Picture getByName(String fileName) {

		Picture picture = pictureMapper.selectByName(fileName);
		if (picture == null) {
			logger.warn("图片名为：" + fileName + " 的图片不存在");
		} else {
			logger.debug("查找图片：" + fileName + " 成功");
		}
		return picture;
	}

	/**
	 *
	 * @param queryBase
	 */
	@Override
	public void query(QueryBase queryBase) {
		if (logger.isDebugEnabled()) {
			logger.debug("根据参数： " + queryBase.getParameters() + " 查询图片");
		}
		queryBase.setResults(pictureMapper.queryPictures(queryBase));
		queryBase.setTotalRow(pictureMapper.countPictures(queryBase));
	}

	@Override
	public int updateonly(Picture picture) {
		Picture f = pictureMapper.selectByPrimaryKey(picture.getId());
		if (f == null) {
			logger.warn("尝试更新图片，但是图片不存在");
			return Status.NOT_EXISTS;
		}
		if (pictureMapper.updateByPrimaryKeySelective(picture) > 0) {
			Mjproduct product = new Mjproduct();
			product.setClicktimes(0);
			product.setCreatetime(new Date());
			product.setUserId(f.getUserId());
			product.setVideoId(f.getVideoId());
			product.setPictureId(f.getId());
			product.setKeepword1("www.baidu.com");
			mjproductMapper.insert(product);
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	public HashMap getVidioByPicName(String fileName, String path) {
		HashMap<String, String> map = new HashMap<String, String>();
		Picture picture = pictureMapper.selectByName(fileName);
		if (picture != null) {

			if (picture.getVideoObj() != null) {
				Video video = picture.getVideoObj();
				map.put("videoUrl", video.getFinalUrl());
			}
		}
		return map;
	}

	public HashMap queryARInfo(String fileName, String path) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("status", "0");
		Picture picture = pictureMapper.selectByName(fileName);
		if (picture != null) {

			if (picture.getVideoObj() != null) {
				Video video = picture.getVideoObj();
				map.put("picName", picture.getName());
				map.put("trackerUrl", path + picture.getKeepword1());
				map.put("arType", "video");
				map.put("param", path + video.getFinalUrl());
				map.put("param2", "");
				map.put("status", "1");
			}
		}
		return map;
	}

	public boolean isPicturExist() {

		return true;

	}
}