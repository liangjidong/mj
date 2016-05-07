/**
 * 
 */
package org.chudian.mj.common;

import org.chudian.mj.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @project xhomeweb
 * @file org.xhome.web.util
 * @author longmaojing
 * @email 619692439@qq.com
 * @data 2013-11-28
 * @descrption
 */
public class ActionUtils {

	public final static String SESSION_USER = "MJ_WEB_USER";
	public final static String SESSION_USER_LOGOUT = "MJ_WEB_USER_LOGOUT"; // 用户登出

	public static User gerCurrenUser(HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute(SESSION_USER);

		return user;

	}

	public static void setCurrentUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER, user);
	}

	public static void removeCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_USER);
		session.setAttribute(SESSION_USER_LOGOUT, true);
	}
}
