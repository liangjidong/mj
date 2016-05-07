package org.chudian.mj.socket;

/**
 * Created by onglchen
 * on 15-3-26.
 */

import java.io.*;
import java.net.Socket;

public class FileCilent {



    public static void main(String[] args) {
        String filepath = "/home/onglchen/temp/upload/10.jpg";
        File uploadFile= new File(filepath);

        FileCilent fileCilent = new FileCilent();
        String response = fileCilent.uploadFile(uploadFile,"182.92.10.18",7878);

        System.out.println("response=  " + response);

    }


    /**
     * 上传文件
     * @param uploadFile
     */
    public String  uploadFile(final File uploadFile ,String ip , int port) {
//                SocketResponse socketResponse =new SocketResponse();
//                socketResponse.setStatus(-1);
                     String reponse2 = "";
        try {
                    String head = "Content-Length="+ uploadFile.length() + ";filename="+ uploadFile.getName() +"\r\n";
                    Socket socket = new Socket(ip,port);
                    OutputStream outStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();


                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String position = items[1].substring(items[1].indexOf("=") + 1);

                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while((len = fileOutStream.read(buffer)) != -1){
                        outStream.write(buffer, 0, len);
                        length += len;
                    }


                    reponse2 = StreamTool.readLine(inStream);




//                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                    Object object = null;
//                    try {
//                        object = objectInputStream.readObject();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    if(object != null){
//                         socketResponse = (SocketResponse)object;
////                        System.out.println("status=" + socketResponse.getStatus());
////                        System.out.println("points=" + socketResponse.getPoint());
//
//
//                    }
//                     System.out.println("videourl=" + socketResponse.getVideourl());

                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    inputStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                 return reponse2;

    }



}


 class StreamTool2 {

    public static void save(File file, byte[] data) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    public static String readLine(PushbackInputStream in) throws IOException {
        char buf[] = new char[128];
        int room = buf.length;
        int offset = 0;
        int c;
        loop:
        while (true) {
            switch (c = in.read()) {
                case -1:
                case '\n':
                    break loop;
                case '\r':
                    int c2 = in.read();
                    if ((c2 != '\n') && (c2 != -1)) in.unread(c2);
                    break loop;
                default:
                    if (--room < 0) {
                        char[] lineBuffer = buf;
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);

                    }
                    buf[offset++] = (char) c;
                    break;
            }
        }
        if ((c == -1) && (offset == 0)) return null;
        return String.copyValueOf(buf, 0, offset);
    }

    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
}