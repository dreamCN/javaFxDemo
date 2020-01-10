package com.wlxk.fx.sokoban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author: 鼠行止
 * @date: 2020/1/6
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setTitle("javaFx推箱子游戏");
        //设置窗口的图标.
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/image/sokoban.png")));
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
