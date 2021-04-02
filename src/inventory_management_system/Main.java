package inventory_management_system;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private TableView partsTable = new TableView();
    private final ObservableList<Part> data =
            FXCollections.observableArrayList(
                    new InHouse(0,"Rim", 2.18, 3, 1, 12, 2),
                    new InHouse(1,"Brake", 5.37, 7, 1, 12, 1),
                    new InHouse(2,"Light", 1.31, 1, 1, 12, 5)
            );

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("inventory_management_system.fxml"));
        primaryStage.setTitle("Inventory Management System");
        Scene scene = new Scene(new Group());

        // Main Pane/Form top
        Label mainFormTitle = new Label("Inventory Management System");

        // Main Pane/Form middle
        HBox partsAndProductsPane = new HBox();
        VBox partsPane = new VBox();    // left
        VBox productsPane = new VBox(); // right
        partsAndProductsPane.getChildren().addAll(partsPane, productsPane);

        // Main Pane/Form bottom
        Button exitButton = new Button("Exit");


        // Parts pane layout
        HBox partsLabelAndSearchBox = new HBox();        // top
        // table view middle
        HBox crudButtonsPane = new HBox();              // bottom
        partsPane.getChildren().addAll(partsLabelAndSearchBox, partsTable, crudButtonsPane);

        // Parts pane top elements
        Label partsPaneTitle = new Label("Parts");
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by part ID or Name");
        partsSearchBox.setFocusTraversable(false);
        partsLabelAndSearchBox.getChildren().addAll(partsPaneTitle, partsSearchBox);

        // Parts pane middle elements
        TableColumn partIDHeader = new TableColumn("Part ID");
        partIDHeader.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));

        TableColumn partNameHeader = new TableColumn("Part Name");
        partNameHeader.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        TableColumn inventoryHeader = new TableColumn("Inventory Level");
        inventoryHeader.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        TableColumn priceHeader = new TableColumn("Price/Cost per Unit");
        priceHeader.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        partsTable.setItems(data);
        partsTable.getColumns().addAll(partIDHeader, partNameHeader, inventoryHeader, priceHeader);
        partsTable.setMaxHeight(150);

        // Parts pane bottom elements
        crudButtonsPane.setSpacing(5);
        crudButtonsPane.setPadding(new Insets(10,0,0,10));
        Button addPartButton = new Button("Add");
        Button modifyPartButton = new Button("Modify");
        Button deletePartButton = new Button("Delete");
        crudButtonsPane.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);
        crudButtonsPane.setAlignment(Pos.BASELINE_RIGHT);

        // Main pane config
        VBox mainPane = new VBox();
        mainPane.setSpacing(5);
        mainPane.setPadding(new Insets(10,0,0,10));
        mainPane.getChildren().addAll(mainFormTitle, partsAndProductsPane, exitButton);

        // Add main pane to scene and main scene to the stage
        ((Group) scene.getRoot()).getChildren().addAll(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
