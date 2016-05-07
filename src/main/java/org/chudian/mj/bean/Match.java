package org.chudian.mj.bean;

import java.io.File;

public class Match{
    public  static final String base_url2= "/usr/local/nginx/html/mjproject";
    public  static final String base_url= "/home/onglchen/proenv/userlib";
    static String LIB_URL_SERVICE = "/home/onglchen/proenv/userlib/libMatch.so";
    static String LIB_URL = "/home/onglchen/proenv/userlib/CODELIB/CODE_6_12_cache/CODE_New/lib/libMatch.so";
    static String LIB_URL2 = "/home/onglchen/proenv/userlib/CODELIB/CODE_7_31/CODE/lib/libMatch.so";
    static String LIB_URL_SERVICE_NEW = "/usr/local/nginx/html/mjproject/libMatch.so";
    static String PIC_INDEX_RUL = "/home/leopardio/UN-IO/picsource/picindex";
    static String PIC_INDEX_RUL2 = "/home/onglchen/proenv/userlib/index.data";
    static
    {
       // System.loadLibrary("Match");
        System.load(LIB_URL_SERVICE);
    }

    private native void train(String trainDir, String feaDir, String indexPath); //声明本地调用
    private native void reTrain(String trainDir, String feaDir, String indexPath);
    private native int load(String indexPath);
    private native int reLoad(String indexPath);
    private native String match(String pathName, String feaDir, String trainDir);
    private native void matchDir(String matchDir, String feaDir, String trainDir);
    private native int genGrayJpg(String srcurl, String desurl);


    public int  genGrayJpg_java(String srcurl, String desurl){
       return genGrayJpg(srcurl, desurl);
    }

    public void load_java(String indexPath){
        load(indexPath);
    }

    public void reLoad_java(String indexPath){
        reLoad(indexPath);
    }

    public void train_java(String trainDir, String feaDir, String indexPath)
    {
        File file = new File(indexPath);
        //索引文件不存在则训练，否则直接加载索引
        if (!file.exists())
            train(trainDir, feaDir, indexPath);
        else
            load(indexPath);
    }

    public void trainOnly(){
       // FileUtil.delAllFile(base_url + "/picFeatures");
        deleteFile(PIC_INDEX_RUL2);
        reTrain(base_url + "/TrainDataDir/", base_url + "/picFeatures", base_url + "/index.data");
        //base_url + "/TrainDataDir/" 训练集
        // base_url + "/picFeatures", 特征集
        // base_url + "/index.data" 索引文件
    }

    public void train(){


        train_java(base_url + "/TrainDataDir/", base_url + "/picFeatures", base_url + "/index.data");
    }

    public String  match_java(String pathName, String feaDir, String trainDir)
    {
        //多个结果以&&分割, 依次分别表示4个点的x,y坐标和各阶段的识别时间, 返回空串表示识别失败
        String ret = match(pathName, feaDir, trainDir);
        return ret;
    }

    //for test
    public void matchDir_java(String matchDir, String feaDir, String trainDir)
    {
        matchDir(matchDir, feaDir, trainDir);
    }

    public static void deleteFile(String fileUrl){
        File file = new File(fileUrl);
        if(file.exists()&&file.isFile()){
            file.delete();
        }
    }

    public static void main(String args[])
    {
//        FileUtil.delAllFile(base_url + "/picFeatures");
        Match match = new Match();
        match.genGrayJpg_java("/home/onglchen/proenv/userlib/20150801150012PM.jpg", "/home/onglchen/proenv/userlib/20150801150012PM.jpg.jpg");
        System.out.println("finish");
    }
}
