package org.chudian.mj.socket;

/**
 * Created by onglchen
 * on 15-3-26.
 */

import org.chudian.mj.bean.Match;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

;

public class FileServer {

    /**
     * @param args
     */


    public static void main(String[] args) {
        ServerSocket ss = null;
        Socket socket = null;
        Match match = new Match();
       // match.train();
        try {
            ss = new ServerSocket(7878);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true)
        {
            try {
                socket = ss.accept();
                System.out.println("accepted connenction from "
                        + socket.getInetAddress() + " @ " + socket.getPort());
                PushbackInputStream inStream = new PushbackInputStream(
                        socket.getInputStream());
                // 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=
                // 如果用户初次上传文件，sourceid的值为空。
                String head = StreamTool.readLine(inStream);
                System.out.println(head);
                if (head != null) {
                    // 下面从协议数据中读取各种参数值
                    String[] items = head.split(";");
                    String filelength = items[0].substring(items[0].indexOf("=") + 1);
                    String filename = items[1].substring(items[1].indexOf("=") + 1);
                    Long id = System.currentTimeMillis();

                    String filePath = "/home/onglchen/proenv/userlib/upload/" + filename;
                    File file = new File(filePath);
                    int position = 0;


                    OutputStream outStream = socket.getOutputStream();
                    String response = "sourceid=" + id + ";position=" + position + "\r\n";
                    //服务器收到客户端的请求信息后，给客户端返回响应信息：sourceid=1274773833264;position=0
                    //sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
                    outStream.write(response.getBytes());

                    RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
                    if (position == 0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度
                    fileOutStream.seek(position);//移动文件指定的位置开始写入数据
                    byte[] buffer = new byte[1024];
                    long total_length = Integer.valueOf(filelength);
                    int len = -1;
                    int length = position;
                    while ((len = inStream.read(buffer)) != -1) {//从输入流中读取数据写入到文件中
                        fileOutStream.write(buffer, 0, len);
                        length += len;
                        if (length >= total_length) {
                            System.out.println("文件传输完成");
                            break;
                        }
                    }
                    System.out.println("length=" + length);


                    String result = match.match_java(filePath, "/home/onglchen/proenv/userlib/picFeatures", "/home/onglchen/proenv/userlib/TrainDataDir");
                    System.out.println("match result: " + result);
                    int status = -1;
                    String videourl = "www.baidu.com";
                    if (result != null && !result.equals("")) {
                        status = 1;
                    }
                    String final_reponse = "status=" + status + ";" + "points=" + result + ";" + "videourl=" + videourl;
                    outStream.write(final_reponse.getBytes());


//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
//                SocketResponse socketResponse = new SocketResponse();
//                if(result != null){
//                    socketResponse.setPoint(result);
//                    socketResponse.setStatus(1);
//                }
//                socketResponse.setVideourl("sdfdf");
//                objectOutputStream.writeObject(socketResponse);

                    fileOutStream.close();
                    inStream.close();
                    outStream.close();
                    file = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}






