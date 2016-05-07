package org.chudian.mj.utils; /**
 * Created by tiwei on 6/16/15.
 */

import net.sf.json.JSONObject;
import org.chudian.mj.bean.Video;

import java.io.*;


public class MediocreExecJavac2 {

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
    public void runCmd(String[] command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null) {
                System.out.println("line==" + line);
            }

            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public boolean transfer(String infile,String outfile,String outType) {

        String trasfer="ffmpeg -i "+infile+" -c:v libx264 -c:a aac -ac 2 -ar 48000 -strict -2 -f "+outType+" "+outfile;

        try{
            runCmd(trasfer);
        }catch (Throwable t){
            t.printStackTrace();
            return false;
        }
        return true;
    }

    public static String readFile(String fileName) {
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

    public String readtime(String file) {
        String str="/home/tmp/info.txt";
        String timelen = "";
        String cmd = "timelen "+file;

        runCmd(cmd);
        timelen=readFile(str);

        return timelen;
    }

    public boolean excuteTransfer(String infile,String outfile,String outFielName, String outType){
        Testsplit testsplit = new Testsplit();
        String homepath = "";
        homepath = testsplit.getBackPath(infile);
        String filename = testsplit.getFileName(infile);
        System.out.println("filename== " + filename);
        filename = filename + "." + outType;

        if(outfile==null){
            outfile = homepath +"/" +outFielName;
        }else{
            outfile = outfile + "/" + outFielName;
        }
        if(transfer(infile, outfile, outType)) {
            System.out.println("the transfer is ok!");
            return true;
        } else {
            System.out.println("the transfer is error!");
            return false;
        }
    }

    public JSONObject fromStringToJson(String data){
        JSONObject JsonInfo = null;
        JSONObject jo = JSONObject.fromObject(data);
        JsonInfo = JSONObject.fromObject(jo.get("format"));
        JSONObject json1 = JSONObject.fromObject(JsonInfo.getJSONObject("tags"));
        JsonInfo.remove("tags");
        JsonInfo.putAll(json1);
        return JsonInfo;
    }

    public JSONObject transInfo(String outfile){
        JSONObject videoInfoJson = null;
        Testsplit testsplit = new Testsplit();
        String filename = testsplit.getFileName(outfile);
        String outfilepath = testsplit.getBackPath(outfile);
        JSONObject infoJson = null;
        String info = "ffprobe " + outfile + " -v quiet -print_format json -show_format > "+outfilepath+"/"+filename+".json";
        String deleteInfo = "rm " + outfilepath+"/"+filename+".json";
        String[] infos = {"/bin/sh","-c",info};
        try{
            runCmd(infos);
            String readString = readFile(outfilepath+"/"+filename+".json");
            videoInfoJson = fromStringToJson(readString);
            //删除产生的json中间文件
            //runCmd(deleteInfo);
        }catch (Throwable t){
            t.printStackTrace();
        }
        return videoInfoJson;
    }

    public Video getVideoInfo(String infile,String outfile,String outType){
        Testsplit testsplit = new Testsplit();
        String homepath = "";
        Video videoInfo = new Video();
        JSONObject videoInfoJson = null;
        homepath = testsplit.getBackPath(infile);
        String filename = testsplit.getFileName(infile);
        System.out.println("filename== "+ filename);
        filename = filename + "." + outType;
        if(outfile==null){
            outfile = homepath +"/" +filename;
        }else{
            outfile = outfile + "/" + filename;
        }
        if(transfer(infile, outfile, outType)) {
            System.out.println("the transfer is ok!");
            videoInfoJson = transInfo(outfile);
            videoInfo.setName(filename);
            videoInfo.setSize((String)videoInfoJson.get("size"));
            videoInfo.setOriginUrl(infile);
            videoInfo.setFinalUrl(outfile);
            videoInfo.setVideoFormat(outType);
            videoInfo.setCodeRate((String)videoInfoJson.get("bit_rate"));
            videoInfo.setDuration((String)videoInfoJson.get("duration"));
        } else {
            System.out.println("the transfer is error!");
        }
        return videoInfo;
    }
    /*
    public static void main(String args[]) {
        MediocreExecJavac me = new MediocreExecJavac();
        String baseurl = "/home/tiwei/tmp/";

        String infile = baseurl + "di.flv";
        String outfile ;
        String homepath = "";
        Testsplit testsplit = new Testsplit();
        homepath = testsplit.getBackPath(infile);
        String filename = testsplit.getFileName("di.flv");
        System.out.println("filename== "+ filename);
        filename = filename + ".3gp";
        outfile = homepath +"/" +filename;
        System.out.println("outfile==" + outfile);
        System.out.println("infile==" + infile);
        //   String timelen = me.readtime(infile);
        //  System.out.println("timelen is :" + timelen);

        if(me.transfer(infile,outfile,"3gp")) {
            System.out.println("the transfer is ok!");
        } else {
            System.out.println("the transfer is error!");
        }
    }
    */
    public static void main(String args[]){
        MediocreExecJavac2 me = new MediocreExecJavac2();
        String infile = "/home/onglchen/temp/2_20150608.3gp";
        //输入3个参数，第一个为源文件，第二个为输出文件路径，第三个为要转换的格式
     //   me.excuteTransfer(infile,null,"ss","avi");
        Video video = me.getVideoInfo(infile, null, "avi");
        System.out.println("video = " + video.getName());
    }
}
