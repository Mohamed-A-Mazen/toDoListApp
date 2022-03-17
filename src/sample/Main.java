
package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import todopackage.ToDoData;

import java.io.IOException;

public class Main  extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(" miza");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }

    @Override
    public void init() {
        try {
            ToDoData.getInstance().loadToDoData();
        }catch (IOException e){
            //nothing need to be done here
        }
    }

    @Override
    public void stop(){
        try {
            ToDoData.getInstance().storeToDoData();
        }catch (IOException e){
            //nothing need to be done here
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
