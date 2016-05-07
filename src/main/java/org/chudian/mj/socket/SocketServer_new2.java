package org.chudian.mj.socket;

import com.alibaba.fastjson.JSONObject;
import org.chudian.mj.bean.Match;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.utils.SocketDB;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer_new2
{
    private String uploadPath = "D:/uploadFile/";
    private ExecutorService executorService;
    private ServerSocket ss = null;
    private int port;
    private boolean quit;


    public static int Service_Port = 80;
    public static double Time_Out = 60000.0D;
    private Match match;

    public SocketServer_new2(int port, Match match)
    {
        this.port = port;
        this.match = match;

        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * 50);
    }

    public void start() throws Exception
    {
        this.ss = new ServerSocket(this.port);
        while (!this.quit) {
            Socket socket = this.ss.accept();

            this.executorService.execute(new SocketTask2(socket, this.match));
        }
    }

    public void quit()
    {
        this.quit = true;
        try {
            this.ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Match match = new Match();
        match.train();
        SocketServer_new2 server = new SocketServer_new2(Service_Port, match);
        server.start();
    }
    private class SocketTask2 implements Runnable {
        private Socket socket;
        private Match match;

        public SocketTask2(Socket socket, Match match) { this.socket = socket;
            this.match = match;
        }

        public void run()
        {
            RandomAccessFile fileOutStream = null;
            PushbackInputStream inStream = null;
            OutputStream outStream = null;
            File file = null;
            double time_now = 0;
            double time_start = 0;
            String hello = "";
            try {
                socket.setKeepAlive(true);
                inStream = new PushbackInputStream(socket.getInputStream());
                hello = "accepted connenction from " + socket.getInetAddress()
                        + " @ " + socket.getPort();
                while (true) {
                    // why suspend 10 ms
                    Thread.sleep(10);

                    if (time_start != 0) {
                        time_now = System.currentTimeMillis() - time_start;
                    }
                    if (time_now >= Time_Out) {
                        break;
                    }

                    String keepon = ""; // 长连接保持字段
                    keepon = StreamTool.readLine(inStream);
                    if (keepon != null) {
                        System.out.println("keepon = " + keepon);
                        // what is 1
                        if (!keepon.equals("1")) {
                            break;
                        }
                    }

                    String reload = ""; // 重新加载索引
                    reload = StreamTool.readLine(inStream);
                    if (reload != null) {
                        if (reload.equals("1")) {
                            match.reLoad_java(Match.base_url + "/index.data");
                            System.out.println("reLoad index.data success");
                            continue;
                        }
                    }

                    // 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=;fileType=0
                    // 0:video 1:3D Model
                    // 如果用户初次上传文件，sourceid的值为空。
                    String head = StreamTool.readLine(inStream);

                    if (head != null) {
                        long time_all_start = System.currentTimeMillis();
                        System.out.println(hello + head + ";"
                                + System.currentTimeMillis());
                        time_start = System.currentTimeMillis();
                        // 下面从协议数据中读取各种参数值
                        String[] items = head.split(";");
                        String filelength = items[0].substring(items[0]
                                .indexOf("=") + 1);
                        String filename = items[1].substring(items[1]
                                .indexOf("=") + 1);

                        String timestamp = items[2].substring(items[2]
                                .indexOf("=") + 1);
                        Long id = System.currentTimeMillis();

                        String filePath = Match.base_url + "/upload/"
                                + filename;
                        file = new File(filePath);
                        int position = 0;

                        outStream = socket.getOutputStream();

                        fileOutStream = new RandomAccessFile(file, "rwd");
                        if (position == 0)
                            fileOutStream
                                    .setLength(Integer.valueOf(filelength));// 设置文件长度
                        fileOutStream.seek(position);// 移动文件指定的位置开始写入数据
                        byte[] buffer = new byte[8192];
                        long total_length = Integer.valueOf(filelength);
                        int len = -1;
                        int length = position;

                        long start_transfer = System.currentTimeMillis();
                        while ((len = inStream.read(buffer)) != -1) {// 从输入流中读取数据写入到文件中
                            fileOutStream.write(buffer, 0, len);
                            length += len;
                            if (length >= total_length) {
                                System.out.println("文件传输完成");
                                break;
                            }
                        }
                        if(fileOutStream != null){
                            fileOutStream.close();
                            fileOutStream = null;
                        }


                        long end_transfer = System.currentTimeMillis();
                        long time_transfer = end_transfer - start_transfer;

                        System.out.println("length=" + length);

                        long start_match = System.currentTimeMillis();

                        String result = this.match.match_java(filePath, "/home/onglchen/proenv/userlib/picFeatures", "/home/onglchen/proenv/userlib/TrainDataDir");
                        String[] result_arr = result.split("&&");
                        int length_arr = result_arr.length;
                        String videoUrl = "";
                        String points = "";
                        int status = -1;
                        System.out.println("result_length = " + length_arr);

                        String fileName = "";
                        String pictureUrl = "";
                        String webUrl = "";
                        String mjProductId = "";
                        if (length_arr > 1) {
                            status = 1;
                            System.out.println("fileName == " + filename);
                            fileName = result_arr[0];
                            pictureUrl = Picture.CACHE_URL + "/" + fileName + ".jpg";

                            SocketDB socketDB = new SocketDB();
                            Map result_db = socketDB.getVideoUrl(fileName);
                            videoUrl = (String)result_db.get("videoUrl");
                            webUrl = (String)result_db.get("webUrl");
                            mjProductId = (String)result_db.get("mjProductId");
                            System.out.println("videoUrl==" + videoUrl);
                            if (videoUrl.equals("")) {
                                videoUrl = "/files/llsw.mp4";
                            }
                            if (webUrl.equals("")) {
                                videoUrl = "www.baidu.com";
                            }

                            for (int i = 1; i < length_arr; i++)
                            {
                                points = points + result_arr[i] + "&&";
                            }

                            long end_match = System.currentTimeMillis();
                            long time_match = end_match - start_match;
                            System.out.println("match result: " + result);
                        }

                        double time_all = System.currentTimeMillis() - time_all_start;
                        System.out.println("times = " + System.currentTimeMillis());
                        String final_reponse = "status=" + status + ";" + "points=" + points + ";" + "videourl=" + videoUrl + ";" + "fileName=" + fileName + ";" + "pictureUrl=" + pictureUrl + "\r\n";

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("status", Integer.valueOf(status));
                        jsonObject.put("points", points);
                        jsonObject.put("videoUrl", videoUrl);
                        jsonObject.put("fileName", fileName);
                        jsonObject.put("pictureUrl", pictureUrl);
                        jsonObject.put("webUrl", webUrl);
                        jsonObject.put("mjProductId", mjProductId);
                        String json_result = jsonObject.toJSONString() + "\r\n";
                        System.out.println("json:==" + json_result);
                        System.out.println("normal:==" + final_reponse);



                        outStream.write(json_result.getBytes());
                    }
                }
            }
            catch (Exception localIOException1) {
                localIOException1.printStackTrace();
            } finally {
                try {
                    if ((this.socket != null) && (!this.socket.isClosed())) this.socket.close();
                    if (fileOutStream != null)
                        fileOutStream.close();
                    if (inStream != null)
                        inStream.close();
                    if (outStream != null)
                        outStream.close();
                    file = null;
                }
                catch (IOException localIOException2)
                {
                }
            }
        }
    }
}