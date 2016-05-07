package org.mj.mapper;

import org.chudian.mj.bean.Match;
import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.common.Status;
import org.chudian.mj.jni.Genset;
import org.chudian.mj.mapper.MjproductMapper;
import org.chudian.mj.mapper.PictureMapper;
import org.chudian.mj.utils.FileUtil;
import org.chudian.mj.utils.ZipUtil2;
import org.junit.Test;

import java.io.File;
import java.util.List;



/**
 * @project xhomeweb
 * @file  org.xhome.web.mapper
 * @author longmaojing
 * @email 619692439@qq.com
 * @data 2013-11-17
 * @descrption
 */
/*
*//**
 * @project xhomeweb
 * @file  org.xhome.web.mapper
 * @author longmaojing
 * @email 619692439@qq.com
 * @data 2013-11-17
 * @descrption
 */

public class PictureMapperTest extends AbstractTest {
	List<Picture> pictures = null;
	Picture picture = null;
	PictureMapper pictureMapper = null;
	MjproductMapper mjproductMapper = null;
	String baseUrl ;
	String TRAIN_DIR;



	public PictureMapperTest() {
		super();
		//pictureMapper = (PictureMapper) context.getBean("pictureMapper");

		 picture = new Picture();
		picture.setUserId(1);
		picture.setVideoId(1);
		picture.setStatus(1);
		baseUrl = "/files";
		TRAIN_DIR = "/home/onglchen/proenv/userlib/TrainDataDir";
	}
	@Test
	public void testadd(){
		File baseFile = new File(TRAIN_DIR);
		File[] files;
		File file;
		files = baseFile.listFiles();
		String fileName;
		for(int i = 0; i < files.length; i++){
			file = files[i];
			fileName = file.getName();
			picture.setName(fileName);
			picture.setUrl(baseUrl + "/" + fileName);
			picture.setType(FileUtil.getExtensions(fileName));
			picture.setSize(FileUtil.FormetFileSize((int) file.length()));
			picture.setVideoId(63);
			genGrayJpgTreed(file.getAbsolutePath(), fileName, picture);
			pictureMapper.insert(picture);
			Mjproduct mjproduct = new Mjproduct();
			mjproduct.setPictureId(picture.getId());
			mjproduct.setVideoId(63);
			mjproductMapper.insert(mjproduct);

			System.out.println("picture " + i + " == " +  picture.getName() + ";" + picture.getUrl() + ";" + picture.getType() + ";" + picture.getSize() + ";");
		}

	}


//
//	public int copyPictureToTrainWeb(String srcFile, String fileName,
//									 Picture picture) {
//		// String oldPath = this.UPLOAD_DIR_SERVICE + "/" + fileName;
//		String oldPath = srcFile;
//		String rename = FileUtil.rename(fileName);
//		String newPath2 = TOMCAT_PICTURE_PATH + "/" + rename;
//		String newPath = this.TRAIN_DIR + "/" + rename;
//		ThreeD threeD = new ThreeD();
//		genGrayJpgTreed(oldPath, rename, picture);
//		FileUtil.Copy(oldPath, newPath);
////		System.out.println("路径问题");
//		// FileUtil.renameFile(UPLOAD_DIR_SERVICE, fileName, rename);
//		//picture.setRealityType(1);
//		//	picture.setThreedId(threeD.getId());
//		picture.setName(rename);
//		picture.setUrl(newPath2);
//		return 1;
//	}



	/**	生成灰度图，3d模型
	 *
	 * @param srcurl
	 * @param fileName
	 * @return
	 */


	public int genGrayJpgTreed(String srcurl, String fileName, Picture picture) {
		int status;
		String fileName_gray = fileName + ".jpg";
		String gensetDir = Picture.GENSET_SAVE_url + "/" + FileUtil.getNameWithoutExtensions(fileName);
		Match match = new Match();
		String desurl = Picture.CACHE_SAVE_URL + "/" + fileName_gray;
		status = match.genGrayJpg_java(srcurl, desurl);
		Genset.getInstance().pictureGenset(desurl, gensetDir + "/model" );
		try {
			ZipUtil2.zip(gensetDir, FileUtil.getBackPath(gensetDir), FileUtil.getNameWithoutExtensions(fileName) + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ZipUtil.compress(gensetDir, gensetDir + ".zip");
		FileUtil.delAllFile(gensetDir);
		FileUtil.deleteFile(gensetDir);

		picture.setKeepword1(Picture.GENSET_url + "/" + FileUtil.getNameWithoutExtensions(fileName) + ".zip" );
//		threeD.setUrl(gensetDir + ".zip");
//		threeD.setName(fileName + "zip");
//		threeDMapper.insert(threeD);

		if (status == 1) {
			System.out.println("杞垚鐏板害鍥炬垚鍔");
			return Status.SUCCESS;
		} else {
			System.out.println("杞伆搴﹀浘澶辫触");
			return Status.ERROR;
		}

	}
    
	/**
	 *@Title  testadduser 
	 *@description 
	 */
	


}
