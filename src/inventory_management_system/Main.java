package inventory_management_system;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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
    /******************* Add Part Form *******************/
    private VBox addPartFormLayout = new VBox();
    private Button cancelAddPart = new Button("Cancel");
    private Button saveAddPart = new Button("Save");
    private Scene addPartFormScene = new Scene(addPartFormLayout);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("inventory_management_system.fxml"));
        primaryStage.setTitle("Inventory Management System");
        Scene mainFormScene = new Scene(new Group());


        // test start
        initializeAddPartForm();
        cancelAddPart.setOnAction(e -> primaryStage.setScene(mainFormScene));
        // test end

        mainFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
        addPartFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());

        // Main Pane/Form top
        Label mainFormTitle = new Label("Inventory Management System");
        mainFormTitle.setId("mainFormTitle");

        // Main Pane/Form middle
        HBox partsAndProductsPane = new HBox();
        VBox partsPane = new VBox();    // left
        VBox productsPane = new VBox(); // right

        partsPane.getStyleClass().add("parts-products-pane");

        productsPane.getStyleClass().add("parts-products-pane");

        partsAndProductsPane.getStyleClass().add("parts-products-parent-pane");
        partsAndProductsPane.getChildren().addAll(partsPane, productsPane);

        // Main Pane/Form bottom
        Button exitButton = new Button("Exit");
        exitButton.setId("exit-button");
        HBox exitButtonPane = new HBox();
        exitButtonPane.getStyleClass().add("exit-button-pane");
        exitButtonPane.getChildren().addAll(exitButton);
        // Parts pane layout
        HBox partsLabelAndSearchBox = new HBox();        // top
        // table view middle
        HBox crudButtonsPane = new HBox();              // bottom
        partsPane.getChildren().addAll(partsLabelAndSearchBox, partsTable, crudButtonsPane);

        // Parts pane top elements
        Label partsPaneTitle = new Label("Parts");
        partsPaneTitle.setId("parts-pane-title");
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by part ID or Name");
        partsSearchBox.setFocusTraversable(false);
        partsLabelAndSearchBox.setId("parts-label-and-search-box");
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
        partsTable.getStyleClass().add("parts-and-products-table");
        partsTable.getColumns().addAll(partIDHeader, partNameHeader, inventoryHeader, priceHeader);


        // Parts pane bottom elements
        crudButtonsPane.getStyleClass().add("crud-buttons-pane");
        Button addPartButton = new Button("Add");
        addPartButton.setOnAction(e -> primaryStage.setScene(addPartFormScene));
        Button modifyPartButton = new Button("Modify");
        Button deletePartButton = new Button("Delete");
        crudButtonsPane.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);

        // Main pane config
        VBox mainPane = new VBox();
        mainPane.getStyleClass().add("main-pane");
        mainPane.getChildren().addAll(mainFormTitle, partsAndProductsPane, exitButtonPane);

        // Add main pane to scene and main scene to the stage
        ((Group) mainFormScene.getRoot()).getChildren().addAll(mainPane);
        primaryStage.setScene(mainFormScene);
        primaryStage.show();
    }

    private void initializeAddPartForm() {
        /******************* addPartFormLayout - top *******************/
        HBox addPartTypePane = new HBox();
        Label addPartLabel = new Label("Add Part");
        addPartLabel.setId("add-part-main-label");
        addPartLabel.getStyleClass().add("add-part-field-labels");
        ToggleGroup partTypeGroup = new ToggleGroup();
        RadioButton inHouse = new RadioButton("In-House");
        inHouse.setId("add-part-in-house");
        inHouse.setToggleGroup(partTypeGroup);
        inHouse.setSelected(true);
        RadioButton outsourced = new RadioButton("Outsourced");
        outsourced.setToggleGroup(partTypeGroup);
        addPartTypePane.getStyleClass().add("add-part-field-panes");
        addPartTypePane.setId("add-part-type-pane");
        addPartTypePane.getChildren().addAll(addPartLabel, inHouse, outsourced);

        /******************* addPartFormLayout - middle *******************/
        // ID
        Label partIdLabel = new Label("ID");
        partIdLabel.getStyleClass().add("add-part-field-labels");
        TextField partIdTextField = new TextField();
        //partIdTextField.getStyleClass().add("add-part-text-fields");
        partIdTextField.setPromptText("Auto Gen - Disabled");
        partIdTextField.setDisable(true);
        HBox partIdPane = new HBox();
        partIdPane.getStyleClass().add("add-part-field-panes");
        partIdPane.getChildren().addAll(partIdLabel, partIdTextField);

        // Name
        Label partNameLabel = new Label("Name");
        partNameLabel.getStyleClass().add("add-part-field-labels");
        TextField partNameTextField = new TextField();
        HBox partNamePane = new HBox();
        partNamePane.getStyleClass().add("add-part-field-panes");
        partNamePane.getChildren().addAll(partNameLabel, partNameTextField);

        // Inventory
        Label partInventoryLabel = new Label("Inv");
        partInventoryLabel.getStyleClass().add("add-part-field-labels");
        TextField partInventoryTextField = new TextField();
        HBox partInventoryPane = new HBox();
        partInventoryPane.getStyleClass().add("add-part-field-panes");
        partInventoryPane.getChildren().addAll(partInventoryLabel, partInventoryTextField);

        // Price/Cost
        Label partPriceLabel = new Label("Price/Cost");
        partPriceLabel.getStyleClass().add("add-part-field-labels");
        TextField partPriceTextField = new TextField();
        HBox partPricePane = new HBox();
        partPricePane.getStyleClass().add("add-part-field-panes");
        partPricePane.getChildren().addAll(partPriceLabel, partPriceTextField);

        // Max and Min
        Label partMaxLabel = new Label("Max");
        partMaxLabel.getStyleClass().add("add-part-field-labels");
        TextField partMaxTextField = new TextField();
        Label partMinLabel = new Label("Min");
        partMinLabel.setId("add-part-min-label");
        TextField partMinTextField = new TextField();
        HBox partMaxAndMinPane = new HBox();
        partMaxAndMinPane.getStyleClass().add("add-part-field-panes");
        partMaxAndMinPane.getChildren().addAll(partMaxLabel, partMaxTextField, partMinLabel, partMinTextField);

        // Machine ID or Company Name
        Label partIdOrNameLabel = new Label("Machine ID");
        partIdOrNameLabel.getStyleClass().add("add-part-field-labels");
        TextField partIdOrNameTextField = new TextField();
        HBox partIdOrNamePane = new HBox();
        partIdOrNamePane.getStyleClass().add("add-part-field-panes");
        partIdOrNamePane.getChildren().addAll(partIdOrNameLabel, partIdOrNameTextField);

        VBox addPartFieldsPane = new VBox();
        addPartFieldsPane.getChildren().addAll(partIdPane, partNamePane, partInventoryPane, partPricePane,
                partMaxAndMinPane, partIdOrNamePane);

        /******************* addPartFormLayout - bottom *******************/
        HBox saveAndCancelButtonsPane = new HBox();
        saveAndCancelButtonsPane.setId("add-part-save-cancel-buttons-pane");
        saveAndCancelButtonsPane.getChildren().addAll(saveAddPart, cancelAddPart);

        addPartFormLayout.getStyleClass().add("add-form-main-pane");
        addPartFormLayout.getChildren().addAll(addPartTypePane, addPartFieldsPane, saveAndCancelButtonsPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
