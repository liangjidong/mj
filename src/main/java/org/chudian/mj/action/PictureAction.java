package org.chudian.mj.action;

import org.chudian.mj.annotation.UserLoginAuthorized;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.bean.User;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.PictureMapper;
import org.chudian.mj.service.PictureService;
import org.chudian.mj.service.UserService;
import org.chudian.mj.service.VideoService;
import org.chudian.mj.socket.UploadUtil;
import org.chudian.mj.utils.ActionUtil;
import org.chudian.mj.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by onglchen on 15-3-14.
 */
@Controller
public class PictureAction {

	@Autowired
	private PictureService pictureService;
	@Autowired
	private VideoService videoService;
	@Autowired
	private UserService userService;

	@Autowired
	private PictureMapper pictureMapper;

	public static String files_url = "/files";



	/*
	 *
	 * 后台上传图片
	 */
	//@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/upload_test", method = RequestMethod.GET)
	public Object uploadPicture_test(HttpServletRequest request, HttpServletResponse response) {

		/*try {
			 //response.sendRedirect("/mj/login.html");
//			request.getRequestDispatcher("/login.html").forward(request, response);
		}  catch (IOException e) {
			e.printStackTrace();
		}
*/
		return new Response(Status.SUCCESS, 100);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param multipartFile
	 * @return   status :  0 -- 成功, 6 -- 该图片已存在,请选择其它图片， 1 -- 识别服务器出错，请稍后再试
	 */
	/*
	 * 
	 * 后台上传图片
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/upload", method = RequestMethod.POST)
	public Object uploadPictureWeb(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile) {
		int status = Status.ERROR;
		int status2 = Status.ERROR;
		int userId = ActionUtil.getCurrentUser(request).getId();
		System.out.println("userId === "+ userId);
		if (!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			String extention = FileUtil.getExtensions(fileName);
			if (FileUtil.isPicture(fileName)) {
				try {

					byte[] jpgData = multipartFile.getBytes();
					System.out.println("Start");
					/*int result = UploadUtil.isEistsByC(jpgData);
					System.out.println("If Exits ==" + result);
					if(result == -1){
						return new Response(Status.ERROR,"识别服务器出错，请稍后再试");
					}
					if(result == 0){
						return new Response(Status.EXISTS,"该图片已存在,请选择其它图片");
					}*/
					Picture picture = new Picture();
					picture.setSize(FileUtil.FormetFileSize((int) multipartFile
							.getSize()));
					picture.setType(extention);
					Date createtime = new Date();
					picture.setCreatetime(createtime);
					status = pictureService.add(picture);
					String rename = "picture_" + picture.getId() + "." + extention;
					// 文件保存路径
					String filePath = request.getSession().getServletContext()
							.getRealPath("/")
							+ "/upload/picture/" + rename;

					// 转存文件
					File file_temp = new File(filePath);
					if (!file_temp.exists()) {
						file_temp.mkdirs();
					}

					/*SocketClient socketClient = new SocketClient();
					socketClient.Init();
					String result  = "";
					result = socketClient.Upload(jpgData,jpgData.length);
					System.out.println("Result == " + result);*/
					//UploadUtil.isEists(jpgData);
					multipartFile.transferTo(file_temp);

//					System.out.println("Png Length == " + jpgData.length);
//					status2 = 0;
					status2 = pictureService.copyPictureToTrainWeb(filePath, rename, picture);
					if (status2 == Status.SUCCESS) {
						picture.setUserId(userId);
						pictureMapper.updateByPrimaryKeySelective(picture);
						return new Response(status2, picture.getId());
					} else {
						return new Response(status2, "上传图片出错，请求重新上传");
					}


				}catch (IOException e){
					e.printStackTrace();
					return new Response(Status.ERROR, "连接误国服务器出错");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return new Response(Status.FOMATE_NOT_MATCH, "图片格式不支持,请求重新上传");
	}

	/*
	 * 安卓端上传产品
	 */

