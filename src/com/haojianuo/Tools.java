package com.haojianuo;

import java.io.*;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by haojianuo on 2/20/18.
 */
public class Tools {
    //my known words
    public static HashSet hsKnown = new HashSet();
    /**
     * import my words from txt which I known
     */
    public static void importKnownWords(){

        //String kw = Tools.readTxt(System.getProperty("user.dir")+"/out/com/haojianuo/knownwords.txt");
        String kw = Tools.readTxt(System.getProperty("user.dir")+"/db/knownwords.txt");
        Tools.oo(kw);
        for (int i=0;i<10;i++){
            kw.replace(""+i,"");
        }
        kw.replace("#","");
        String[] sa = kw.split(" ");
        Tools.oo(sa.length);
        for (int i=0;i<sa.length;i++){
            if(sa[i]!=null && sa[i].length()>0) {
                if(Tools.isInteger(sa[i])){continue;}
                hsKnown.add(sa[i].toLowerCase());
            }
        }

        Tools.oo(hsKnown.size());
    }

    public static void addTransIntoWords(){
        try {
           File filename = new File(System.getProperty("user.dir")+"/db/EnglishWordsTransform.txt"); // 要读取以上路径的input。txt文件
           InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();

            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(line==null || line.length()==0){continue;}
                line =  line.replace("\t"," ");
                String[] al = line.split(" ");
                if(al.length>1 && hsKnown.contains(al[0])){
                    for (int i = 0; i < al.length; i++) {
                        hsKnown.add(al[i]);
                    }
                }
            }
            oo(hsKnown.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * import origin subtitle and write a new subtitl with stars
     * @param pathname
     */
    public static void starOriginSrt(String pathname){

        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

                /* 读入TXT文件 */
            //String pathname = "D:\\twitter\\13_9_6\\dataset\\en\\input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(System.getProperty("user.dir")+"/db/"+pathname); // 要读取以上路径的input。txt文件
            File writename = new File(System.getProperty("user.dir")+"/db/new_"+pathname); // 相对路径，如果没有则要建立一个新的output。txt文件

            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                oo("------"+line);
                /* 写入Txt文件 */
                if(line == null ){continue;}
                String[]  al = line.split(" ");
                String newline = "";
                for(int i=0;i<al.length;i++){
                    String t = al[i].toLowerCase();
                    t = t.replace("*"," ");
                    t = t.replace(","," ");
                    t = t.replace("."," ");
                    t = t.replace("?"," ");
                    t = t.replace("\""," ");
                    t = t.replace("!"," ");
                    t = t.replace(" ","");
                    if (hsKnown.contains(t)){
                        //line = line.replace(al[i],getStars(al[i]));
                        newline+= getStars(al[i])+" ";
                    }else{

                        newline+= (al[i])+" ";
                    }
                }
                out.write(newline+"\r\n");
                oo("======"+newline);
            }
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static  String getStars(String s){
        String r ="";
        if (s!=null && s.length()>0){
            for(int i=0;i<s.length();i++){
                r+="#";
            }
        }
        return r;
    }
    public static void oo(Object s){System.out.println(s);}

    public static String readTxt(String pathname){
        String r= "";
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

                /* 读入TXT文件 */
            //String pathname = "D:\\twitter\\13_9_6\\dataset\\en\\input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                r+=line+" \r\n";
            }
            return r;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeTxt(String pathname){
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
                /* 写入Txt文件 */
            File writename = new File(pathname); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}


