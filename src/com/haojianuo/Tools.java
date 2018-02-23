package com.haojianuo;

import com.lsj.trans.LANG;
import com.lsj.trans.factory.TFactory;
import com.lsj.trans.factory.TranslatorFactory;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by haojianuo on 2/20/18.
 */
public class Tools {
    //my known words
    public static HashSet hsKnown = new HashSet();
    public static HashMap<String,String> hmDictionary = new HashMap();
    public static HashMap<String,String> hmTransform= new HashMap();

    public static TFactory factory;
    /**
     * import my words from txt which I known
     */
    public static void importKnownWords(){

        String kw = Tools.readTxt(System.getProperty("user.dir")+"/db/knownwords-kunpeng.txt");
        //Tools.oo(kw);
        for (int i=0;i<10;i++){
            kw.replace(""+i,"");
        }
        kw.replace(":","");
        kw.replace("#","");
        String[] sa = kw.split(" ");
        Tools.oo(sa.length);
        for (int i=0;i<sa.length;i++){
            if(sa[i]!=null && sa[i].length()>0) {
                if(sa[i].contains("????")){break;}//发现四个问号，就略过后面所有的单词。前面做了问号标记的同样会是生词
                if(Tools.isInteger(sa[i])){continue;}
                hsKnown.add(sa[i].toLowerCase());
            }
        }

        Tools.oo("mywords:"+hsKnown.size());
    }


