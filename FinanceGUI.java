/*
 * Finance
 * Authors: Tristan / Théo
 * Date: 03/10/14
 * Version: 1.0 (beta)
 */

import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class FinanceGUI extends Application {
    
    private Stage app_stage;
    private final TableView<Person> table = new TableView<>();
    private ObservableList<Person> person_data;
    private ObservableList<PieChart.Data> pie_chart_data;
    private ObservableList<String> items =FXCollections.observableArrayList("No data");
    final HBox entry_panel = new HBox();
    // Let's the stop() function now if it was called through the Exit button
    private boolean exitFlag = false;

    public static void main(String[] args) {
        // We need to call launch() to start the application.
        launch(args);
    }
    
    @Override
    public void init() {
        // Distrubute the space in the table equally between the two columns
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Initialise the list holding the data for each Person
        person_data = FXCollections.observableArrayList();
        // Initialise the list used by the chart
        pie_chart_data = FXCollections.observableArrayList();
        // Add a sentinel value to make the empty chart appear on the screen
        pie_chart_data.add(new PieChart.Data("NULL",1));
        // Try to load the last session from the backup file
        try {
            File backupFile = new File("backup.fin");
            load_file(backupFile);    
        }
        // backup.fin file does not exist
        catch (Exception ex) {
            //ShowError(2);
        }
    }

    // Override the start() method. We need to do this because start
    // is defined abstract in the Application class.
    public void start(Stage finance_stage) {
        
        // Save the reference to the main stage in the app_stage attribut,
        // so that it can be used by other functions
        app_stage = finance_stage;    

        // Disable resizing
        finance_stage.setResizable(false);
        
        // Update the application image
        finance_stage.getIcons().add(new Image("images/finance.png"));

        // Give the stage a title.
        finance_stage.setTitle("Finance");
        
        // Create a root node. 
        GridPane root_node = new GridPane();

        // PieChart
        final PieChart chart = new PieChart(pie_chart_data);
        chart.setTitle("Chart");
        // Disables the legend to save space on the page
        chart.setLegendVisible(false);

        VBox second_column = new VBox();
        second_column.setPadding(new Insets(10, 50, 50, 50));
        second_column.setSpacing(10);

        // Create the results header
        final Label result_header = new Label("Results");
        result_header.getStyleClass().add("result-header");

        // Create the results list
        ListView<String> list = new ListView<>();
        list.setItems(items);
        // Set the list dimensions
        list.setPrefWidth(50);
        list.setPrefHeight(200);
    
        // Pack the header, list and chart to the vbox
        second_column.getChildren().addAll(result_header,list,chart);
        
        // Data entry table
        final Label table_label = new Label("People");
        table_label.setFont(new Font("Cambria", 20));
        table_label.getStyleClass().add("table-header");
        // Enable cell editing
        table.setEditable(true);

        // Create the name column
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
                // Update the display
                update_pie_chart_list();
                List<String> res = Compute.calculate_money(person_data);
                items.clear();
                for(int count=0;count<res.size();count++){
                    items.add(res.get(count));
                }
        });

        // Create the money column
        TableColumn<Person, String> money_column = new TableColumn("Money");
        money_column.setMinWidth(100);
        money_column.setCellValueFactory(
                    new PropertyValueFactory<Person,String>("money_show"));
        // Handle cell editing
        money_column.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
        money_column.setOnEditCommit(
            (CellEditEvent<Person, String> t) -> {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).set_money(Float.parseFloat(t.getNewValue()));
                // Update the display
                update_pie_chart_list();
                List<String> res = Compute.calculate_money(person_data);
                items.clear();
                for(int count=0;count<res.size();count++){
                    items.add(res.get(count));
                }
                // Refresh the data table (fixes display problems)
                table.getColumns().clear();
                table.getColumns().addAll(name_column, money_column); 
        });
         
        // Bind the list containing the Persons to the data table
        table.setItems(person_data);
        // Pack columns to the table
        table.getColumns().addAll(name_column, money_column); 

        // Create the text entry fields
        final TextField add_name = new TextField();
        add_name.setPromptText("Name");
        add_name.setMaxWidth(name_column.getPrefWidth());

        final TextField add_money = new TextField();
        add_money.setMaxWidth(money_column.getPrefWidth());
        add_money.setPromptText("Money");
         
        // Create Add button
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

        // Create the menu bar
        MenuBar menuBar = new MenuBar();
 
        // Create the file menu
        Menu menuFile = new Menu("File");

        // Create and handle the File->Reset All option
        MenuItem reset = new MenuItem("Reset All");
            reset.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
            reset.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    person_data.clear();
                    pie_chart_data.clear();
                    pie_chart_data.add(new PieChart.Data("NULL",1));
                    items.clear();
                }
            });
        
        // Create and handle the File->Exit option
        MenuItem exit = new MenuItem("Exit");
            exit.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    exitFlag = true;
                    stop();
                }
            });

        // Create and handle the File->Save option
        MenuItem save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    FileChooser fileChooser = new FileChooser();
         
                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("FIN files (*.fin)", "*.fin");
                    fileChooser.getExtensionFilters().add(extFilter);
                     
                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(app_stage);
                     
                    if(file != null){
                        save_person_data(file);
                    }
                }
            });

        // Create and handle the File->Load option
        MenuItem load = new MenuItem("Load");
        load.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        load.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();

                // Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                            "FIN files (*.fin)", "*.fin");
                fileChooser.getExtensionFilters().add(extFilter);

                // Show save file dialog
                File file = fileChooser.showOpenDialog(app_stage);

                if (file != null) {
                    load_file(file);
                }
            }
        });

        // Create and handle the File->Reset Money option
        MenuItem resetMoney = new MenuItem("Reset Money");
            resetMoney.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
            resetMoney.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    for(int count=0;count<person_data.size();count++){
                        person_data.get(count).set_money(0);
                    }
                    pie_chart_data.clear();
                    pie_chart_data.add(new PieChart.Data("NULL",1));
                    table.getColumns().clear();
                    table.getColumns().addAll(name_column, money_column);
                    items.clear();
                    items.add("No data");
                }
            });

        // Pack the file options to the File menu
        menuFile.getItems().addAll(reset,resetMoney,save,load,exit);

        // Create the About menu
        Menu menuEdit = new Menu("About");
 
        // Create the Help menu
        Menu menuView = new Menu("Help");
 
        // Pack the menus to the menu bar
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        // Send the bar to the system bar (Mac)
        menuBar.setUseSystemMenuBar(true);

        // Pack the entry fields to the entry panel (hbox)
        entry_panel.getChildren().addAll(add_name, add_money, add_button);
        entry_panel.setSpacing(3);

        // Create the column containg the data table and the entry panel
        final VBox first_column = new VBox();
        first_column.setSpacing(5);
        first_column.setPadding(new Insets(10, 0, 0, 10));

        // Pack the table and the entry panel to the vbox
        first_column.getChildren().addAll(table_label, table,entry_panel);

        // Set container positions in the grid
        GridPane.setColumnSpan(menuBar,2);
        GridPane.setRowIndex(menuBar,0);
        GridPane.setColumnIndex(menuBar,0);
        GridPane.setRowIndex(first_column, 1);
        GridPane.setColumnIndex(first_column, 0);
        GridPane.setRowIndex(second_column,1);
        GridPane.setColumnIndex(second_column,1);

        // Create a scene
        // Numbers represent window sizes
        Scene finance_scene = new Scene(root_node, 850, 515);
      
        // Set the scene on the stage.

        root_node.getChildren().addAll(first_column);
        root_node.getChildren().add(second_column);
        root_node.getChildren().add( menuBar);
        
        // Bind the css file to the scene
        finance_scene.getStylesheets().add("finance_style.css"); 
        finance_stage.setScene(finance_scene);  
        
        // Show the stage
        finance_stage.show();
    }

    private void save_person_data(File file) {
        try {
            String content = prepare_person_data();
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            ShowError(3);
        }
    }

    private void update_pie_chart_list () {
        pie_chart_data.clear();
        boolean flag_added = false;
        for (int counter = 0; counter < person_data.size(); counter++) {
            Person current_person = person_data.get(counter);
            pie_chart_data.add(new PieChart.Data(current_person.get_name(),
                                current_person.get_money_spent()));
            flag_added = true;
        }
        if (! flag_added) {
            pie_chart_data.add(new PieChart.Data("NULL",1));
        }
    }

    private void ShowError(int number){
        Stage dialogStage = new Stage();
        // Disable resizing
        dialogStage.setResizable(false);
        GridPane root_dialogue = new GridPane();
        root_dialogue.setAlignment(Pos.CENTER);
        root_dialogue.setHgap(10);
        root_dialogue.setVgap(10);//pading
        Scene dialogScene = new Scene(root_dialogue,250,150);
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
                comment = "Please enter a number! ";
                break;
            case 0:
                comment = "Please enter a name!";
                break;
            case 2:
                comment = "No save file found!";
                break;
            case 3:
                comment = "Could not save file!";
                break;
            case 4:
                comment = "Could not load file!";
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

    private String prepare_person_data() {
        String result = "";
        for (int counter = 0; counter < person_data.size(); counter++) {
            Person current = person_data.get(counter);
            result += current.get_name();
            result += "::";
            result += current.get_money_spent();
            if (counter < person_data.size()-1) {
                result += "::";
            }
        }
        return result;
    }
    
    private void load_file (File file) {
        try {
            person_data.clear();
            FileReader input = new FileReader(file);
            BufferedReader bufRead = new BufferedReader(input);
            String myLine = null;
            while ( (myLine = bufRead.readLine()) != null) {    
                String[] array1 = myLine.split("::");
                for (int counter = 0; counter < array1.length; counter+=2) {
                    String name = array1[counter];
                    String temp_money = array1[counter+1];
                    float money = Float.parseFloat(temp_money);
                    Person new_person = new Person(name, money);
                    person_data.add(new_person);
                }
            }
            // update the display
            update_pie_chart_list();
            List<String> res = Compute.calculate_money(person_data);
            items.clear();
            boolean flag_added = false;
            for(int count=0;count<res.size();count++){
                items.add(res.get(count));
                flag_added = true;
            }
        } 
        catch (IOException ex) {
            ShowError(4);
        }
    }

    // Override the stop() method.
    public void stop() {
        // Save the data currently in the app
        File saveFile = new File("backup.fin");
        save_person_data(saveFile);
        // Destroy the stage if called through the Exit button
        if (exitFlag) {
            app_stage.close();
        }
    }


}
