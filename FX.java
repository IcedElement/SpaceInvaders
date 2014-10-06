/*
 * Finance
 * @authors:
 * 
 */

/*
 * CHANGELOG:
 *
 *
 */

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.ScrollPane;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ListView;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCombination;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class FX extends Application {
    
    private final TableView<Person> table = new TableView<>();
    private ObservableList<Person> person_data;
    private ObservableList<PieChart.Data> pie_chart_data;
    private ObservableList<String> items =FXCollections.observableArrayList("No data");
    final HBox entry_panel = new HBox();

    public static void main(String[] args) {
        // We need to call launch() to start the application.
        launch(args);
    }
    
    @Override
    public void init() {
        // Distrubute the space in the table equally between the two columns
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        person_data = FXCollections.observableArrayList();
        pie_chart_data = FXCollections.observableArrayList();
        pie_chart_data.add(new PieChart.Data("NULL",1));
    }

    // Override the start() method. We need to do this because start
    // is defined abstract in the Application class.
    public void start(Stage finance_stage) {
        
        // Update the stage image
        finance_stage.getIcons().add(new Image("images/finance.png"));

        // Give the stage a title.
        finance_stage.setTitle("Finance");
        
        // Create a root node. 
        GridPane root_node = new GridPane();

        //GridPane.setRowIndex(button, 1);
        //GridPane.setColumnIndex(button, 2)


        // PieChart

        final PieChart chart = new PieChart(pie_chart_data);
        chart.setTitle("Graphique");
        chart.setLegendVisible(false);

        VBox second_column = new VBox();
        second_column.setPadding(new Insets(10, 50, 50, 50));
        second_column.setSpacing(10);

        final Label result_header = new Label("Results");
        result_header.getStyleClass().add("result-header");

        // List View
        ListView<String> list = new ListView<>();
        list.setItems(items);

        list.setPrefWidth(50);
        list.setPrefHeight(200);
    
        second_column.getChildren().addAll(result_header,list,chart);

        final Label table_label = new Label("People");
        table_label.setFont(new Font("Cambria", 20));
        table_label.getStyleClass().add("table-header");
 
        table.setEditable(true);

        TableColumn<Person, String> name_column = new TableColumn<>("Name");
        name_column.setMinWidth(100);
        name_column.setCellValueFactory(
                    new PropertyValueFactory<Person,String>("name"));

        name_column.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
        name_column.setOnEditCommit(
            (CellEditEvent<Person, String> t) -> {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).set_name(t.getNewValue());
                // updates stuff
                update_pie_chart_list();
                List<String> res = Compute.calculate_money(person_data);
                items.clear();
                for(int count=0;count<res.size();count++){
                    items.add(res.get(count));
                }
        });

        TableColumn<Person, String> money_column = new TableColumn("Money");
        money_column.setMinWidth(100);
        money_column.setCellValueFactory(
                    new PropertyValueFactory<Person,String>("money_show"));

        money_column.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
        money_column.setOnEditCommit(
            (CellEditEvent<Person, String> t) -> {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).set_money(Float.parseFloat(t.getNewValue()));
                // updates stuff
                update_pie_chart_list();
                List<String> res = Compute.calculate_money(person_data);
                items.clear();
                for(int count=0;count<res.size();count++){
                    items.add(res.get(count));
                }
                table.getColumns().clear();
                table.getColumns().addAll(name_column, money_column); 
        });
         
        table.setItems(person_data);
        table.getColumns().addAll(name_column, money_column); 

        // Text field stuff
        final TextField add_name = new TextField();
        add_name.setPromptText("Name");
        add_name.setMaxWidth(name_column.getPrefWidth());

        final TextField add_money = new TextField();
        add_money.setMaxWidth(money_column.getPrefWidth());
        add_money.setPromptText("Money");
         
        // ATTENTION - NEEDS VERIFICATIONS!!!
        final Button add_button = new Button("Add");
        add_button.setOnAction((ActionEvent e) -> {
            Float money;
            try{
                money = Float.parseFloat(add_money.getText());
                if(add_name.getText().equals("")){
                    ShowError(0);
                }else{
                    person_data.add(new Person(add_name.getText(), money));
                    pie_chart_data.add(new PieChart.Data(add_name.getText(),
                        Float.parseFloat(add_money.getText())));
                    add_name.clear();
                    add_money.clear();
                    update_pie_chart_list();
                    List<String> res = Compute.calculate_money(person_data);
                    items.clear();
                    for(int count=0;count<res.size();count++){
                        items.add(res.get(count));
                    } 
                }    

            }catch(NumberFormatException f){
                add_money.clear();
                ShowError(1);
                money = 0f;
            }     
        });  

        // --- Menu bar
        MenuBar menuBar = new MenuBar();
 
        // --- Menu File
        Menu menuFile = new Menu("File");
        MenuItem reset = new MenuItem("reset all");
            reset.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
            reset.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    person_data.clear();
                    pie_chart_data.clear();
                    pie_chart_data.add(new PieChart.Data("NULL",1));
                    items.clear();
                }
            });
        
        MenuItem exit = new MenuItem("Exit");
            exit.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    System.exit(0);
                }
            });

        MenuItem resetMoney = new MenuItem("Reset money");
            resetMoney.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
            resetMoney.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    for(int count=0;count<person_data.size();count++){
                        person_data.get(count).set_money(0);
                    }
                    update_pie_chart_list();
                    table.getColumns().clear();
                    table.getColumns().addAll(name_column, money_column);
                    items.clear();
                    items.add("No data");
                }
            });

        menuFile.getItems().addAll(reset,resetMoney,exit);
            

        // --- Menu about
        Menu menuEdit = new Menu("About");
 
        // --- Menu Help
        Menu menuView = new Menu("Help");
 
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuBar.setUseSystemMenuBar(true);

        entry_panel.getChildren().addAll(add_name, add_money, add_button);
        entry_panel.setSpacing(3);

        final VBox first_column = new VBox();
        first_column.setSpacing(5);
        first_column.setPadding(new Insets(10, 0, 0, 10));
        first_column.getChildren().addAll(table_label, table,entry_panel);

        GridPane.setColumnSpan(menuBar,2);
        GridPane.setRowIndex(menuBar,0);
        GridPane.setColumnIndex(menuBar,0);
        GridPane.setRowIndex(first_column, 1);
        GridPane.setColumnIndex(first_column, 0);
        GridPane.setRowIndex(second_column,1);
        GridPane.setColumnIndex(second_column,1);

        // Create a scene
        // Numbers represent window sizes
        Scene finance_scene = new Scene(root_node, 850, 500);
      
        // Set the scene on the stage.

        root_node.getChildren().addAll(first_column);
        root_node.getChildren().add(second_column);
        root_node.getChildren().add( menuBar);
        
        finance_scene.getStylesheets().add("finance_style.css"); 
        finance_stage.setScene(finance_scene);  

        // EXAMPLE - DO NOT UNCOMMENT!!!
        //root_node.getChildren().add(button2);
        
        // Show the stage and its scene.
        finance_stage.show();
    }

    private void update_pie_chart_list () {
        pie_chart_data.clear();
        for (int counter = 0; counter < person_data.size(); counter++) {
            Person current_person = person_data.get(counter);
            pie_chart_data.add(new PieChart.Data(current_person.get_name(),
                                current_person.get_money_spent()));
        }
    }

    private void ShowError(int number){
        Stage dialogStage = new Stage();
        GridPane root_dialogue = new GridPane();
        root_dialogue.setAlignment(Pos.CENTER);
        root_dialogue.setHgap(10);
        root_dialogue.setVgap(10);//pading
        Scene dialogScene = new Scene(root_dialogue,400,150);
        Image image = new Image("fail.png");
        ImageView image_error = new ImageView();
        image_error.setImage(image);
        final Button b_dialogue = new Button("Close");
        b_dialogue.setOnAction((ActionEvent g) -> {
            dialogStage.close();
        });
        String comment = "";
        switch(number){
            case 1:
                comment = "Please enter a number ! ";
                break;
            case 0:
                comment = "Please enter a name !";
                break;
            case 2:
                comment = "No save file found !";
                break;
            case 3:
                comment = "Could not save file !";
                break;
            case 4:
                comment = "Could not load file !";
                break;
        }
        Label l_dialogue = new Label(comment);
        root_dialogue.add(image_error,0,0);
        root_dialogue.add(l_dialogue,1,0);
        root_dialogue.add(b_dialogue,1,1);

        dialogStage.setTitle("Error");
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    
    // Override the stop() method.
    public void stop() {
        System.out.println("Inside the stop() method.");
    }

}
