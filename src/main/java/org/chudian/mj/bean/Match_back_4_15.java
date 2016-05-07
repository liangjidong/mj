package org.chudian.mj.bean;

import java.io.File;

public class Match_back_4_15 {
    static
    {
        //System.loadLibrary("Match");
        System.load("/home/onglchen/proenv/userlib/libMatch.so");
    }

    private native void train(String trainDir, String feaDir, String indexPath); //声明本地调用
    private native int load(String indexPath);
    private native String  match(String pathName, String feaDir, String trainDir);
    private native void matchDir(String matchDir, String feaDir, String trainDir);

    public void train_java(String trainDir, String feaDir, String indexPath)
    {
        File file = new File(indexPath);
        //索引文件不存在则训练，否则直接加载索引
        if (!file.exists())
            train(trainDir, feaDir, indexPath);
        else
            load(indexPath);
    }

    public  String match_java(String pathName, String feaDir, String trainDir)
    {
//        double begain = System.currentTimeMillis();
        String ret = match(pathName, feaDir, trainDir);
//        double end = System.currentTimeMillis();
//        double time = end - begain;
//        System.out.println("match Time :====" + time);
//        String[] result = ret.split("&&");
//        String result3[] = new String[10];
//        String result2 = "";

//        for(int i = 0; i < result.length; )
//        result3[0] = result[0] + "&" + result[1];
//
//        System.out.println("match result:");
//        if(result.length > 1){
//            result2 = result[0];
//
//        }
//        System.out.println("length=="+result.length);
//        for (int i = 0;i < result.length; i++)
//        {
//            System.out.println(result[i]);
//        }
//        System.out.println(ret+"\n");
        return ret;
    }

    public void train(){
        String baseurl = "/home/onglchen/proenv/userlib";
        train_java(baseurl + "/TrainDataDir/", baseurl + "/picFeatures", baseurl + "/index.data");
    }

    //for test
    public void matchDir_java(String matchDir, String feaDir, String trainDir)
    {
        matchDir(matchDir, feaDir, trainDir);
    }

    public static void main(String args[])
    {
//        String s =  System.getProperty("java.library.path");
//        System.out.println(s);
        String baseurl = "/home/onglchen/proenv/userlib";
        String trainDir =baseurl + "/TrainDataDir/";

        Match_back_4_15 t = new Match_back_4_15();
        t.train();
       System.out.println(t.match_java("/home/onglchen/proenv/userlib/TrainDataDir/3.jpg", baseurl + "/picFeatures", trainDir));

        //  t.match_java(baseurl+"/f-2.jpg", baseurl+"/picFeatures", trainDir);
        // t.matchDir_java("./MatchDataDir/", "./picFeatures/");
    }
}

