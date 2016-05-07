package org.chudian.mj.action;


import org.chudian.mj.bean.Match;
import org.chudian.mj.service.PictureService;
import org.chudian.mj.utils.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

/**
* Created by onglchen
* on 15-3-5.
*/
@Controller
public class Match2Action {
    Match match;
    @Autowired
    private PictureService pictureService;




    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toFile(String base64Code, String targetPath)
            throws Exception {

//        byte[] buffer = base64Code.getBytes();
//        FileOutputStream out = new FileOutputStream(targetPath);
//        out.write(buffer);
//        out.close();

        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64Code);
        FileOutputStream os = new FileOutputStream(targetPath);
        os.write(bs);
        os.close();
    }

    /**
     * Read an input stream into a string
     * @param in
     * @return
     * @throws IOException
     */
    static public String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
     * @author temdy
     * @Date 2015-01-26
     * @param base64 图片Base64数据
     * @param path 图片路径
     * @return
     */
    public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null) { // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @Descriptionmap 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author temdy
     * @Date 2015-01-26
     * @param path 图片路径
     * @return
     */
    public static String imageToBase64(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }


    private boolean saveImageToDisk(byte[] data,String photoPathFile, String imageName) throws IOException{
        int len = data.length;
        //
        // 写入到文件
        FileOutputStream outputStream = new FileOutputStream(new File(photoPathFile,imageName));

        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        //
        return true;
    }
    private byte[] decode(String imageData) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] data = decoder.decodeBuffer(imageData);
        for(int i=0;i<data.length;++i)
        {
            if(data[i]<0)
            {
                //调整异常数据
                data[i]+=256;
            }
        }
        //
        return data;
    }



    @ResponseBody
    @RequestMapping(value = "/api/picture/upload3", method = RequestMethod.POST)
    public Object uploadImages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String path = ServletUtil.getRequestUrl(request);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("status","0");
        String imageName = "temp.jpg";
        String filePath = request.getSession().getServletContext()
                .getRealPath("/")
                + "upload/picture/";
        String pic_temp = filePath + imageName;
        String imageData = request.getParameter("data");
        int success = 0;
        String message = "";
        if (null == imageData || imageData.length() < 100) {
            // 数据太短，明显不合理
            message = "上传失败,数据太短或不存在";
        } else {
            // 去除开头不合理的数据
            imageData = imageData.substring(31);
            System.out.println(imageData);
            imageData = URLDecoder.decode(imageData, "UTF-8");

            //System.out.println(imageData);
            byte[] data = decode(imageData);

            int len = data.length;
            int len2 = imageData.length();
           
            saveImageToDisk(data, filePath, imageName);
            if (match == null) {
                match = new Match();
                match.train();
            }
            String result = this.match.match_java(pic_temp,
                    Match.base_url + "/picFeatures", Match.base_url
                            + "/TrainDataDir");
            System.out.println("match Reslut ======" + result);
            String[] result_arr = result.split("&&");
            int length_arr = result_arr.length;
            int status;

            String fileName = "";
            String pictureUrl = "";
            String webUrl = "";
            if (length_arr > 1) {
                status = 1;
                fileName = result_arr[0];
                map = pictureService.getVidioByPicName(fileName, path);
                map.put("status",status+"");
                
            }else {
                map.put("status","0");

            }
            
        }
        return map;

    }





}