	@ResponseBody
	@RequestMapping(value = "/api/picture/addvideo2", method = RequestMethod.POST)
	public Object uploadPictureVideo2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userId") int userId,
			@RequestParam("pictureName") String pictureName,
			@RequestParam("videoName") String videoName) {
		int status = Status.ERROR, status2 = Status.ERROR, status3 = Status.ERROR;

		String picUrl = files_url + "/" + pictureName;
		String videoUrl = files_url + "/" + videoName;
		String extention = FileUtil.getExtensions(pictureName);
		Picture picture = new Picture();
		picture.setType(extention);
		picture.setUserId(userId);
		picture.setUrl(picUrl);
		picture.setName(pictureName);
		// status = pictureService.add(picture);
		status = pictureService.copyPictureToTrain(pictureName, picture);
		if (status == Status.SUCCESS) {
			Video video = new Video();
			video.setUserId(userId);
			video.setName(videoName);
			video.setOriginUrl(videoUrl);
			videoService.transferVideo(videoName, video);
			status2 = videoService.add(video);
			if (status2 == Status.SUCCESS) {
				picture.setVideoId(video.getId());
				status3 = pictureService.updateonly(picture);

				return new Response(status3);
			} else {
				return new Response(status2);
			}

		}
		return new Response(status);

	}

	/*
	 * 安卓端上传产品(ar)
	 */

	@ResponseBody
	@RequestMapping(value = "/api/picture/addthreed", method = RequestMethod.POST)
	public Object uploadPictureTreed(HttpServletRequest request, HttpServletResponse response,
									 @RequestParam("userId") int userId,
									 @RequestParam("pictureName") String pictureName) {
		int status = Status.ERROR, status2 = Status.ERROR, status3 = Status.ERROR;

		String picUrl = files_url + "/" + pictureName;

		String extention = FileUtil.getExtensions(pictureName);
		Picture picture = new Picture();
		picture.setType(extention);
		picture.setUserId(userId);
		picture.setUrl(picUrl);
		picture.setName(pictureName);
		// status = pictureService.add(picture);
		status = pictureService.copyPictureToTrain(pictureName, picture);

		return new Response(status);

	}


	/*
	 * 暂时不用
	 */

	@ResponseBody
	@RequestMapping(value = "/api/picture/addvideo3", method = RequestMethod.POST)
	public Object uploadPictureVideo3(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("userId") int userId,
			@RequestParam("fileurl") String fileurl,
			@RequestParam("filename") String filename) {
		int status = Status.ERROR, status2 = Status.ERROR, status3 = Status.ERROR;
		if (!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			String extention = FileUtil.getExtensions(fileName);
			Picture picture = new Picture();
			picture.setSize(FileUtil.FormetFileSize((int) multipartFile
					.getSize()));
			picture.setType(extention);
			status = pictureService.add(picture);
			if (status == Status.SUCCESS) {
				String rename = "picture_" + picture.getId() + "." + extention;
				// 文件保存路径
				String filePath = request.getSession().getServletContext()
						.getRealPath("/")
						+ "/upload/picture/" + rename;
				String savePath = "upload/picture/" + rename;

				// 转存文件
				File file_temp = new File(filePath);
				if (!file_temp.exists()) {
					file_temp.mkdirs();
				}
				try {
					multipartFile.transferTo(file_temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				picture.setUserId(userId);
				picture.setUrl(savePath);
				picture.setName(rename);
				pictureService.update(picture);

				Video video = new Video();
				video.setUserId(userId);
				video.setName(filename);
				video.setFinalUrl(fileurl);
				status2 = videoService.add(video);
				if (status2 == Status.SUCCESS) {
					picture.setVideoId(video.getId());
					status3 = pictureService.update(picture);

					return new Response(status3);
				} else {
					return new Response(status2);
				}

			}

		}
		return new Response(status);

	}

	/*
	 * 安卓注册头像
	 */
	@ResponseBody
	@RequestMapping(value = "/api/picture/headimg", method = RequestMethod.POST)
	public Object uploadHeadImg(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("userId") int userId) {
		int status = Status.ERROR;
		if (!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			String extention = FileUtil.getExtensions(fileName);
			try {
				Picture picture = new Picture();
				picture.setSize(FileUtil.FormetFileSize((int) multipartFile
						.getSize()));
				picture.setType(extention);
				status = pictureService.add(picture);
				String rename = "picture_" + picture.getId() + "." + extention;
				// 文件保存路径
				String filePath = request.getSession().getServletContext()
						.getRealPath("/")
						+ "/upload/picture/" + rename;
				String savePath = "upload/picture/" + rename;

				// 转存文件
				File file_temp = new File(filePath);
				if (!file_temp.exists()) {
					file_temp.mkdirs();
				}
				multipartFile.transferTo(file_temp);

				picture.setUrl(savePath);
				picture.setName(rename);
				pictureService.updateOnly(picture);

				User user = userService.get(userId);
				user.setHeadImg(picture.getUrl());
				userService.update(user);

				return new Response(status, picture.getUrl());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return new Response(status, "格式不符");
	}

	/*
	 * 上传图片
	 */
	@ResponseBody
	@RequestMapping(value = "/api/picture/upload2", method = RequestMethod.POST)
	public Object uploadPicture(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile) {
		int status = Status.ERROR;
		Integer userId=null;
		try{
			userId=ActionUtil.getCurrentUser(request).getId();
		}catch(Exception e){
			e.printStackTrace();
		}
		if (!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			String extention = FileUtil.getExtensions(fileName);
			try {
				Picture picture = new Picture();
				Date date=new Date();
				picture.setCreatetime(date);
				picture.setSize(FileUtil.FormetFileSize((int) multipartFile
						.getSize()));
				picture.setType(extention);
				picture.setUserId(userId);
//				status = pictureService.add(picture);
				String rename = "picture_" + picture.getId() + "." + extention;
				// 文件保存路径
				String filePath = request.getSession().getServletContext()
						.getRealPath("/")
						+ "/upload/picture/" + rename;
				String savePath = "upload/picture/" + rename;

				// 转存文件
				File file_temp = new File(filePath);
				if (!file_temp.exists()) {
					file_temp.mkdirs();
				}
				multipartFile.transferTo(file_temp);
				pictureService.copyPictureToTrainWeb(filePath, rename, picture); 
//				pictureService.update(picture);
				status = pictureService.add(picture);
//
				return new Response(status, rename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Response(status, "上传出错");
	}




	/**	根据id查询视频
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return status : 0 -- 成功， 7 -- 图片不存在, 13 -- 登录失效
	 *
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/{id}", method = RequestMethod.GET)
	public Object get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		int status;
		String message = "";
		Picture picture = pictureService.get(id);
		if (picture == null) {
			status = Status.NOT_EXISTS;
			message = "图片不存在";

		} else {
			status = Status.SUCCESS;
			message = "查找图片成功";

		}
		return new Response(status,message, picture);

	}


	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/{id}", method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		int status;
		String message = "";
		Picture picture = new Picture();
		picture.setId(id);
		status = pictureService.delete(picture);
		if(status == Status.NOT_EXISTS){
			message = "视频不存在";
		}
		if(status == Status.ERROR){
			message = "删除失败";
		}
		if(status == Status.NO_DELETE){
			message = "存在关联产品，此视频不能删除";
		}
		if(status == Status.SUCCESS){
			message = "删除成功";
		}
		return new Response(status,message);
	}

	// 后台 修改图片的对应视频链接
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/addvideo", method = RequestMethod.PATCH)
	public Object addPictureVideo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("pictureId") int pictureId,
			@RequestParam("videoId") int videoId) {

		int status;
		int userId = ActionUtil.getCurrentUser(request).getId();
		Picture picture = new Picture();
		picture.setId(pictureId);
		picture.setVideoId(videoId);
		picture.setUserId(userId);
		status = pictureService.updateOnly(picture);
		return new Response(status);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param query
	 * @param currentPage
	 * @return status : 0 --成功 ;  13 -- 登录失效
	 * 		   body :  用户返回自己拥有的图片， 管理员则返回所有图片
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/query", method = RequestMethod.GET)
	public Object query(HttpServletRequest request, HttpServletResponse response,
			QueryBase query, @RequestParam(value = "currentPage", required = false) String currentPage, @RequestParam(value = "maxRow", required = false) String maxRow) {
		int userId = ActionUtil.getCurrentUser(request).getId();
		String message;
		if (query == null) {
			query = new QueryBase();
		}
		User user = userService.get(userId);
		if(user != null && user.getRole() == User.ROLE_USER){
			query.addParameter("userId", userId);
		}

		if (currentPage != null) {
			long cuPage = Long.parseLong(currentPage);
			query.setCurrentPage(cuPage);
		}
		if(maxRow != null){
			long maxR = Long.parseLong(maxRow);
			query.setMaxRow(maxR);
		}
		pictureService.query(query);
		return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults());
	}
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/picture/register", method = RequestMethod.POST)
	public Object uploadPictureVideo3(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("pictureName") String pictureName) {
		int userId = ActionUtil.getCurrentUser(request).getId();
		int status = Status.ERROR;
		String picUrl = files_url + "/" + pictureName;
		String extention = FileUtil.getExtensions(pictureName);
		Picture picture = new Picture();
		picture.setType(extention);
		picture.setUrl(picUrl);
		picture.setName(pictureName);
		status = pictureService.copyPictureToTrain(pictureName, picture);
		return new Response(status);
	}

	/*@ResponseBody
	@RequestMapping(value = "/api/queryARInfo", method = RequestMethod.GET)
	public Object getVidioByPicName(HttpServletRequest request, HttpServletResponse response, @RequestParam("picName") String picName){
		String path = ServletUtil.getRequestUrl(request);
		HashMap map = pictureService.queryARInfo(picName, path);
		return map;
	}*/





}
