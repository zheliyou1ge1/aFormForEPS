package Main;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class SubTable extends Region  {
    private WebView subTableWebView = new WebView();
    private WebEngine subTableWebEngine = subTableWebView.getEngine();
    //Value存放子表的value值
    //FieldsName存放子表的fields名
    public static String Value="";
    public static String FieldsName="";
    public SubTable() {
        //Main.OpenSubTableName=钻孔_1-钻孔综合信息表_s1
        for (Map.Entry<String, String> entry : Main.mSonTableTmpValue.entrySet()) {
            if(entry.getKey().equals(Main.OpenSubTableName)){
                Value=entry.getValue();
                String[] tabNameString=Value.split("\"");
                FieldsName=tabNameString[3];
            }
        }

        String TableHtmlUrl="";
        try {
            URL url = new URL("file://"+Main.classPath+ "/aForm/SubTable.html");
            //添加alert映射
            subTableWebEngine.setOnAlert((WebEvent<String> wEvent) -> {
                //System.out.println( wEvent.getData());
                JLabel jlb = new JLabel();	//实例化JLble
                String filepath = FileUtil.getFileRealPath(Main.class)+"/aForm/images/info.png";
                ImageIcon icon = new ImageIcon(filepath);
                icon.setImage(icon.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT ));
                jlb.setIcon(icon);
                jlb.setSize(50, 50);
                JOptionPane.showMessageDialog(null, wEvent.getData(),"",JOptionPane.INFORMATION_MESSAGE,icon);
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
        subTableWebEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> ov, Worker.State oldState,
                 Worker.State newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) subTableWebEngine.executeScript("window");
                        window.setMember("subTableControll", Main.subTableControll );
                        subTableWebEngine.executeScript("load()");
                    }
                });
        subTableWebEngine.load(TableHtmlUrl);
        getChildren().add(subTableWebView);

    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(subTableWebView,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }


}
