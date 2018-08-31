package Main;

import Controller.SubTableControll;
import Controller.TableControll;
import Controller.TableListControll;
import Route.TableList;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    //传入的三个参数Type="钻孔" id="1" Path="C:\Users\laola\Desktop\outEpsDir\"
    public static String Type;
    public static String id;
    public static String Path;

    public static Stage PublicStage;
    public static String OpenTableName;
    public static String OpenSubTableName;
    public static Map<String, String> mSonTableTmpValue=new HashMap<String, String>();
    public static String classPath=FileUtil.getFileRealPath(Main.class);

    //统一在入口类中初始化对象，防止new多次导致页面js无法响应
    public static TableListControll tableListControll= new TableListControll();
    public static TableControll tableControll= new TableControll();
    public static SubTableControll subTableControll= new SubTableControll();

    //添加就会报错
//    public static TableList tableList= new TableList();
//    public static Table table= new Table();
//    public static SubTable subTable= new SubTable();

    public static void main(String[] args) {
        try {
            Type = args[0];
            id=args[1];
            Path=args[2];
        }catch (Exception e)
        {

        }
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        PublicStage=stage;
        Frame frame = new Frame();
        frame.getState();
        stage.setTitle("云南地质大数据");
        String filepath = "/aForm/images/edit.png";
        Image icon = new Image(filepath);
        stage.getIcons().add(icon);
        Scene scene = new Scene(new TableList(),1000,700, Color.web("#ABABAB"));

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.setScene(scene);

        //设置透明度
        stage.setOpacity(0.99);
        stage.setMaximized(true);
        //设置窗体类型
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }
}