    public static void addTransIntoWords(){
        try {
            File filename = new File(System.getProperty("user.dir")+"/db/EnglishWordsTransform.txt");
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while (line != null) {
                line = br.readLine();
                if(line==null || line.length()==0){continue;}
                line =  line.replace("\t"," ");
                String[] al = line.split(" ");
                if(al.length>1 && hsKnown.contains(al[0])){
                    String originalWord = "";
                    for (int i = 0; i < al.length; i++) {
                        hsKnown.add(al[i]);
                        if(i==0) {
                            originalWord=al[0];
                        } else{
                            hmTransform.put(al[i],originalWord);
                        }

                    }
                }
            }
            oo("mywords-transformed:"+hsKnown.size());
            oo("hsTransform:"+hmTransform.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * import origin subtitle and write a new subtitl with stars
     * @param pathname
     */
    public static void starOriginSrt(String pathname){

        try {

            File filename = new File(System.getProperty("user.dir")+"/db/"+pathname);
            File writename = new File(System.getProperty("user.dir")+"/db/new_"+pathname);

            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while (line != null) {
                line = br.readLine();
                oo("------"+line);
                if(line == null ){continue;}
                line = line.replaceAll("\\*+"," ");
                String[]  al = line.split(" ");
                String newline = "";
                for(int i=0;i<al.length;i++){
                    String t = al[i].toLowerCase();

                    if(t.indexOf("'")>0){
                        t = t.substring(0,t.indexOf("'"));
                    }
                    if(t.indexOf(".")>0){
                        t = t.substring(0,t.indexOf("."));
                    }
                    if(t.indexOf(",")>0){
                        t = t.substring(0,t.indexOf(","));
                    }
                    t = t.replace("*"," ");
                    t = t.replace(","," ");
                    t = t.replace("."," ");
                    t = t.replace("?"," ");
                    t = t.replace("\""," ");
                    t = t.replace("!"," ");
                    t = t.replaceAll(" ","");
                    if (hsKnown.contains(t.toLowerCase())){
                        //line = line.replace(al[i],getStars(al[i]));
                        newline+= getStars(al[i])+" ";
                    }else{
                        newline+= (al[i]+" ["+translate(al[i])+"] ");
                    }
                }
                out.write(newline+"\r\n");
                oo("======"+newline);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static  String getStars(String s){
        String r ="";
        if (s!=null && s.length()>0){
            for(int i=0;i<s.length();i++){
                if(i<s.length()-1 && s.substring(i,i+1).equals("'")){
                    r+="'";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals("*")){
                    r+="*";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals("?")){
                    r+="?";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals(".")){
                    r+="";
                }
                else{
                    r+=".";
                }
            }
        }
        return r;
    }
    public static void oo(Object s){System.out.println(s);}

    public static String readTxt(String pathname){
        String r= "";
        try {
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while (line != null) {
                line = br.readLine();
                r+=line+" \r\n";
            }
            return r;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeTxt(String pathname){
        try {
            File writename = new File(pathname);
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            //out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    /**
     * import dictionary from txt
     */
    public static void importDictionary(){
        try {
            File filename = new File(System.getProperty("user.dir")+"/db/GRE-Words.txt"); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            int loop=0;
            String key="",value="";
            while (line != null) {
                line = br.readLine();
                if(line==null || line.length()==0){continue;}
                if(line.indexOf("Q:")==0){
                    loop=0;
                }
                if(loop==0){
                    key=line.replaceFirst("Q:","").replace(" ","");
                }
                if(loop==2){
                    value=line.replace("A:","").replace(" ","");
                }
                if(loop<3 && key.length()>0 && value.length()>0){
                    hmDictionary.put(key,value);
                    key="";value="";
                }
                loop++;
            }
            oo("hmDictionary:"+hmDictionary.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File filename = new File(System.getProperty("user.dir")+"/db/Niujin-Words2.txt"); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String key="",value="";
            while (line != null) {
                line = br.readLine();
                if(line==null || line.length()==0){continue;}
                line = line.replaceAll(" +"," ");
                String[] sa = line.split(" ");
                if(sa!=null && sa.length>1){
                    key=sa[0];
                    value=sa[1];
                }
                if(key.length()>0 && value.length()>0){
                    hmDictionary.put(key,value);
                    key="";value="";
                }
            }
            oo("hmDictionary-niujin-after:"+hmDictionary.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String translate(String origin){

        String t = origin.replace("*"," ");
        t = t.replace(","," ");
        t = t.replace("."," ");
        t = t.replace("?"," ");
        t = t.replace("\""," ");
        t = t.replace("!"," ");
        t = t.replaceAll(" ","");
        try {
            if(factory==null)
                factory = new TranslatorFactory();
            if(Tools.isInteger(t))
                 return "";
            if(t.matches("[a-z]+")) {
                if(hmTransform.containsKey(t)) {
                    t = hmTransform.get(t);
                }
                if (hmDictionary.containsKey(t)) {
                    return hmDictionary.get(t);
                    //return "####";
                }else {
                    String r = " ???? ";
                    //r = factory.get("omi").trans(LANG.EN, LANG.ZH, t);
                    if(r.length()>5) r=r.substring(0,5);
                    return r;
                    //return "****";
                }
            }
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void test() throws Exception {

        if(factory==null)
            factory = new TranslatorFactory();
        String origin = "Xamarin.Forms has several layouts and features for organizing content on screen.\n Each layout control is described below, as well as details on how to handle screen orientation changes:";
        System.out.println("金山 : " + factory.get("jinshan").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("有道 : " + factory.get("youdao").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("百度 : " + factory.get("baidu").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("谷歌 : " + factory.get("google").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("腾讯 : " + factory.get("tencent").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("欧米 : " + factory.get("omi").trans(LANG.EN, LANG.ZH, origin));
        System.out.println("\n");


        origin = "这个巡展，大家想去的先注册，临近当地巡展时候，会有注册确认邮件发给大家，届时可以看到详细的活动地址。\n等你工作了你就知道你当时的观念是多么的幼稚";
        System.out.println("金山 : " + factory.get("jinshan").trans(LANG.ZH, LANG.EN, origin));
        System.out.println("有道 : " + factory.get("youdao").trans(LANG.ZH, LANG.EN, origin));
        System.out.println("百度 : " + factory.get("baidu").trans(LANG.ZH, LANG.EN, origin));
        System.out.println("谷歌 : " + factory.get("google").trans(LANG.ZH, LANG.EN, origin));
        System.out.println("腾讯 : " + factory.get("tencent").trans(LANG.ZH, LANG.EN, origin));
        System.out.println("欧米 : " + factory.get("omi").trans(LANG.ZH, LANG.EN, origin));
    }
}


