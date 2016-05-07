package org.chudian.mj.action;

import org.chudian.mj.annotation.UserLoginAuthorized;
import org.chudian.mj.bean.User;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.UserService;
import org.chudian.mj.service.VideoService;
import org.chudian.mj.utils.ActionUtil;
import org.chudian.mj.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * Created by onglchen on 15-3-20.
 */
@Controller
public class VideoAction {

	@Autowired
	private VideoService videoService;

	@Autowired
	private UserService userService;

	public static String files_url = "/files";


	/*
	 * web端上传视频
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/video/upload_test", method = RequestMethod.POST)
	public Object uploadVideo_test(HttpServletRequest request,
							  @RequestParam("file") MultipartFile multipartFile,
							  @RequestParam("userId") int userId) {
		int status = Status.ERROR;

		return new Response(Status.SUCCESS, 100);// 这里改的 解决 上传产品是后乱报错
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param multipartFile
	 * @return status 0 -- 成功,  1 -- 失败
	 * 			body : 刚上传视频id
	 */
	/*
	 * web端上传视频
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/video/upload", method = RequestMethod.POST)
	public Object uploadVideo(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile) {
		int status = Status.ERROR;
		int userId = ActionUtil.getCurrentUser(request).getId();
		if (!multipartFile.isEmpty()) {

			String fileName = multipartFile.getOriginalFilename();
			String extention = FileUtil.getExtensions(fileName);
			if (FileUtil.isVideo(fileName)) {
				try {
					Video video = new Video();
					video.setName(fileName);
					video.setSize(FileUtil.FormetFileSize((int) multipartFile
							.getSize()));
					video.setVideoFormat(extention);
					video.setUserId(userId);
					Date createtime = new Date();
					video.setCreatetime(createtime);
					status = videoService.add(video);
					String rename = "video_" + video.getId() + "." + extention;
					// 文件保存路径
					String filePath = request.getSession().getServletContext()
							.getRealPath("/")
							+ "/upload/video/" + rename;
					String savePath = "upload/video/" + rename;

					// 转存文件
					File file_temp = new File(filePath);
					if (!file_temp.exists()) {
						file_temp.mkdirs();
					}
					multipartFile.transferTo(file_temp);
					videoService.transferVideoWeb(filePath, rename, video);
					videoService.update(video);// 数据表的编码问题这里这样用为什么会报错

					return new Response(status, video.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return new Response(Status.ERROR, "视频格式不支持，请求重新上传");// 这里改的 解决 上传产品是后乱报错
	}
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/video/query", method = RequestMethod.GET)
	public Object query(HttpServletRequest request,HttpServletResponse response,
			QueryBase query,
			@RequestParam(value = "currentPage", required = false) String currentPage,@RequestParam(value = "maxRow", required = false) String maxRow) {
		String message;
		int userId = ActionUtil.getCurrentUser(request).getId();
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
		videoService.query(query);
		return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults());


	}

	/**		查询视频列表，不分页
	 *
	 * @param request
	 * @param response
	 * @param query
	 * @return
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/video/queryAll", method = RequestMethod.GET)
	public Object queryAll(HttpServletRequest request,HttpServletResponse response, QueryBase query) {
		String message;
		int userId;
		if (query == null) {
			query = new QueryBase();
		}
		userId = ActionUtil.getCurrentUser(request).getId();
		User user = userService.get(userId);
		if(user != null && user.getRole() == User.ROLE_USER){
			query.addParameter("userId", userId);
		}

		videoService.queryAll(query);
		message = "查询成功";

		return new Response(Status.SUCCESS, message, query.getResults());



	}

	/**	根据id查询视频
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return status : 0 -- 成功， 7 -- 产品不存在, 13 -- 登录失效
	 *
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/video/{id}", method = RequestMethod.GET)
	public Object get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		int status;
		String message = "";
		Video video = videoService.get(id);
		if (video == null) {
			status = Status.NOT_EXISTS;
			message = "视频不存在";

		} else {
			status = Status.SUCCESS;
			message = "查找视频成功";

		}
		return new Response(status,message, video);

	}

	/**
	 *
	 * @param request
	 * @param id
	 * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在 ,3 -- 不能删除（存在关联产品）  13 -- 登录失效
	 */
	@ResponseBody
	@RequestMapping(value = "/api/video/{id}", method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request, @PathVariable int id) {
		int status = Status.ERROR;
		String message = "";
		Video video = new Video();
		video.setId(id);
		status = videoService.delete(video);
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


	/*
	 * 转码
	 */

	@ResponseBody
	@RequestMapping(value = "/video/transfer", method = RequestMethod.POST)
	public Object ConvertVideo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("filename") String filename) {
		Video video = new Video();
		boolean flag = videoService.transferVideo(filename, video);
		if (flag) {
			return new Response(Status.SUCCESS);
		}
		return new Response(Status.ERROR);
	}

	/*
	 * 移动端注册视频
	 */
	@ResponseBody
	@RequestMapping(value = "/api/video", method = RequestMethod.POST)
	public Object registerVideo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("fileurl") String fileurl,
			@RequestParam("filename") String filename,
			@RequestParam("userId") int userId) {
		int status = Status.ERROR;
		Video video = new Video();
		video.setUserId(userId);
		video.setName(filename);
		video.setFinalUrl(fileurl);
		status = videoService.add(video);
		if (status == Status.SUCCESS) {
			return new Response(status, video);
		}

		return new Response(status);
	}

}
