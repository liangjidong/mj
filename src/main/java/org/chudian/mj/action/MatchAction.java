package org.chudian.mj.action;


import org.chudian.mj.bean.Match;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.MjproductService;
import org.chudian.mj.service.PictureService;
import org.chudian.mj.socket.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.Socket;

/**
* Created by onglchen
* on 15-3-5.
*/
@Controller
public class MatchAction {
    Match match;
    String baseurl = "/home/onglchen/proenv/userlib";
   String baseipurl = "http://120.25.241.211:8080/mj" ;
    //String baseipurl = "http://192.168.0.122:8081/mj" ;
    String TrainDataDir = baseurl + "/TrainDataDir/";

    @Autowired
    private PictureService pictureService;
    @Autowired
    private MjproductService mjproductService;


    /**
     *  java socket 用的
     * @return
     */


 /*   @ResponseBody
    @RequestMapping(value = "/api/train", method = RequestMethod.POST)
    public Object train() {
        Socket socket;
        if (match == null) {
            match = new Match();
        }
        match.trainOnly();
        try {
            socket = new Socket(UploadUtil.LOCAL_IP,7878);
            UploadUtil.sendKeepOn(1,socket);
            UploadUtil.sendReTrain(1,socket);
            socket.close();
            mjproductService.updateTrainStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return  new Response(Status.SUCCESS);
	}*/

    /**
     *  C++ 并发用的
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/train", method = RequestMethod.POST)
    public Object train() {
        Socket socket;
        if (match == null) {
            match = new Match();
        }
       // match.trainOnly();
        try {
//            socket = new Socket(UploadUtil.SERVER_IP2,7004);
            socket = new Socket("120.27.195.37",7004);
//            socket.setSoTimeout(3000);
            UploadUtil.sendReTrain(socket);
            socket.close();
            mjproductService.updateTrainStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new Response(Status.SUCCESS);
    }


}
