package com.haojianuo;

import java.io.File;
import java.util.HashSet;

public class Main {



    public static void main(String[] args) {
	// write your code here
        init();
        Tools.starOriginSrt("Counterpart.S01E04.720p.WEB.H264-DEFLATE.英文.srt");
    }
    public static void init(){
        System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径
        File directory = new File("");//设定为当前文件夹
        try{
            System.out.println(directory.getCanonicalPath());//获取标准的路径
            System.out.println(directory.getAbsolutePath());//获取绝对路径
        }catch(Exception e){}

        Tools.importKnownWords();
        Tools.addTransIntoWords();

    }


}
