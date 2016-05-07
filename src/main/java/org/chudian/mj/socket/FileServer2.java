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

public class FileServer2 {

    /**
     * @param args
     */
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        try {
//            ServerSocket server = new ServerSocket(7777);
//            Socket socket = new Socket();
//            while(true){
//                socket = server.accept();
//                InputStream is =  socket.getInputStream();
//
//                OutputStream os = socket.getOutputStream();
//
//
//
//                PushbackInputStream inStream = new PushbackInputStream(
//                        is);
//                // 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=
//                // 如果用户初次上传文件，sourceid的值为空。
//                String head = StreamTool.readLine(inStream);
//                System.out.println(head);
//                String filelength = "";
//                String filename = "";
//                if (head != null) {
//                    // 下面从协议数据中读取各种参数值
//                    String[] items = head.split(";");
//                     filelength = items[0].substring(items[0].indexOf("=") + 1);
//                     filename = items[1].substring(items[1].indexOf("=") + 1);
//                    System.out.println("length=" + filelength);
//                    System.out.println("filename=" + filename);
//                }
//
//
//
//                byte[] b = new byte[1024];
//                //1、得到文件名
//                int a = is.read(b);
//               // String filename2 = new String(b, 0, a);
//                System.out.println("接受到的文件名为："+filename);
//
////                String houzhui = filename.substring(filename.indexOf("."), filename.length());
////                String rand = String.valueOf((int) (Math.random() * 100000));
//              //  filename = rand+houzhui;
//                System.out.println("新生成的文件名为:"+filename);
//                FileOutputStream fos = new FileOutputStream("/home/onglchen/temp/"+filename);
//                int length = 0;
//                int total_length = length;
//                while((length=is.read(b))!=-1){
//                    //2、把socket输入流写到文件输出流中去
//                    fos.write(b, 0, length);
//                    total_length = total_length + length;
////                    if(total_length >= 15638){
////                        break;
////                    }
//                }
//                System.out.println("total_length= " + total_length);
//                //fos.flush();
//                fos.close();
//                os.flush();
//                os.close();
//                is.close();
//                socket.close();
//            }
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
        ServerSocket ss =null;
        Socket socket = null;
        Match match = new Match();

        try {
            ss = new ServerSocket(7878);
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

                File file = new File("/home/onglchen/temp/"+filename);
                int position = 0;


                OutputStream outStream = socket.getOutputStream();
                String response = "sourceid="+ id+ ";position="+ position+ "\r\n";
                //服务器收到客户端的请求信息后，给客户端返回响应信息：sourceid=1274773833264;position=0
                //sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
                outStream.write(response.getBytes());

                RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
                if(position==0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度
                fileOutStream.seek(position);//移动文件指定的位置开始写入数据
                byte[] buffer = new byte[1024];
                long total_length = Integer.valueOf(filelength);
                int len = -1;
                int length = position;
                while( (len=inStream.read(buffer)) != -1){//从输入流中读取数据写入到文件中
                    fileOutStream.write(buffer, 0, len);
                    length += len;
                    if(length >= total_length){
                        System.out.println("文件传输完成");
                        break;
                    }
                }
                System.out.println("length=" + length);

               // match.train();
                String result = match.match_java("/home/onglchen/temp/2.jpg", "/home/onglchen/proenv/userlib/picFeatures", "/home/onglchen/proenv/userlib/TrainDataDir");
                System.out.println("match result: " + result);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
                SocketResponse socketResponse = new SocketResponse();
                if(result != null){
                    socketResponse.setPoint(result);
                    socketResponse.setStatus(1);
                }
                socketResponse.setVideourl("sdfdf");
                objectOutputStream.writeObject(socketResponse);

                fileOutStream.close();
                inStream.close();
                outStream.close();
                file = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {}
        }
    }

}


class SocketTask2 implements Runnable {
    private Socket socket;

    public SocketTask2(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
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

                File file = null;
                int position = 0;


                OutputStream outStream = socket.getOutputStream();
                String response = "sourceid="+ id+ ";position="+ position+ "\r\n";
                //服务器收到客户端的请求信息后，给客户端返回响应信息：sourceid=1274773833264;position=0
                //sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
                outStream.write(response.getBytes());

                RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
                if(position==0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度
                fileOutStream.seek(position);//移动文件指定的位置开始写入数据
                byte[] buffer = new byte[1024];
                int len = -1;
                int length = position;
                while( (len=inStream.read(buffer)) != -1){//从输入流中读取数据写入到文件中
                    fileOutStream.write(buffer, 0, len);
                    length += len;
                    System.out.println("length= " + length);
                }
                System.out.println("length= " + length);




                fileOutStream.close();
                inStream.close();
                outStream.close();
                file = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {}
        }
    }

}


