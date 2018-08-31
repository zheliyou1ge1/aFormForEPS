package Controller;

import Main.Main;
import Route.Table;
import Route.TableList;
import Route.SubTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileUtil;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableControll {


    public TableControll() {
        UIManager UI=new UIManager();
        UI.put("OptionPane.background", Color.white);
        UI.put("Panel.background", Color.white);
        UI.put("OptionPane.font", new FontUIResource(new Font("宋体",Font.BOLD,300)));
    }

    public String getFormValue()
    {
        String ret="";
        File file=new File(Table.ValueFilePath);
        if(file.exists())
        {
            ret=FileUtil.readFile(file.getPath());
        }else
        {
            String valueFileName=Main.Type+"_"+Main.OpenTableName+".value";
            file=new File(FileUtil.getFileRealPath(TableListControll.class)+"/aForm/conf/");
            List<File> FileList=new ArrayList<File>();
            FileList=FileUtil.getDirAllFileList(file,FileList);
            for(File f:FileList)
            {
                if(valueFileName.equals(f.getName()))
                {
                    ret=FileUtil.readFile(f.getParent());
                }
            }

        }
        return ret;
    }
    public String getFormFields()
    {
        String ret="";
        File confDir=new File(FileUtil.getFileRealPath(Main.class)+"/aForm/conf/");
        List<File> FileList=new ArrayList<File>();
        FileList=FileUtil.getDirAllFileList(confDir,FileList);
        for(File fieldsFile:FileList)
        {
            if(fieldsFile.getName().equals(Table.FieldsName))
            {
                ret=FileUtil.readFile(fieldsFile.getPath());
            }
        }
        return ret;
    }
    public String getSubTableList()
    {
        Map<String,String> subTableMap=new HashMap<String, String>();
        String ret = "";
        String jsonValue="";
        String selectName=Main.Type+"_"+Main.id+"-"+Main.OpenTableName+"_s";
        File dataDir=new File(Main.Path+"/data/");
        List<File> FileList=new ArrayList<File>();
        FileList=FileUtil.getDirAllFileList(dataDir,FileList);
        for(File sub:FileList)
        {
            if(sub.getName().contains(selectName))
            {
                //获取子表json的值
                jsonValue=FileUtil.readFile(sub.getPath()).trim();
            }
        }
        if( !jsonValue.isEmpty()&&jsonValue.charAt(0) == '[' )
        {
            try {
                JsonArray jsonArray = (JsonArray)new JsonParser().parse(jsonValue);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                    if (jsonObject == null) continue;
                    subTableMap.put(selectName + (i+1) , jsonObject.toString());
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }else{

        }
        if( !subTableMap.isEmpty() )
        {
            for (String s : subTableMap.keySet()) {
                if ( !Main.mSonTableTmpValue.keySet().contains(s) ) {
                    Main.mSonTableTmpValue.put(s, subTableMap.get(s));
                }
            }
        }
        for (String s : Main.mSonTableTmpValue.keySet())
        {
            if( !s.contains(selectName) ) continue;
            if(!ret.isEmpty()) ret += ",";
            ret += s;
        }
        return ret;
    }

    public void backToTableList()
    {
        Main.OpenTableName="";
        Stage stage=Main.PublicStage;

        stage.setTitle("云南地质大数据");
        String filepath = "/aForm/images/edit.png";
        javafx.scene.image.Image icon = new javafx.scene.image.Image(filepath);
        stage.getIcons().add(icon);
        Scene scene = new Scene(new TableList(),1000,700, javafx.scene.paint.Color.web("#ABABAB"));
        stage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        //设置透明度
        stage.setOpacity(0.99);
        stage.setMaximized(true);
        //设置窗体类型
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }
    //打开子表
    public void openSubTable(String subTableName)
    {
        Main.OpenSubTableName=subTableName;
        Stage stage=Main.PublicStage;

        stage.setTitle(Main.OpenSubTableName.split("-")[1].split("_s")[0]);
        String filepath = "/aForm/images/edit.png";
        javafx.scene.image.Image icon = new javafx.scene.image.Image(filepath);
        stage.getIcons().add(icon);
        Scene scene = new Scene(new SubTable(),1000,700, javafx.scene.paint.Color.web("#ABABAB"));
        stage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        //设置透明度
        stage.setOpacity(0.99);
        stage.setMaximized(true);
        //设置窗体类型
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }

    public int saveForm(String FormInfo)
    {
        String TableFileName=Main.Type+"_"+Main.id+"-"+Main.OpenTableName+".json";
        String TableFileNamePath=Main.Path+"/data/"+TableFileName;
        try {
            FileUtil.saveFile(FormInfo,TableFileNamePath);
            //return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        if(hasSubTable(Main.Type+"_"+Main.OpenTableName)){
            String sonJson = "[";
            for (Map.Entry<String, String> entry : Main.mSonTableTmpValue.entrySet()) {
                if(entry.getKey().contains(TableFileName.split(".json")[0])){
                    if( sonJson.length()>1 ) sonJson +=",";
                    sonJson += entry.getValue();
                }
            }
            sonJson += "]";
            String SubTableFilePath=Main.Path+"/data/"+Main.Type+"_"+Main.id+"-"+Main.OpenTableName+"_s.json";
            try {
                FileUtil.saveFile(sonJson,SubTableFilePath);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
    public int addSubTable()
    {
        String selectName=Main.Type+"_"+Main.id+"-"+Main.OpenTableName+"_s";
        // 遍历，目录下的所有文件，得到最大序号
        int num = 0;
        for (String fname : Main.mSonTableTmpValue.keySet())
        {
            if (fname.contains(selectName)) {
                //根据获取到的包含point_1-point_t1_s的文件名按_s截取后一段的序号
                String numStr = fname.substring(fname.lastIndexOf("_s")+2, fname.length());
                if (num < Integer.parseInt(numStr)) num = Integer.parseInt(numStr);
            }
        }
        String newFileName=selectName+(num+1);
        //获取默认值文件名
        String defValue = "";
        {
            //String url=c.getExternalFilesDir(null) +"/aForm/conf/"+typename+"_"+valuePath;
            String url = "";
            String valueName=Main.Type+"_"+Main.OpenTableName+"_s.value";
            File confDir = new File(FileUtil.getFileRealPath(TableControll.class)+ "/aForm/conf/");
            List<File> FileList = new ArrayList<File>();
            FileList = FileUtil.getDirAllFileList(confDir, FileList);
            for (File f : FileList) {
                if (f.getName().equals(valueName)) {
                    url = f.getPath();
                    break;
                }
            }
            if(url.isEmpty())
            {
                //没有子表
                return 0;
            }
            //获取子表默认值
            defValue=FileUtil.readFile(url);
        }
        //如果mSonTableTmpValue没有key为newFileName的就将取到的值和名put进去
        if ( !Main.mSonTableTmpValue.keySet().contains(newFileName) ) {
            Main.mSonTableTmpValue.put(newFileName,defValue);
            openSubTable(newFileName);
            return 2;
        }else
        {
            //添加子表失败
            return 1;
        }

    }
    public boolean hasSubTable(String selectName)
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
