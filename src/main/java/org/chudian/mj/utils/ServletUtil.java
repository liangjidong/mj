package org.chudian.mj.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by onglchen on 8/29/15.
 */
public class ServletUtil {
    static String projectName = "mj";
    static String pre_word = "http://";
    public static   String getRequestUrl(HttpServletRequest request){
        return pre_word + request.getServerName() +":" + request.getServerPort() + "/" + projectName +"/";
    }
}
