package org.chudian.mj.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.chudian.mj.bean.User;
import org.chudian.mj.action.UserAction;

public class ActionUtil {

	public static final String SESSION_USER = "MJ_USER";
	public static final String SESSION_USER_LOGOUT = "MJ_USER_LOGOUT";// 用户登出
	public static final String SESSION_USER_ACTIVATION_KEY = "MJ_USER_ACTIVATION_KEY";
	public static final String SESSION_USER_RESETPASSWORD_KEY = "MJ_USER_RESETPASSWORD_KEY";

	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SESSION_USER);
		return user;
	}

	public static void setCurrentUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		System.out.println("setCurrentUser运行了");
		session.setAttribute(SESSION_USER, user);
		session.setAttribute(SESSION_USER_LOGOUT, false);
	}

	public static void removeCurrentUser(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_USER);
		session.setAttribute(SESSION_USER_LOGOUT, true);
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			if (name.equals(UserAction.COOKIE_USER_SID)
					|| name.equals(UserAction.COOKIE_USER_PASSWORD)) {
				cookie.setValue(null);
				cookie.setMaxAge(0);// 立刻删除Cookie
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	public static boolean getLogout(HttpServletRequest request) {
		Object r = request.getSession().getAttribute(SESSION_USER_LOGOUT);
        return r != null && (Boolean) r;
	}

	public static void setActivationKey(HttpServletRequest request, String key) {
		request.getSession().setAttribute(SESSION_USER_ACTIVATION_KEY, key);
	}

	public static String getActivationKey(HttpServletRequest request) {
		String key = (String) request.getSession().getAttribute(
				SESSION_USER_ACTIVATION_KEY);
		return key;
	}

	public static void removeActivationKey(HttpServletRequest request) {
		request.getSession().removeAttribute(SESSION_USER_ACTIVATION_KEY);
	}

	public static void setResetPasswordKey(HttpServletRequest request,
			String key) {
		request.getSession().setAttribute(SESSION_USER_RESETPASSWORD_KEY, key);
	}

	public static String getResetPasswordKey(HttpServletRequest request) {
		String key = (String) request.getSession().getAttribute(
				SESSION_USER_RESETPASSWORD_KEY);
		return key;
	}

	public static void removeResetPasswordKey(HttpServletRequest request) {
		request.getSession().removeAttribute(SESSION_USER_RESETPASSWORD_KEY);
	}

}
