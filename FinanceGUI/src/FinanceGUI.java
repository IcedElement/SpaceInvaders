
// The base of the application
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.scene.chart.PieChart;

public class FinanceGUI extends Application {
    
    public static void main(String[] args) {
        // We need to call launch() to start the application.
        launch(args);
    }
    private Object scene;
    
    // Override the init() method.
    @Override
    public void init() {
        System.out.println("Inside the init() method.");
    }

    // Override the start() method. We need to do this because start
    // is defined abstract in the Application class.
    @Override
    public void start(Stage finance_stage) {
        
        // Update the stage image
        finance_stage.getIcons().add(new Image("images\finance.png"));

        // Give the stage a title.
        finance_stage.setTitle("Finance");
        
        // Create a root node. In this case, a flow layout
        // is used, but several alternatives exist.
        GridPane root_node = new GridPane();
       
        Scene finance_scene = new Scene(root_node,500, 500);
        
        //Pie Chart
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("NULL", 100)
                );
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Graphique");
        // A discuter
        chart.setLegendVisible(false);
        set_PieChart_GUI(pieChartData,"Jac",100);
        root_node.getChildren().add(chart);
        



        // Set the scene on the stage.
        finance_stage.setScene(finance_scene);

        // EXAMPLE - DO NOT UNCOMMENT!!!
        //root_node.getChildren().add(button2);
        
        // Show the stage and its scene.
        finance_stage.show();

    }
    
    public void set_PieChart_GUI(ObservableList<PieChart.Data> Data, String name, int money){
          Data.add(new PieChart.Data(name, money));
          Data.add(new PieChart.Data("Bonjon",44));
    }
    
    // Override the stop() method.
    @Override
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
