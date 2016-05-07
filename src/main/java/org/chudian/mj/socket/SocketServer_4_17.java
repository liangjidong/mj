package org.chudian.mj.socket;

import org.chudian.mj.bean.Match;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by onglchen
 * on 15-3-30.
 */
public class SocketServer_4_17 {
    private String uploadPath = "D:/uploadFile/";
    private ExecutorService executorService;// 线程池
    private ServerSocket ss = null;
    private int port;// 监听端口
    private boolean quit;// 是否退出

    public static int  Service_Port = 7999;
    public static double Time_Out = 60000 ;

    private Match match;

    public SocketServer_4_17(int port, Match match) {
        this.port = port;
        this.match = match;
        // 初始化线程池
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * 50);
    }

    // 启动服务
    public void start() throws Exception {
        ss = new ServerSocket(port);
        while (!quit) {
            Socket socket = ss.accept();// 接受客户端的请求
            // 为支持多用户并发访问，采用线程池管理每一个用户的连接请求
            executorService.execute(new SocketTask(socket, this.match));// 启动一个线程来处理请求
        }
    }

    // 退出
    public void quit() {
        this.quit = true;
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Match match = new Match();
        match.train();
        SocketServer_4_17 server = new SocketServer_4_17(Service_Port, match);
        server.start();
    }

    private class SocketTask implements Runnable {
        private Socket socket;
        private Match match;

        public SocketTask(Socket socket, Match match) {
            this.socket = socket;
            this.match = match;
        }

        @Override
        public void run() {
            RandomAccessFile fileOutStream = null;
            PushbackInputStream inStream = null;
            OutputStream outStream = null;
            File file = null;
            double time_now = 0;
            double time_start = 0;
            String hello = "";
            try {
                socket.setKeepAlive(true);
                inStream = new PushbackInputStream(
                        socket.getInputStream());
                hello = "accepted connenction from "
                        + socket.getInetAddress() + " @ " + socket.getPort();
                while (true) {
                    Thread.sleep(10);

                    if (time_start != 0) {
                        time_now = System.currentTimeMillis() - time_start;
                    }
                    System.out.println(time_now);
                    if (time_now >= Time_Out) {
                        break;
                    }

                    String keepon = "";
                    keepon = StreamTool.readLine(inStream);
                    System.out.println("keepon = " + keepon);

                    if(!keepon.equals("1")){
                        break;
                    }
                    // 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=
                    // 如果用户初次上传文件，sourceid的值为空。
                    String head = StreamTool.readLine(inStream);

                    if (head != null) {
                        double time_all_start = System.currentTimeMillis();
                        System.out.println(hello + head);
                        time_start = System.currentTimeMillis();
                        // 下面从协议数据中读取各种参数值
                        String[] items = head.split(";");
                        String filelength = items[0].substring(items[0].indexOf("=") + 1);
                        String filename = items[1].substring(items[1].indexOf("=") + 1);
                        String timestamp = items[2].substring(items[2].indexOf("=") + 1);
                        Long id = System.currentTimeMillis();

                        String filePath = "/home/onglchen/proenv/userlib/upload/" + filename;
                        file = new File(filePath);
                        int position = 0;


                        outStream = socket.getOutputStream();

                        fileOutStream = new RandomAccessFile(file, "rwd");
                        if (position == 0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度
                        fileOutStream.seek(position);//移动文件指定的位置开始写入数据
                        byte[] buffer = new byte[8192];
                        long total_length = Integer.valueOf(filelength);
                        int len = -1;
                        int length = position;

                        long start_transfer = System.currentTimeMillis();
                        while ((len = inStream.read(buffer)) != -1) {//从输入流中读取数据写入到文件中
                            fileOutStream.write(buffer, 0, len);
                            length += len;
                            if (length >= total_length) {
                                System.out.println("文件传输完成");
                                break;
                            }
                        }
                        long end_transfer = System.currentTimeMillis();
                        long time_transfer = end_transfer - start_transfer;

                        System.out.println("length=" + length);

                        long start_match = System.currentTimeMillis();
                        String result = this.match.match_java(filePath, "/home/onglchen/proenv/userlib/picFeatures", "/home/onglchen/proenv/userlib/TrainDataDir");
                        long end_match = System.currentTimeMillis();
                        long time_match = end_match - start_match;
                        int status = -1;
                        String videourl = "www.baidu.com";
                        if (result != null && !result.equals("")) {
                            status = 1;
                        }
//                        double time_all = System.currentTimeMillis() - time_all_start;
//                        String final_reponse = result + ";" + "videourl=" + videourl + ";"+ "timestamp="   + timestamp + ";" +
//                                "time_transfer=" + time_transfer + ";" + "time_match=" + time_match +  ";" + "time_all=" + time_all + "\r\n";
//
                        outStream.write(result.getBytes());


                    }
            }
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) socket.close();
                    if (fileOutStream != null)
                        fileOutStream.close();
                    if (inStream != null)
                        inStream.close();
                    if (outStream != null)
                        outStream.close();
                    file = null;
                } catch (IOException e) {
                }
            }


        }
    }
}

