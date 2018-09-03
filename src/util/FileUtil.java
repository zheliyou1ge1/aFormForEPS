package util;

import Controller.TableControll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static String getFileRealPath(Class cls){
        //检查用户传入的参数是否为空
        if(cls==null)
            throw new IllegalArgumentException("参数不能为空！");
        ClassLoader loader=cls.getClassLoader();
        //获得类的全名，包括包名
        String clsName=cls.getName()+".class";
        //获得传入参数所在的包
        Package pack=cls.getPackage();
        String path="";
        //如果不是匿名包，将包名转化为路径
        if(pack!=null){
            String packName=pack.getName();
            //此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if(packName.startsWith("java.")||packName.startsWith("javax."))
                throw new IllegalArgumentException("不要传送系统类！");
            //在类的名称中，去掉包名的部分，获得类的文件名
            clsName=clsName.substring(packName.length()+1);
            //判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if(packName.indexOf(".")<0) path=packName+"/";
            else{//否则按照包名的组成部分，将包名转换为路径
                int start=0,end=0;
                end=packName.indexOf(".");
                while(end!=-1){
                    path=path+packName.substring(start,end)+"/";
                    start=end+1;
                    end=packName.indexOf(".",start);
                }
                path=path+packName.substring(start)+"/";
            }
        }
        //调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        java.net.URL url =loader.getResource(path+clsName);
        //从URL对象中获取路径信息
        String realPath=url.getPath();
        //去掉路径信息中的协议名"file:"
        int pos=realPath.indexOf("file:");
        if(pos>-1) realPath=realPath.substring(pos+5);
        //去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos=realPath.indexOf(path+clsName);
        realPath=realPath.substring(0,pos-1);
        //如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if(realPath.endsWith("!"))
            realPath=realPath.substring(0,realPath.lastIndexOf("/"));
      /*------------------------------------------------------------
       ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径
        中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要
        的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的
        中文及空格路径
      -------------------------------------------------------------*/
        try{
            realPath=java.net.URLDecoder.decode(realPath,"utf-8");
        }catch(Exception e){throw new RuntimeException(e);}
        return realPath;
    }//getAppPath定义结束

    public static List<File> getDirAllFileList(File file, List<File> resultFileList){
        File[] files = file.listFiles();
        if(files==null)return resultFileList;// 判断目录下是不是空的
        for (File f : files) {
            if(f.isDirectory()){// 判断是否文件夹
                resultFileList.add(f);
                getDirAllFileList(f,resultFileList);// 调用自身,查找子目录
            }else
                resultFileList.add(f);
        }
        return resultFileList;
    }
    public static String readFile(String url){
        FileInputStream inputStream;
        StringBuilder sb = new StringBuilder("");
        try {
            inputStream = new FileInputStream(url);
            InputStreamReader reader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            inputStream.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void saveFile(String toSaveString, String filePath)  throws Exception{
        File saveFile = new File(filePath);
//        FileOutputStream fos = new FileOutputStream(saveFile);
//        fos.write(toSaveString.getBytes());
//        fos.close();

        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(saveFile), "utf-8");
        oStreamWriter.append(toSaveString);
        oStreamWriter.close();

    }
    public static boolean hasSubTable(String selectName)
    {
        File confDir = new File(FileUtil.getFileRealPath(TableControll.class)+ "/aForm/conf/");
        List<File> FileList = new ArrayList<File>();
        FileList = FileUtil.getDirAllFileList(confDir, FileList);
        for (File f : FileList) {
            if (f.getName().contains(selectName)) {
                return true;
            }
        }
        return false;
    }
}
