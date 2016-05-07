package org.chudian.mj.action;

import org.chudian.mj.bean.Picture;
import org.chudian.mj.bean.ThreeD;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.PictureService;
import org.chudian.mj.service.ThreeDService;
import org.chudian.mj.utils.FileUtil;
import org.chudian.mj.utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Controller
public class ThreeDAction {
	final String ThreeDTempDir = "F:";
	final String ThreeDZip = "F:\\CJTech\\";
	@Autowired
	ThreeDService threeDService;
	@Autowired
	PictureService pictureService;

	/*
	 * 后台上传3D文件
	 */
	@ResponseBody
	@RequestMapping(value = "/api/threed", method = RequestMethod.POST)
	public Object upload3D(HttpServletRequest request,
			@RequestParam String name, @RequestParam int[] x,
			@RequestParam int[] y, @RequestParam int[] z,
			@RequestParam("osgFile") MultipartFile osgFile,
			@RequestParam("pictures") MultipartFile[] pictures)
			throws IOException {
		int status;
		Date date = new Date();

		java.util.Calendar c = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		File outerfolder = new File(ThreeDTempDir + "/" + f.format(c.getTime()));
		if (!outerfolder.exists()) {
			outerfolder.mkdirs();
			File folder = new File(ThreeDTempDir + "/" + f.format(c.getTime())
					+ "/pictures");
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
		if(osgFile!=null){
			downloadFile(ThreeDTempDir + "/" + f.format(c.getTime()) + "/"
					+ osgFile.getOriginalFilename(), osgFile);
		}
		if (pictures.length > 0) {
			for (int i = 0; i < pictures.length; i++) {
				downloadFile(ThreeDTempDir + "/" + f.format(c.getTime()) + "/pictures/"
						+ pictures[i].getOriginalFilename(), pictures[i]);
			}
		}
		try {
			ZipUtil.compress(ThreeDTempDir + "/" + f.format(c.getTime()),ThreeDZip+f.format(c.getTime())+".zip" );
//			new ZipUtil(ThreeDZip+f.format(c.getTime())+".zip").compress(ThreeDTempDir + "/" + f.format(c.getTime()));
			//ZipUtils.zip(ThreeDZip+f.format(c.getTime())+".zip", new File(ThreeDTempDir + "/" + f.format(c.getTime())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ThreeD threeD = new ThreeD();
		threeD.setName(name);
		threeD.setCreatetime(date);
		threeD.setUrl("/mj/threed/"+f.format(c.getTime())+".zip");
		status = threeDService.add(threeD);
		return new Response(status);
	}



	public void downloadFile(String path, MultipartFile file)
			throws IOException {
		byte[] buffer = new byte[1444];
		InputStream in = file.getInputStream();
		File localFile = new File(path);
		FileOutputStream fs = new FileOutputStream(localFile);
		try {
			while (in.read(buffer) != -1) {
				fs.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
