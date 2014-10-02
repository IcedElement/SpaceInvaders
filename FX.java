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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ListView;
import java.util.List;


public class FX extends Application {
    
    private final TableView<Person> table = new TableView<>();
    private ObservableList<Person> person_data;
    private ObservableList<PieChart.Data> pie_chart_data;
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
        //person_data.add(new Person("Theo", 23.0f));
        //person_data.add(new Person("Tristan", 24.0f));
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
        //GridPane.setColumnIndex(button, 2);

        // PieChart

        final PieChart chart = new PieChart(pie_chart_data);
        chart.setTitle("Graphique");
        chart.setLegendVisible(false);

        VBox second_column = new VBox();
        second_column.setPadding(new Insets(10, 50, 50, 50));
        second_column.setSpacing(10);

        final Label result_header = new Label("Results");
        result_header.getStyleClass().add("result-header");
        //result_header.setFont(new Font("Cambria" , 20));
        
        /*
        final Label result_field = new Label("No data");
        result_field.getStyleClass().add("result-field"); 
        result_field.setWrapText(true);
        result_field.setMinWidth(400);
        result_field.setMinHeight(10000);
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(120, 120);
        s1.setPrefWidth(400);
        s1.setPrefHeight(100);
        s1.setContent(result_field);
        s1.setHbarPolicy(ScrollBarPolicy.NEVER);
        s1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    
        /*
        ScrollPane result_scroll = new ScrollPane();
        result_scroll.setMinWidth(400);
        result_scroll.setMinHeight(100);
        result_scroll.setContent(result_field);
        */

        ListView<String> list = new ListView<>();
        ObservableList<String> items =FXCollections.observableArrayList(
         "No data");
        list.setItems(items);

        second_column.getChildren().addAll(result_header,list,chart);

        final Label table_label = new Label("People");
        table_label.setFont(new Font("Cambria", 20));
        table_label.getStyleClass().add("table-header");
 
        table.setEditable(true);

        TableColumn name_column = new TableColumn("Name");
        name_column.setMinWidth(100);
        name_column.setCellValueFactory(
                    new PropertyValueFactory<Person,String>("name"));

        TableColumn money_column = new TableColumn("Money");
        money_column.setMinWidth(100);
        money_column.setCellValueFactory(
                    new PropertyValueFactory<Person,String>("money_show"));
         
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
            person_data.add(new Person(
                add_name.getText(),
                Float.parseFloat(add_money.getText())
            ));
            pie_chart_data.add(new PieChart.Data(add_name.getText(),
                Float.parseFloat(add_money.getText())));
            add_name.clear();
            add_money.clear();
            List<String> res = Compute.calculate_money(person_data);
            items.clear();
            for(int count=0;count<res.size();count++){
                items.add(res.get(count));
            }
            //result_field.setText(res);
        });  

        entry_panel.getChildren().addAll(add_name, add_money, add_button);
        entry_panel.setSpacing(3);

        final VBox first_column = new VBox();
        first_column.setSpacing(5);
        first_column.setPadding(new Insets(10, 0, 0, 10));
        first_column.getChildren().addAll(table_label, table,entry_panel);

        GridPane.setRowIndex(first_column, 0);
        GridPane.setColumnIndex(first_column, 0);
        GridPane.setRowIndex(second_column,0);
        GridPane.setColumnIndex(second_column,1);

        // Create a scene
        // Numbers represent window sizes
        Scene finance_scene = new Scene(root_node, 850, 500);
      
        // Set the scene on the stage.

        root_node.getChildren().addAll(first_column);
        root_node.getChildren().add(second_column);
        
        finance_scene.getStylesheets().add("finance_style.css"); 
        finance_stage.setScene(finance_scene);  

        // EXAMPLE - DO NOT UNCOMMENT!!!
        //root_node.getChildren().add(button2);
        
        // Show the stage and its scene.
        finance_stage.show();
    }
    
    // Override the stop() method.
    public void stop() {
        System.out.println("Inside the stop() method.");
    }

}

// EXAMPLES

    /*example
    Button button2 = new Button("Accept");

    button2.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent ae) {
        String[] test  = {"ad","ad"};
        Finance.main(test);
        }
    });
    */

    /* image and label examples
    Image image = new Image(getClass().getResourceAsStream("tests.jpg"));
    Label label3 = new Label("PARTAGE", new ImageView(image));
    Label label3 = new Label("PARTAGE");
    label3.setFont(Font.font("Cambria", 32));
    */