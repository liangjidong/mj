package org.chudian.mj.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.annotation.UserLoginAuthorized;
import org.chudian.mj.bean.LogHistory;
import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.*;
import org.chudian.mj.utils.ActionUtil;
import org.chudian.mj.utils.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by onglchen on 15-3-11.
 */
@Controller
public class MjproductAction {

	private final Log logger = LogFactory.getLog(MjproductAction.class);

	@Autowired
	private MjproductService mjproductService;
	@Autowired
	private PictureService pictureService;
	@Autowired
	private VideoService videoService;
	@Autowired
	private LogHistoryService logHistoryService;
	@Autowired
	private UserService userService;

	/**
	 *
	 * @param request
	 * @param response
	 * @param mjproduct
	 * @return status : 0 -- 成功 ,1 -- 失败
	 */
	// 后台上传产品
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct", method = RequestMethod.POST)
	public Object registMjproduct(HttpServletRequest request,HttpServletResponse response,
			@RequestBody Mjproduct mjproduct) {
		int status;
		String message = "";
		int userId;
		int videoId;
		// the userId from url is not useful
		userId = ActionUtil.getCurrentUser(request).getId();
		mjproduct.setUserId(userId);
		Picture picture = new Picture();
		if(mjproduct.getVideoId() == null){ // 当ar类型不是视频时，给一个id = 1
			System.out.println("XXXXXXXXXXX ===");
			videoId = 1;
			mjproduct.setVideoId(videoId);
		}else {
			videoId = mjproduct.getVideoId();
		}
		if (mjproduct.getPictureId() != null) {
			picture.setId(mjproduct.getPictureId());
			picture.setVideoId(videoId);
			picture.setRealityType(0);
			picture.setStatus(1);
		}
		mjproduct.setCreatetime(new Date());
		mjproduct.setStatus(1);
//		mjproduct.setKeepword1("www.baidu.com");
		
		mjproduct.setClicktimes(0);
		status = mjproductService.add(mjproduct);
		
		//pictureService.update(picture);
		if (status == Status.SUCCESS) {
			message = "添加产品成功";
			try {
				LogHistory l = new LogHistory();
				l.setOperatonTime(new Date());
				l.setUserId(userId);
				l.setUserId(userId);
				l.setOperationContent("添加产品：" + mjproduct.getTitle() + "    "
						+ l.getOperatonTime());
				logHistoryService.add(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Response(status, message, mjproduct.getId());
		}

		message = "添加产品失败";
		return new Response(status, message);
	}
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/updateclicktimes", method = RequestMethod.POST)
	public void updateclicktimes(@RequestParam("id") int id) {
		mjproductService.updateclicktimes(id);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return status : 0 -- 成功， 7 -- 产品不存在, 13 -- 登录失效
	 * 		   body : pictureObj是产品关联图片信息, videoObj是产品关联视频信息
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/{id}", method = RequestMethod.GET)
	public Object get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		int status;
		String message = "";
		Mjproduct mjproduct = mjproductService.get(id);
		if (mjproduct == null) {
			status = Status.NOT_EXISTS;
			message = "产品不存在";

		} else {
			status = Status.SUCCESS;
			message = "查找产品成功";

		}
		return new Response(status,message, mjproduct);

	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return  status :0 -- 成功 ,1 -- 失败, 7 -- 不存在 ,3 -- 不能删除
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/{id}", method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		int status;
		String message = "";
		int userId = ActionUtil.getCurrentUser(request).getId();
		Mjproduct mjproduct = new Mjproduct();
		mjproduct.setId(id);
		status = mjproductService.delete(mjproduct);
		if(status == Status.NOT_EXISTS){
			message = "魔镜产品不存在";
		}
		if(status == Status.ERROR){
			message = "删除失败";
		}
		if(status == Status.NO_DELETE){
			message = "此产品不能删除";
		}
		if(status == Status.SUCCESS){
			message = "删除成功";
			LogHistory l = new LogHistory();
			l.setOperatonTime(new Date());
			l.setUserId(userId);
			l.setOperationContent("删除产品："
					+ mjproduct.getTitle() + "    "
					+ l.getOperatonTime());
			System.out.println("shabi");
			logHistoryService.add(l);
		}
		return new Response(status,message);
	}

	/**   修改产品接口
	 *
	 * @param request
	 * @param response
	 * @param mjproduct
	 * @return  status: 0 -- 成功, 10 -- 传参错误, 7 -- 产品不存在, 1 -- 修改失败 ,  13 -- 登录失效
	 */

	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproductedit", method = RequestMethod.PATCH)
	public Object update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Mjproduct mjproduct) {
		int status;
		String message = "";
		if(mjproduct.getId() == null){
			message = "传参错误，修改失败";
			return new Response(Status.CODE_NOT_MATCH, message);
		}
		status = mjproductService.update(mjproduct);
		if(status == Status.SUCCESS){
			LogHistory l = new LogHistory();
			l.setOperatonTime(new Date());
			l.setUserId(mjproduct.getUserId());
			l.setOperationContent("更新产品：" + mjproduct.getTitle()
					+ "    " + l.getOperatonTime());
			logHistoryService.add(l);
			message = "修改成功";
		}else if(status == Status.NOT_EXISTS){
			message = "产品不存在，修改失败";
		}else {
			message = "修改失败";
		}
		return new Response(status, message);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param query
	 * @param currentPage
	 * @return
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/query", method = RequestMethod.GET)
	public Object query(
			HttpServletRequest request, HttpServletResponse response,
			QueryBase query,
			@RequestParam(value = "currentPage", required = false) String currentPage, @RequestParam(value = "maxRow", required = false) String maxRow) {
		String message;
		int userId = ActionUtil.getCurrentUser(request).getId();
//		int userId = 1;

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
		mjproductService.query(query);
		return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults());
	}

	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/query/{userId}", method = RequestMethod.GET)
	public Object querybyUserId(HttpServletRequest request, HttpServletResponse response, QueryBase query,
			@PathVariable("userId") String userId,
			@RequestParam(value = "currentPage") String currentPage) {
		Object obj = ActionUtil.getCurrentUser(request);
		if (obj != null) {
			User user_session = (User) obj;
			int request_userId = Integer.parseInt(userId);
			// get the User object in db related to the user in Session
			User u_db = userService.get(user_session.getId());
			if (user_session.getId() == request_userId
					|| u_db.getRole().equals(2)) {
				String message;
				if (query == null) {
					query = new QueryBase();
				}
				if (currentPage != null) {
					long cuPage = Long.parseLong(currentPage);
					query.setCurrentPage(cuPage);
				}

				query.addParameter("userId", userId);
				mjproductService.query(query);
				return new Response(Status.SUCCESS,
						Long.parseLong(currentPage), query.getTotalPage(),
						query.getResults());

			} else {
				System.out.println("obj不为空");
				return new Response(Status.INVALID_OPERATION);
			}
		}
		System.out.println("obj为空");
		return new Response(Status.INVALID_OPERATION);

		// String message;
		// if (query == null) {
		// query = new QueryBase();
		// }
		// if (currentPage != null) {
		// long cuPage = Long.parseLong(currentPage);
		// query.setCurrentPage(cuPage);
		// }
		// User user = ActionUtil.getCurrentUser(request);
		// if (user != null) {
		// if (user.getId() == Integer.parseInt(userId)) {
		// query.addParameter("userId", userId);
		// mjproductService.query(query);
		// return new Response(Status.SUCCESS, query.getResults());
		// }
		// }
		// return new Response(Status.INVALID_OPERATION);
	}

	// 后台 修改图片的对应视频链接
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/mjproduct/changeVideo", method = RequestMethod.PATCH)
	public Object changeVideo(HttpServletRequest request, HttpServletResponse response,
								  @RequestParam("videoId") int videoId,
							      @RequestParam("productId") int productId) {

		int status;
		Picture picture = new Picture();
		Mjproduct mjproduct = new Mjproduct();
		mjproduct.setId(productId);
		mjproduct.setVideoId(videoId);
		picture.setVideoId(videoId);
		 //pictureService.updateOnly(picture);
		status = mjproductService.update(mjproduct);
		return new Response(status);
	}

	/**
	 *    识别图片不区分用户
	 * @param request
	 * @param response
	 * @param picName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/queryARInfo", method = RequestMethod.GET)
	public Object getVidioByPicName(HttpServletRequest request, HttpServletResponse response, @RequestParam("picName") String picName){
		String path = ServletUtil.getRequestUrl(request);
		HashMap map = mjproductService.queryARInfo(picName, path );
		return map;
	}

	/**
	 * 		识别图片区分用户
	 * @param request
	 * @param response
	 * @param picName
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/queryARInfoByUser", method = RequestMethod.GET)
	public Object getVidioByPicName(HttpServletRequest request, HttpServletResponse response, @RequestParam("picName") String picName,@RequestParam("userId") int userId){
		String path = ServletUtil.getRequestUrl(request);
		HashMap map = mjproductService.queryARInfo(picName, path ,userId);
		return map;
	}
}
