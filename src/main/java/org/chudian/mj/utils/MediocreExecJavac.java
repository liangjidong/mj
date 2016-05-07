package org.chudian.mj.utils;

import java.io.*;


public class MediocreExecJavac {

    public void runCmd(String command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null)
                System.out.println(line);

            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean transfer(String infile,String outfile) {
        String gptomp4 = "ffmpeg -i "+infile+" -c:v libx264 -c:a aac -ac 2 -ar 48000 -strict -2 -f mp4"+outfile;
        String avitoflv = "ffmpeg -i "+infile+" -ar 22050 -ab 56 -f flv -y -s 320x240 "+outfile;
        String flvto3gp = "ffmpeg -i " + infile + " -ar 8000 -ac 1 -acodec amr_nb -vcodec h263 -s 176x144 -r 12 -b 30 -ab 12 " + outfile;
        String avito3gp = "ffmpeg -i " + infile + " -ar 8000 -ac 1 -acodec amr_nb -vcodec h263 -s 176x144 -r 12 -b 30 -ab 12 " + outfile;
        String avitojpg = "ffmpeg -i " + infile + " -y -f image2 -ss 00:00:10 -t 00:00:01 -s 350x240 " + outfile;

        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(gptomp4);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null)
                System.out.println(line);

            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

    public static String readFile(String fileName,int id) {
        String dataStr = "";
        FileInputStream fis = null;

        try {
            FileReader file = new FileReader(fileName);//建立FileReader对象，并实例化为fr
            BufferedReader br=new BufferedReader(file);//建立BufferedReader对象，并实例化为br
            String Line=br.readLine();//从文件读取一行字符串
            dataStr=Line;
            br.close();//关闭BufferedReader对象
        } catch(Exception e) {

        } finally {
            try {
                if(fis!=null)
                    fis.close();
            } catch(Exception e) {}
        }
        return dataStr;
    }

//    public String readtime(String file) {
//        String str="/root/Desktop/info.txt";
//        String timelen = "";
//        String cmd = "timelen "+file;
//
//        runCmd(cmd);
//        timelen=readFile(str,1);
//
//        return timelen;
//    }

//    public static void main(String args[]) {
//        MediocreExecJavac me = new MediocreExecJavac();
//        String baseurl = "/home/onglchen/temp/";
//        String infile = baseurl + "1.mp4";
//        String outfile = baseurl + "2.flp";
//
//        String timelen = me.readtime(infile);
//        System.out.println("timelen is :" + timelen);
//
//        if(me.transfer(infile,outfile)) {
//            System.out.println("the transfer is ok!");
//        } else {
//            System.out.println("the transfer is error!");
//        }
//    }
}