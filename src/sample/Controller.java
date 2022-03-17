package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import todopackage.ToDoData;
import todopackage.ToDoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private BorderPane borderPaneId;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private ListView<ToDoItem> myListView;
    @FXML
    private TextArea myTextArea;
    @FXML
    private Label myDateLabel;
    @FXML
    private Button button;
    @FXML
    private FilteredList<ToDoItem> filteredList;
    private SortedList<ToDoItem> sortedList;

    Predicate<ToDoItem> myFilter = new Predicate<ToDoItem>() {
        @Override
        public boolean test(ToDoItem item) {
            return (item.getDate().equals(LocalDate.now()));
        }
    };
    Comparator<ToDoItem> myComparator = new Comparator<ToDoItem>() {
        @Override
        public int compare(ToDoItem item1, ToDoItem item2) {
            return (item1.getDate().compareTo(item2.getDate()));
        }
    };

    public void initialize() {
        contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem();
        deleteMenuItem.setText("delete");
        contextMenu.getItems().addAll(deleteMenuItem);
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) {
                try {
                    ToDoItem item = myListView.getSelectionModel().getSelectedItem();
                    myTextArea.setText(item.getDetailedDescription());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    myDateLabel.setText(df.format(item.getDate()));
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
        }
    );

        filteredList = new FilteredList<>(ToDoData.getInstance().getTodolist(), myFilter);
        sortedList = new SortedList<>(ToDoData.getInstance().getTodolist(), myComparator);
        myListView.setItems(sortedList);
        myListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        myListView.getSelectionModel().selectFirst();
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteItem();
            }
        });
        myListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> toDoItemListView) {
                ListCell<ToDoItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean b) {
                        super.updateItem(item, b);
                        if (item == null) {
                            setText(null);
                        } else if (item.getDate().isBefore(LocalDate.now())) {
                            setText(item.getShortDescription());
                            setTextFill(Color.RED);
                        } else if (item.getDate().equals(LocalDate.now())) {
                            setText(item.getShortDescription());
                            setTextFill(Color.ORANGE);
                        } else {
                            setText(item.getShortDescription());
                            setTextFill(Color.BLACK);
                        }

                    }
                };
                if (cell.isEmpty()) {
                    cell.setContextMenu(contextMenu);
                }
                return cell;
            }

        });
    }

    public void exitPressed() {

        Platform.exit();
    }

    public void showNewItemDailog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPaneId.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addToDoItemDailog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            //
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == (ButtonType.OK)) {
            DialogController controller = fxmlLoader.getController();
            controller.getNewToDoItem();
            myListView.getSelectionModel().selectFirst();
        }

    }

    public void deletePressed(KeyEvent event) {
        if ((event.getCode().equals(KeyCode.DELETE)) || (event.getCode().equals(KeyCode.BACK_SPACE))) {
            deleteItem();
        }
    }

    public void deleteItem() throws NullPointerException {

        ToDoItem item = myListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Selected Item");
        alert.setHeaderText("delete item: " + item.getShortDescription());
        alert.setContentText("Are You Sure You Want To Delete The Item");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            ToDoData.getInstance().getTodolist().remove(item);
        }

    }

    public void toggleButtonPressed() {
        ToDoItem item = myListView.getSelectionModel().getSelectedItem();
        if (toggleButton.isSelected()) {
            myListView.setItems(sortedList.filtered(myFilter));

            if (filteredList.isEmpty()) {
                myDateLabel.setText(null);
                myTextArea.clear();
            } else if (filteredList.contains(item)) {
                myListView.getSelectionModel().select(item);
            } else {
                myListView.getSelectionModel().selectFirst();
            }
        } else {
            myListView.setItems(sortedList);
            myListView.getSelectionModel().select(item);
        }
    }
}
