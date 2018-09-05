package Route;
import Controller.TableListControll;
import Main.Main;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Table extends Region  {
    private WebView tableWebView = new WebView();
    private WebEngine tableWebEngine = tableWebView.getEngine();
    public static String ValueFilePath="";
    public static String FieldsName="";

    public Table() {
        String valueFileName=Main.Type+"_"+Main.id+"-"+Main.OpenTableName+".json";
        File file=new File(Main.Path+"/data/"+valueFileName);
        if(file.exists())
        {
            ValueFilePath=file.getPath();
            String FileValue=FileUtil.readFile(ValueFilePath);
            String[] tabNameString=FileValue.split("\"");
            FieldsName=tabNameString[3];
        }else
        {
            valueFileName=Main.Type+"_"+Main.OpenTableName+".value";
            File confDir=new File(FileUtil.getFileRealPath(TableListControll.class)+"/aForm/conf/");
            List<File> FileList=new ArrayList<File>();
            FileList=FileUtil.getDirAllFileList(confDir,FileList);
            for(File f:FileList)
            {
                if(f.getName().equals(valueFileName))
                {
                    ValueFilePath=f.getPath();
                    String FileValue=FileUtil.readFile(ValueFilePath);
                    String[] tabNameString=FileValue.split("\"");
                    FieldsName=tabNameString[3];
                }
            }
        }
        String TableHtmlUrl="";
        try {
            URL url = new URL("file://"+Main.classPath+ "/aForm/Table.html");
            //添加alert映射
            tableWebEngine.setOnAlert((WebEvent<String> wEvent) -> {
                //System.out.println( wEvent.getData());
                JLabel jlb = new JLabel();	//实例化JLble
                String filepath = FileUtil.getFileRealPath(Main.class)+"/aForm/images/info.png";
                ImageIcon icon = new ImageIcon(filepath);
                icon.setImage(icon.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT ));
                jlb.setIcon(icon);
                jlb.setSize(50, 50);
                JOptionPane.showMessageDialog(Main.frame, wEvent.getData(),"",JOptionPane.INFORMATION_MESSAGE,icon);
            });
            TableHtmlUrl = url.toExternalForm();
        }catch (Exception e)
        {
            URL url = null;
            try {
                url = new URL("file://"+Main.classPath+"/aForm/Error.html");
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            TableHtmlUrl=url.toExternalForm();
            e.printStackTrace();
        }

        //添加js映射
        tableWebEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> ov, Worker.State oldState,
                 Worker.State newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) tableWebEngine.executeScript("window");
                        window.setMember("tableControll", Main.tableControll);
                        tableWebEngine.executeScript("load()");
                    }
                });
        tableWebEngine.load(TableHtmlUrl);
        getChildren().add(tableWebView);

    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(tableWebView,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }


}
