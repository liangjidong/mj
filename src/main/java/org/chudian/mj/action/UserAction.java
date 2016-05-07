package org.chudian.mj.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.annotation.ManagerLoginAuthorized;
import org.chudian.mj.annotation.UserLoginAuthorized;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.UserService;
import org.chudian.mj.utils.ActionUtil;
import org.chudian.mj.utils.IdentifyingCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by onglchen on 15-3-11.
 */
@Controller
public class UserAction {

	private final Log logger = LogFactory.getLog(UserAction.class);
	public static final String COOKIE_USER_SID = "MJ_COOKIE_USER_SID";
	public static final String COOKIE_USER_PASSWORD = "MJ_COOKIE_USER_PASSWORD";

	@Autowired
	private UserService userService;

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/api/user/identifyPic", method = RequestMethod.GET)
	public void getIdentifyPic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的相应图片
		response.setContentType("image/jpeg");
		IdentifyingCode idCode = new IdentifyingCode();
		BufferedImage image = new BufferedImage(idCode.getWidth(),
				idCode.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		// 定义字体样式
		Font myFont = new Font("黑体", Font.BOLD, 16);
		// 设置字体
		g.setFont(myFont);

		g.setColor(idCode.getRandomColor(200, 250));
		// 绘制背景
		g.fillRect(0, 0, idCode.getWidth(), idCode.getHeight());
		HttpSession session = request.getSession();
		g.setColor(idCode.getRandomColor(180, 200));
		idCode.drawRandomLines(g, 160);
		String identifyCode = idCode.drawRandomString(4, g);
		session.setAttribute("identifyCode", identifyCode);
		g.dispose();
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 *
	 * @param user
	 *            用户信息
	 * @param code
	 *            验证码
	 * @param httpSession
	 * @return state : 0 -- 成功, 10 -- 验证码错误, 6--用户已存在
	 */

	@ResponseBody
	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
	public Object registUser(@RequestBody User user, @RequestParam String code,
			HttpSession httpSession) {
		// public Object registUser(@RequestBody User user,HttpSession
		// httpSession){
		int status;
		String message = "";
		String identifyCode = (String) httpSession.getAttribute("identifyCode");

		if (!(code.equals(identifyCode))) {
			message = "验证码错误";
			status = Status.CODE_NOT_MATCH;
			return new Response(status, message);
		} else {
			user.setRole(1);
			status = userService.add(user);
			if (status == Status.SUCCESS) {
				message = "注册用户成功";
				return new Response(status, message, user.getId());
			}
			message = "注册用户失败";
			return new Response(status, message);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/api/user2", method = RequestMethod.POST)
	public Object registUser2(@RequestBody User user, HttpSession httpSession) {
		// public Object registUser(@RequestBody User user,HttpSession
		// httpSession){
		int status;
		String message = "";

		user.setRole(1);
		status = userService.add(user);
		if (status == Status.SUCCESS) {
			message = "注册用户成功";
			return new Response(status, message, user.getId());
		}
		message = "注册用户失败";
		return new Response(status, message);

	}

	/**
	 *
	 * @param request
	 * @param user
	 *            用户json数据
	 * @param code
	 *            验证码
	 * @param httpSession
	 * @return status 0--成功, 10--验证码错误, 7 --用户不存在,9-- 密码不正确
	 */

	@ResponseBody
	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
	public Object login(HttpServletRequest request, @RequestBody User user,
			@RequestParam("code") String code, HttpSession httpSession) {
		// 此处是如何将requestbody直接变为对象的呢？(由于requestBody是json格式的数据,springMVC框架可以自动转换)
		int status;
		logger.info(user);
		String message = "";
		String identifyCode = (String) httpSession.getAttribute("identifyCode");

		if (!(code.equals(identifyCode))) {
			message = "验证码错误";
			status = Status.CODE_NOT_MATCH;
			return new Response(status, message);
		} else {

			status = userService.login(user);
			if (status == Status.SUCCESS) {
				User user_db = userService.selectByName(user.getName());
				ActionUtil.setCurrentUser(request, user_db);
				user.setPassword(""); // 返回把密码置空
				user.setRole(user_db.getRole());
				message = "登录成功";
				return new Response(status, message, user);
			}
			message = "登录失败";
			return new Response(status, message);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/api/user/login2", method = RequestMethod.POST)
	public Object login2(HttpServletRequest request, @RequestBody User user,
			HttpSession httpSession) {
		int status;
		logger.info(user);
		String message = "";
		status = userService.login(user);
		if (status == Status.SUCCESS) {
			User user_db = userService.selectByName(user.getName());

			// ActionUtil.setCurrentUser(request, user_db);
			user.setPassword(""); // 返回把密码置空
			user.setRole(user_db.getRole());
			message = "登录成功";
			return new Response(status, message, user);
		}
		message = "登录失败";
		return new Response(status, message);
	}

	/**
	 * 查询用户详情
	 *
	 * @param request
	 * @param id
	 * @return status : 0 --成功， 7 -- 用户不存在 body : 用户数据json格式
	 */
	@UserLoginAuthorized
	@ResponseBody
	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public Object get(HttpServletRequest request, @PathVariable int id) {
		int status;
		String message = "";
		User user = userService.get(id);
		if (user == null) {
			status = Status.NOT_EXISTS;
			message = "用户不存在";

		} else {
			status = Status.SUCCESS;
			message = "查找用户成功";
			user.setPassword("");

		}
		return new Response(status, message, user);

	}

	/**
	 *
	 * @param request
	 * @param id
	 * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在 ,3 -- 不能删除（存在关联产品） 13 --
	 *         登录失效/权限不够
	 */
	@ResponseBody
	@ManagerLoginAuthorized
	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request, @PathVariable int id) {
		int status;
		String message = "";
		User user = new User();
		user.setId(id);
		status = userService.delete(user);
		if (status == Status.NOT_EXISTS) {
			message = "用户不存在";
		}
		if (status == Status.ERROR) {
			message = "删除失败";
		}
		if (status == Status.NO_DELETE) {
			message = "该用户不能删除";
		}
		if (status == Status.SUCCESS) {
			message = "删除成功";
		}
		return new Response(status, message);
	}

	/**
	 *
	 * @param request
	 * @param user
	 *            user.id 为必填
	 * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 用户不存在, 1 -- 修改失败, 13 -- 登录失效
	 */
	@ResponseBody
	@RequestMapping(value = "/api/user", method = RequestMethod.PATCH)
	public Object update(HttpServletRequest request, @RequestBody User user) {
		int status = Status.ERROR;
		String message = "";
		if (user.getId() == null) {
			message = "传参错误，修改失败";
			return new Response(Status.CODE_NOT_MATCH, message);
		}
		status = userService.update(user);
		if (status == Status.SUCCESS) {
			message = "修改成功";
		} else if (status == Status.NOT_EXISTS) {
			message = "用户不存在，修改失败";
		} else {
			message = "修改失败";
		}

		return new Response(status, message);
	}

	@ResponseBody
	// @ManagerLoginAuthorized
	@RequestMapping(value = "/api/userlevel", method = RequestMethod.PATCH)
	public Object updateUserLevel(HttpServletRequest request,
			@RequestParam int userId, @RequestParam int type) {
		int status;
		User user = new User();
		user.setId(userId);
		if (type == 1) {
			user.setRole(1);
		} else if (type == 2) {
			user.setRole(2);
		} else {
			return new Response(Status.ERROR);
		}
		status = userService.update(user);

		return new Response(status);
	}

	/**
	 *
	 * @param request
	 * @param query
	 * @param currentPage
	 * @return
	 */
	@ResponseBody
	// @ManagerLoginAuthorized
	@RequestMapping(value = "/api/user/query", method = RequestMethod.GET)
	public Object query(
			HttpServletRequest request,
			QueryBase query,
			@RequestParam(value = "currentPage", required = false) String currentPage,
			@RequestParam(value = "maxRow", required = false) String maxRow) {
		String message;
		if (query == null) {
			query = new QueryBase();
		}

		if (currentPage != null) {
			long cuPage = Long.parseLong(currentPage);
			query.setCurrentPage(cuPage);
		}
		if (maxRow != null) {
			long maxR = Long.parseLong(maxRow);
			query.setMaxRow(maxR);
		}
		userService.query(query);

		return new Response(Status.SUCCESS, query.getTotalRow(),
				query.getResults());
	}

	@ResponseBody
	@RequestMapping(value = "/api/user/loginOut", method = RequestMethod.GET)
	public void loginOutWeb(HttpServletRequest request,
			HttpServletResponse response) {
		ActionUtil.removeCurrentUser(request, response);
		// request.getRequestDispatcher("/web/html/page/login.html").forward(request,
		// response);
		System.out.println("loginOut======");
		try {
			response.sendRedirect("../../login.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return new ModelAndView("/takeout/web/html/page/login.html");
	}

	/**
	 * 用户退出
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/user/loginout", method = RequestMethod.GET)
	public Object loginOut(HttpServletRequest request,
			HttpServletResponse response) {
		ActionUtil.removeCurrentUser(request, response);
		String message = "用户登出成功";
		return new Response(Status.SUCCESS, message);
	}

}
