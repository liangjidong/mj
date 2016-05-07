package org.chudian.mj.listener;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.chudian.mj.bean.User;
import org.chudian.mj.utils.ActionUtil;

public class SessionListener implements HttpSessionAttributeListener,
		HttpSessionListener {
	private static Map<Integer, HttpSession> sessions = new HashMap<Integer, HttpSession>();
	// synchronizedMap
	static {
		if (sessions == null) {
			sessions = Collections
					.synchronizedMap(new HashMap<Integer, HttpSession>());
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {

	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent e) {
		HttpSession session = e.getSession();
		System.out.println("-------------*start added*-----------------------");
		String attrName = e.getName();
		if (attrName.equals(ActionUtil.SESSION_USER)) {
			System.out.println(session.getAttribute(attrName));
			User nowUser = (User) e.getValue();
			// System.out.println(nowUser.getName());
			Object[] userSet = sessions.keySet().toArray();
			for (int i = sessions.size() - 1; i >= 0; i--) {
				int userId = (int) userSet[i];
				if (userId == nowUser.getId()) {
					sessions.get(userId).invalidate();
					break;
				}
			}
			sessions.put(nowUser.getId(), session);
			System.out.println(new Date());
		} else {
			System.out.println("Unknow attribute");
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent e) {
		HttpSession session = e.getSession();
		System.out
				.println("-------------*start Removed*-----------------------");
		String attrName = e.getName();
		if (attrName.equals(ActionUtil.SESSION_USER)) {
			User nowUser = (User) e.getValue();
			Object[] userSet = sessions.keySet().toArray();
			for (int i = sessions.size() - 1; i >= 0; i--) {
				int userId = (int) userSet[i];
				if (userId == nowUser.getId()) {
					sessions.remove(userId);
					System.out.println(new Date());
					break;
				}
			}

		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent e) {
		HttpSession session = e.getSession();
		System.out
				.println("-------------*start replace*-----------------------");
		String attrName = e.getName();
		if (attrName.equals(ActionUtil.SESSION_USER)) {
			User nowUser = (User) session.getAttribute(ActionUtil.SESSION_USER);
			Object[] userSet = sessions.keySet().toArray();
			for (int i = sessions.size() - 1; i >= 0; i--) {
				int userId = (int) userSet[i];
				HttpSession oldsession = sessions.get(userId);
				if (userId == nowUser.getId()
						&& !oldsession.getId().equals(session.getId())) {
					System.out.println("Remove:invalidate 1!");
					oldsession.invalidate();
					//没有更新sessions map
				} else if (oldsession.getId().equals(session.getId())) {
					System.out.println("User changed!");
					sessions.remove(userId);
					sessions.put(nowUser.getId(), session);
				}
			}
			
		}

	}

}
