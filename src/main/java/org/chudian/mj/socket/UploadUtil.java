package org.chudian.mj.socket;

/**
 * Created by onglchen on 4/4/15.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.chudian.mj.jni.SocketClient;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;


public class UploadUtil {


    public final static String SERVER_IP="182.92.10.18";
    public final static String SERVER_IP2="120.25.241.211";
    public final static String SERVER_IP_DEV = "120.24.69.164";
    public final static String LOCAL_IP = "127.0.0.1";
    public final static int  SERVER_PORT=7878;

    public static UploadUtil util=new UploadUtil();

    public static UploadUtil getInstance()
    {
        return util;
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






    public String  uploadFile(final File uploadFile ,String ip , int port) {

        String reponse2 = "";
        try {
            String head = "Content-Length="+ uploadFile.length() + ";filename="+ uploadFile.getName() +"\r\n";
            Socket socket = new Socket(ip,port);
            OutputStream outStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();


            outStream.write(head.getBytes());

            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
            String response = readLine(inStream);
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


            reponse2 = readLine(inStream);


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

    //示例方法  ：这是改成了长连接 后的调用示例。 一次连接，多次上传。
    public static void main(String[] args)  {
      //  Socket socket = new Socket("120.25.241.211",7004);
       // UploadUtil.sendReTrain(socket);

       // byte[] data = getBytes("/home/onglchen/proenv/userlib/7.jpg");
      //  byte[] data2 = getBytes("/home/onglchen/temp/source/2.jpg");

        Socket socket = null;
        try {
            socket = new Socket(SERVER_IP2,7004);
        } catch (IOException e) {
            System.out.println("1111111111");
            e.printStackTrace();
        }
        try {
            socket.setSoTimeout(100000);
        } catch (SocketException e) {
            System.out.println("22222222222");
            e.printStackTrace();
        }
        try {
            UploadUtil.sendReTrain(socket);
        } catch (IOException e) {
            System.out.println("33333333333");
            e.printStackTrace();
        }

        /*UploadUtil uploadUtil = new UploadUtil();
        if(UploadUtil.sendKeepOn(1,socket))
        {   UploadUtil.sendReTrain(0, socket);
            String response = UploadUtil.upaload(data, socket);
            System.out.println("response=  " + response);
        }

        if(UploadUtil.sendKeepOn(0,socket))
        {
            String response2 = UploadUtil.upaload(data2, socket);
            System.out.println("response2=  " + response2);
        }*/



    }

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public void uploadMethod(){
        try {
//        Socket socket = new Socket(SERVER_IP,SERVER_PORT);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int  isEistsByC( byte[] jpgData){
        SocketClient socketClient = new SocketClient();
        socketClient.Init();
        String result  = "";
        result = socketClient.Upload(jpgData,jpgData.length);
        System.out.println("Result == " + result);
        socketClient.Release();
        if(result.equals("") || result== null){
            return -1;
        }JSONObject json= (JSONObject) JSON.parse(result);
        int status = json.getInteger("status");
        String name = json.getString("pic_name");
        System.out.println("name = " + name + ", status = " + status);
        if(status == -1){
            return 1; //不存在，可以上传
        }else if(status == 1){
            return 0;//已存在相同照片，不可以上传
        }
        return -1;//与服务器通信出错
    }

    public static void isEists( byte[] jpgData) throws IOException {
        Socket socket = new Socket("120.25.241.211",7004);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        int len = jpgData.length;
        String header = "{\"Content-Length\":\"" + len + ",\"picture_length\":\""+ len + ",\"picture_name\":\"test\"}";
        int headerLen =header.length();
        String format_len = new DecimalFormat("000000000000").format(headerLen);
        String header2 = format_len + "{\"Content-Length\":\"" + len + "\",\"picture_length\":\"" + len + "\",\"picture_name\":\"test\"}";

        outputStream.write(header2.getBytes(),0,header2.length());
        System.out.println("Send Head " + header2);

        outputStream.write(jpgData, 0, len);
        byte[] readBuff = new byte[255];
        System.out.println("Send Over " );
        int a;
//        inputStream.read(readBuff);
//        System.out.println("Reply == " + readBuff);

        /*StringBuilder sb = new StringBuilder();
        try {
            socket.setSoTimeout(5000);
            while ((a = socket.getInputStream().read(readBuff)) != -1) {
                sb.append(new String(readBuff, 0, a));

            }
        } catch (Exception e) {
        }
        System.out.println("Result == " + sb);*/
    }

    public static void sendReTrain( Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
//        String jsondata="{\"Content-Length\":\"0\",\"picture_length\":\"0\",\"picture_name\":\"test\",\"retrain\":\"1\"}";
        String keepOnstr = "000000000079{\"Content-Length\":\"0\",\"picture_length\":\"0\",\"picture_name\":\"test\",\"retrain\":\"1\"}";
        outputStream.write(keepOnstr.getBytes());

       /* byte[] buffer = new byte[2048];
        int readBytes = 0;
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Start ::==");
        int i = 0;
        while ((readBytes = inputStream.read(buffer)) >0){
            System.out.println("i == " + i);
            i ++;
            stringBuilder.append(new String(buffer,0,readBytes));
        }
        System.out.println("TTTTTest === " + stringBuilder.toString());*/

    }

    public static void sendReTrain(int reTrain, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        String keepOnstr = reTrain +"\r\n";
        outputStream.write(keepOnstr.getBytes());
    }

    public static boolean sendKeepOn(int keepon, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        String keepOnstr = keepon +"\r\n";
        outputStream.write(keepOnstr.getBytes());
        if(keepon == 1){
            return true;
        } else{
            return  false;
        }

    }

    //这是我修改了的方法
    public static  String upaload(byte[] data,Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        String reponse2 = "";
        String head = "Content-Length="+ data.length + ";filename="+ "test_socket_file.jpg;timestamp=" +
                System.currentTimeMillis()
                +"\r\n";
        try {
            outputStream.write(head.getBytes());

        PushbackInputStream inStream = new PushbackInputStream(inputStream);

        outputStream.write(data, 0, data.length);
        reponse2 = readLine(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponse2;
    }


    public String  uploadByteArray(byte[] data) {

        String reponse2 = "";
        try {
            String head = "Content-Length="+ data.length + ";filename="+ "test_socket_file.jpg;timestamp:" +
                    System.currentTimeMillis()
                    +"\r\n";

            Socket socket = new Socket(SERVER_IP,SERVER_PORT);
            OutputStream outStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();


            outStream.write(head.getBytes());

            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
            String response = readLine(inStream);
            String[] items = response.split(";");
            String position = items[1].substring(items[1].indexOf("=") + 1);


            outStream.write(data, 0, data.length);

            reponse2 = readLine(inStream);



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
