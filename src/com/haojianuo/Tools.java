package com.haojianuo;



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
    public static void starOriginSrtBAT(String pathname){
        File file = new File(pathname);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                    } else {
                        if(file2.getName().indexOf("srt.srt")>0) {continue;}
                        if(file2.getName().indexOf(".srt")>0) {
                            System.out.println("处理字幕文件:" + file2.getAbsolutePath());
                            starOriginSrt(file2.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println(pathname+"文件不存在!");
        }
    }
    /**
     * import origin subtitle and write a new subtitl with stars
     * @param pathname
     */
    public static void starOriginSrt(String pathname){

        try {

            File filename = new File(pathname);
            File writename = new File(pathname+".srt");

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
                //line = line.replace(","," ");
                line = line.replace("."," ");
                line = line.replaceAll(" +"," ");
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
                    t = t.replaceAll(" +","");
                    if(!al[i].matches("[a-zA-Z']+")){
                        newline+= (al[i])+" ";
                        continue;
                    }
                    if (hsKnown.contains(t.toLowerCase())){
                        newline+= getStars(al[i])+" ";
                    }else{
                        if (t.length()>2 && al[i].matches("[a-zA-Z']+"))
                            newline+= (al[i]+translate(al[i])+" ");
                        else
                            newline+= (al[i])+" ";
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
        boolean lucky=false;
        //lucky = (new java.util.Random().nextInt(3)==1);
        if (s!=null && s.length()>0){
            for(int i=0;i<s.length();i++){
                if(i<s.length()-1 && s.substring(i,i+1).equals("'")){
                    r+="'";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals("*")){
                    r+="*";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals("?")){
                    r+="?";
                }else if(i<s.length()-1 && s.substring(i,i+1).equals(".")){
                    r+=".";
                }
                else{
                    if (s.length()>3 && lucky)
                        r+=s.substring(i,i+1);
                    else
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
        if(origin.length()<4) {return "";}
        String t = origin.replace("*"," ");
        t = t.replace(","," ");
        t = t.replace("."," ");
        t = t.replace("?"," ");
        t = t.replace("\""," ");
        t = t.replace("!"," ");
        t = t.replaceAll(" ","");
        t = t.toLowerCase();
        try {
            if(Tools.isInteger(t))
                 return origin;
            if(t.matches("[a-zA-Z']+")) {
                if(hmTransform.containsKey(t)) {
                    t = hmTransform.get(t);
                }
                if (hmDictionary.containsKey(t)) {
                    return "["+hmDictionary.get(t)+"]";
                    //return "####";
                }else {
                    String r = "[??]";
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


}


