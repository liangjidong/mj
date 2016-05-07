package org.chudian.mj.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by onglchen on 4/16/15.
 */
public class SessionUtil {

    private static final String SESSION_KEY_CURRENT_USER = "SESSION_KEY_CURRENT_USER";

    public static Object getCurrentUser(HttpServletRequest req){
        return sessionGet(req, SESSION_KEY_CURRENT_USER);
    }

    public static void setCurrentUser(HttpServletRequest req,Object user){
        sessionSet(req, SESSION_KEY_CURRENT_USER, user);
    }

    public static void removeCurrentUser(HttpServletRequest req){
        sessionRemove(req, SESSION_KEY_CURRENT_USER);
    }

    /**
     *
     * @title sessionGet
     * @author Rick
     * @date 2014-5-20
     * @description:
     * @param req
     * @param sessionKey
     * @return
     */
    private static Object sessionGet(HttpServletRequest req,String sessionKey){
        return req.getSession().getAttribute(sessionKey);
    }
    /**
     *
     * @title sessionSet
     * @author Rick
     * @date 2014-5-20
     * @description:
     * @param request
     * @param sessionKey
     * @param value
     */
    private static void sessionSet(HttpServletRequest request,String sessionKey,Object value){
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, value);
    }
    /**
     *
     * @title sessionRemove
     * @author Rick
     * @date 2014-5-20
     * @description:
     * @param request
     * @param sessionKey
     */
    private static void sessionRemove(HttpServletRequest request,String sessionKey){
        HttpSession session = request.getSession();
        session.removeAttribute(sessionKey);
    }
}
