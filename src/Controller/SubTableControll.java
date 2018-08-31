package Controller;

import Main.Main;
import Main.SubTable;
import Main.Table;
import Main.TableList;
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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubTableControll {


    public SubTableControll() {
        UIManager UI=new UIManager();
        UI.put("OptionPane.background", Color.white);
        UI.put("Panel.background", Color.white);
        UI.put("OptionPane.font", new FontUIResource(new Font("宋体",Font.BOLD,300)));
    }

    public String getFormValue()
    {
        return SubTable.Value;
    }
    public String getFormFields()
    {
        String ret="";
        File confDir=new File(FileUtil.getFileRealPath(Main.class)+"/aForm/conf/");
        List<File> FileList=new ArrayList<File>();
        FileList=FileUtil.getDirAllFileList(confDir,FileList);
        for(File fieldsFile:FileList)
        {
            if(fieldsFile.getName().equals(SubTable.FieldsName))
            {
                ret=FileUtil.readFile(fieldsFile.getPath());
            }
        }
        return ret;
    }

    public void backToTable()
    {
        Main.OpenSubTableName="";
        Stage stage=Main.PublicStage;

        stage.setTitle(Main.OpenTableName);
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

}
