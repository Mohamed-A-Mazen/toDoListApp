package todopackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {
    private static final ToDoData instance = new ToDoData();
    private static final String fileName = "ToDoData.txt";
    private ObservableList<ToDoItem> todolist;
    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ToDoData(){
    }

    public static ToDoData getInstance() {
        return instance;
    }

    public static String getFileName() {
        return fileName;
    }

    public ObservableList<ToDoItem> getTodolist() {
        return todolist;
    }

    public void loadToDoData() throws IOException {
        Path path = Paths.get(fileName);
        BufferedReader bf = Files.newBufferedReader(path);
        todolist= FXCollections.observableArrayList();
        try {
            String line ;
            while ((line=bf.readLine())!=null && !line.isBlank()){
                String[] strings = line.split("\t");
                LocalDate date = LocalDate.parse(strings[2],formatter);
                ToDoItem item = new ToDoItem(strings[0],strings[1],date);
                todolist.add(item);
            }
        }
        finally {
            if (bf!=null){
                bf.close();
            }
        }
    }
    public void storeToDoData() throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ToDoItem> iterator = todolist.iterator();
            while (iterator.hasNext()) {
                ToDoItem item = iterator.next();
                String shortDescription = item.getShortDescription();
                String detailedDescription = item.getDetailedDescription();
                String localDate = item.getDate().format(formatter);
                bw.write(String.format("%s\t%s\t%s",shortDescription,detailedDescription,localDate));
                bw.newLine();
            }
        }finally {
            if (bw!=null){
                bw.close();
            }
        }

    }

}
