package Route;
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
import java.net.MalformedURLException;
import java.net.URL;

public class TableList extends Region  {
    private static WebView tableListWebView = new WebView();
    public  static WebEngine tableListWebEngine = tableListWebView.getEngine();

    public TableList() {
        String TableListHtmlUrl="";
        try {
            System.out.println("jar包路径:"+Main.classPath);
            URL url = new URL("file://"+Main.classPath+ "/aForm/TableList.html");
            System.out.println("html路径:"+url.getPath());

            //添加alert映射
            tableListWebEngine.setOnAlert((WebEvent<String> wEvent) -> {
                //System.out.println( wEvent.getData());
                JLabel jlb = new JLabel();	//实例化JLble
                String filepath = FileUtil.getFileRealPath(Main.class)+"/aForm/images/info.png";
                ImageIcon icon = new ImageIcon(filepath);
                icon.setImage(icon.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT ));
                jlb.setIcon(icon);
                jlb.setSize(50, 50);
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), wEvent.getData(),"",JOptionPane.INFORMATION_MESSAGE,icon);
            });
            TableListHtmlUrl = url.toExternalForm();
        }catch (Exception e)
        {
            URL url = null;
            try {
                url = new URL("file://"+Main.classPath+"/aForm/Error.html");
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            TableListHtmlUrl=url.toExternalForm();
            e.printStackTrace();
        }

        //添加js映射
        tableListWebEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> ov, Worker.State oldState,
                 Worker.State newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) tableListWebEngine.executeScript("window");
                        window.setMember("tableListControll",Main.tableListControll);
                        tableListWebEngine.executeScript("load()");
                        //tableWebEngine.executeScript("exeJs2Form()");
                        //webEngine.executeScript("subTabListDivOnload()");
                    }
                });
        tableListWebEngine.load(TableListHtmlUrl);
        getChildren().add(tableListWebView);

    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(tableListWebView,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }


}
