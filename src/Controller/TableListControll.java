package Controller;

import Main.Main;
import Main.Table;
import Main.TableList;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileUtil;
import util.RowMapper;
import util.SqliteHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableListControll {

    public TableListControll() {
        UIManager UI=new UIManager();
        UI.put("OptionPane.background", Color.white);
        UI.put("Panel.background", Color.white);
        UI.put("OptionPane.font", new FontUIResource(new Font("宋体",Font.BOLD,300)));
    }
    public String selectTableOfType()
    {
        //遍历从conf目录获取所有文件,根据Main入口传入的类型查找该类型的所有表,返回字符串以,分割
        String ret="";
        File confDir=new File(FileUtil.getFileRealPath(TableListControll.class)+"/aForm/conf/");
        List<File> FileList=new ArrayList<File>();
        FileList=FileUtil.getDirAllFileList(confDir,FileList);
        for(File f:FileList)
        {
            String fName=f.getName();
            //类型的所有主表
            if(fName.contains(Main.Type+"_")&&!fName.contains("_s")&&fName.contains(".fields"))
            {

                if( !ret.isEmpty() ) ret+=",";
                ret += fName.split("_")[1].split(".fields")[0];
            }
        }
        return ret;
    }
    public int isTabEmpty(String selectName){
        //selectName=钻探点综合记录表.json
        selectName=Main.Type+"_"+Main.id+"-"+selectName;
        //selectName=钻孔_1-钻探点综合记录表.json
        File selectFile = new File(Main.Path+"/data/"+selectName);
        if( selectFile.exists())
            return 1;
        else
            return 0;
    }
    public void openTableByTableName(String tableName)
    {
        Main.OpenTableName=tableName;
        Stage stage=Main.PublicStage;

        stage.setTitle(tableName);
        String filepath = "/aForm/images/edit.png";
        javafx.scene.image.Image icon = new javafx.scene.image.Image(filepath);
        stage.getIcons().add(icon);
        Scene scene = new Scene(new Table(),1000,700, javafx.scene.paint.Color.web("#ABABAB"));
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
    public String getImageUrl()
    {
        String ret=getFieldInSqlite("ImageUrl");
        if(ret.isEmpty())
        {
            return "";
        }
        ret.trim();
        String temp[] = ret.split("\\/"); /**split里面必须是正则表达式，"\\"的作用是对字符串转义*/
        ret = temp[temp.length-1];
        //页面读文件路径必须加file:///
        ret ="file:///"+Main.Path+"/images/"+ret;
        return ret;
    }

    public String getVoiceUrl()
    {
        String ret=getFieldInSqlite("VoiceUrl");
        if(ret.isEmpty())
        {
            return "";
        }
        ret.trim();
        String temp[] = ret.split("\\/"); /**split里面必须是正则表达式，"\\"的作用是对字符串转义*/
        ret = temp[temp.length-1];
        ret ="file:///"+Main.Path+"/audio/"+ret;
        return ret;
    }
    public String getText()
    {
        return getFieldInSqlite("TEXT");
    }
    public String getFieldInSqlite(String Fields)
    {
        String ret="";
        try {
            SqliteHelper sh = new SqliteHelper(Main.Path+"/DB/layers.db");
            List<String> sList = sh.executeQuery("select "+Fields+" from "+Main.Type+" where _id="+Main.id, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int index)
                        throws SQLException {
                    return rs.getString(Fields);
                }
            });
            if(sList.get(0)!=null)
            {
                ret=sList.get(0);
                System.out.println(Main.Type+"_"+Main.id+"的"+Fields+"："+sList.get(0));
            }else
            {
                System.out.println("字段为空");
                return "";
            }
        }catch (Exception e) {
            e.printStackTrace();
            onCatch();
        }
        return ret;
    }
    public void onCatch()
    {
        URL url = null;
        try {
            url = new URL("file://"+Main.classPath+"/aForm/Error.html");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        String TableListHtmlUrl=url.toExternalForm();
        TableList.tableListWebEngine.load(TableListHtmlUrl);
    }

    public void exit() {
        JLabel jlb = new JLabel();	//实例化JLble
        String filepath = FileUtil.getFileRealPath(TableListControll.class)+"/aForm/images/退出.png";
        ImageIcon icon = new ImageIcon(filepath);
        icon.setImage(icon.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT ));
        jlb.setIcon(icon);
        jlb.setSize(50, 50);
        int flag =JOptionPane.showConfirmDialog(null,"       确定退出吗?",null,JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,icon);
        if(flag==0)
        {
            Platform.exit();
        }
    }

}
