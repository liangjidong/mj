package org.chudian.mj.socket;

/**
 * Created by onglchen
 * on 15-3-26.
 */

import java.io.*;
import java.net.Socket;

public class FileCilent2 {

    /**
     * @param args
     */
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        try {
//            Socket client = new Socket("127.0.0.1", 7777);
//            InputStream is =  client.getInputStream();
//            OutputStream os = client.getOutputStream();
//            String filepath = "/home/onglchen/temp/upload/2.jpg";
//            File file = new File(filepath);
//            String filename = file.getName();
//            String head = "Content-Length="+ file.length() + ";filename="+ file.getName() +"\r\n";
//            os.write(head.getBytes());
//
//            System.out.println("send's file name:"+filename);
//            //1、发送文件名
//            os.write(filename.getBytes());
//            FileInputStream fis = new FileInputStream(file);
//            byte[] b = new byte[1024];
//            int length = 0;
//            int total_length = length;
//            while((length=fis.read(b))!=-1){
//                //2、把文件写入socket输出流
//                os.write(b, 0, length);
//                total_length = total_length + length;
//            }
//            System.out.println("total_length = " + total_length);
//            os.close();
//            fis.close();
//            is.close();
//            System.out.println("send over");
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
        String filepath = "/home/onglchen/temp/upload/2.jpg";
        File uploadFile= new File(filepath);
        try {
            String head = "Content-Length="+ uploadFile.length() + ";filename="+ uploadFile.getName() +"\r\n";
            Socket socket = new Socket("127.0.0.1",7878);
            OutputStream outStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outStream.write(head.getBytes());

            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
            String response = StreamTool.readLine(inStream);
            //  System.out.println("response=" + response);
            String[] items = response.split(";");
            String responseid = items[0].substring(items[0].indexOf("=")+1);
            String position = items[1].substring(items[1].indexOf("=")+1);

            RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
            fileOutStream.seek(Integer.valueOf(position));
            byte[] buffer = new byte[1024];
            int len = -1;
            int length = Integer.valueOf(position);
            while((len = fileOutStream.read(buffer)) != -1){
                outStream.write(buffer, 0, len);
                length += len;

            }

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = null;
            try {
                object = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(object != null){
                SocketResponse socketResponse = (SocketResponse)object;
                System.out.println("status=" + socketResponse.getStatus());
                System.out.println("points=" + socketResponse.getPoint());
                System.out.println("videourl=" + socketResponse.getVideourl());

            }



            fileOutStream.close();
            outStream.close();
            inStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传文件
     * @param uploadFile
     */
    private void uploadFile(final File uploadFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String head = "Content-Length="+ uploadFile.length() + ";filename="+ uploadFile.getName() +"\r\n";
                    Socket socket = new Socket("192.168.1.78",7878);
                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String responseid = items[0].substring(items[0].indexOf("=")+1);
                    String position = items[1].substring(items[1].indexOf("=")+1);

                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while((len = fileOutStream.read(buffer)) != -1){
                        outStream.write(buffer, 0, len);
                        length += len;
                    }
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
