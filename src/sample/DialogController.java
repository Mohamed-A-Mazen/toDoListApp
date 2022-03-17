package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import todopackage.ToDoData;
import todopackage.ToDoItem;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private  TextField shortDescription;
    @FXML
    private  TextArea detailedDescription;
    @FXML
    private  DatePicker date;
@FXML
    public void getNewToDoItem(){
        String shortDes = shortDescription.getText();
        String detailedDes = detailedDescription.getText();
        LocalDate myDate = date.getValue();
        ToDoData.getInstance().getTodolist().add(0,new ToDoItem(shortDes,detailedDes,myDate));

    }
}
