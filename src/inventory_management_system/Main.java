package inventory_management_system;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
                    new InHouse(2,"Light", 1.31, 1, 1, 12, 5),
                    new Outsourced(2,"Bumper", 35.20, 1, 1, 12, "Super Bikes")
            );
    /******************* Add Part Form *******************/
    private Label addPartLabel = new Label("Add Part");
    private RadioButton inHouse = new RadioButton("In-House");
    private RadioButton outsourced = new RadioButton("Outsourced");
    private TextField partIdTextField = new TextField();
    private TextField partNameTextField = new TextField();
    private TextField partInventoryTextField = new TextField();
    private TextField partPriceTextField = new TextField();
    private TextField partMaxTextField = new TextField();
    private TextField partMinTextField = new TextField();
    private Label partIdOrNameLabel = new Label("Machine ID");
    private TextField partIdOrNameTextField = new TextField();
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
        addPartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // update main form label
                resetAddModifyPartForm();
                primaryStage.setScene(addPartFormScene);
            }
        });
        Button modifyPartButton = new Button("Modify");
        modifyPartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int index = partsTable.getSelectionModel().getSelectedIndex();
                if (index == -1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must select a part to modify");
                    errorAlert.showAndWait();
                } else {
                    updateAddModifyPartForm(index);
                    primaryStage.setScene(addPartFormScene);
                }
            }
        });
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
        addPartLabel.setId("add-part-main-label");
        addPartLabel.getStyleClass().add("add-part-field-labels");
        ToggleGroup partTypeGroup = new ToggleGroup();

        inHouse.setId("add-part-in-house");
        inHouse.setToggleGroup(partTypeGroup);
        inHouse.setSelected(true);

        outsourced.setToggleGroup(partTypeGroup);
        addPartTypePane.getStyleClass().add("add-part-field-panes");
        addPartTypePane.setId("add-part-type-pane");
        addPartTypePane.getChildren().addAll(addPartLabel, inHouse, outsourced);

        /******************* addPartFormLayout - middle *******************/
        // ID
        Label partIdLabel = new Label("ID");
        partIdLabel.getStyleClass().add("add-part-field-labels");
        partIdTextField.setPromptText("Auto Gen - Disabled");
        partIdTextField.setDisable(true);
        HBox partIdPane = new HBox();
        partIdPane.getStyleClass().add("add-part-field-panes");
        partIdPane.getChildren().addAll(partIdLabel, partIdTextField);

        // Name
        Label partNameLabel = new Label("Name");
        partNameLabel.getStyleClass().add("add-part-field-labels");
        HBox partNamePane = new HBox();
        partNamePane.getStyleClass().add("add-part-field-panes");
        partNamePane.getChildren().addAll(partNameLabel, partNameTextField);

        // Inventory
        Label partInventoryLabel = new Label("Inv");
        partInventoryLabel.getStyleClass().add("add-part-field-labels");
        HBox partInventoryPane = new HBox();
        partInventoryPane.getStyleClass().add("add-part-field-panes");
        partInventoryPane.getChildren().addAll(partInventoryLabel, partInventoryTextField);

        // Price/Cost
        Label partPriceLabel = new Label("Price/Cost");
        partPriceLabel.getStyleClass().add("add-part-field-labels");
        HBox partPricePane = new HBox();
        partPricePane.getStyleClass().add("add-part-field-panes");
        partPricePane.getChildren().addAll(partPriceLabel, partPriceTextField);

        // Max and Min
        Label partMaxLabel = new Label("Max");
        partMaxLabel.getStyleClass().add("add-part-field-labels");
        Label partMinLabel = new Label("Min");
        partMinLabel.setId("add-part-min-label");
        HBox partMaxAndMinPane = new HBox();
        partMaxAndMinPane.getStyleClass().add("add-part-field-panes");
        partMaxAndMinPane.getChildren().addAll(partMaxLabel, partMaxTextField, partMinLabel, partMinTextField);

        // Machine ID or Company Name
        partIdOrNameLabel.getStyleClass().add("add-part-field-labels");
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

    private void updateAddModifyPartForm(int index) {
        addPartLabel.setText("Modify Part");
        Part selectedPart = data.get(index);
        if (selectedPart.getClass() == InHouse.class) {
            inHouse.setSelected(true);
            partIdOrNameLabel.setText("Machine ID");
            partIdOrNameTextField.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
        } else if (selectedPart.getClass() == Outsourced.class) {
            outsourced.setSelected(true);
            partIdOrNameLabel.setText("Company Name");
            partIdOrNameTextField.setText(((Outsourced) selectedPart).getCompanyName());
        }
        partIdTextField.setText(String.valueOf(selectedPart.getId()));
        partNameTextField.setText(selectedPart.getName());
        partInventoryTextField.setText(String.valueOf(selectedPart.getStock()));
        partPriceTextField.setText(String.valueOf(selectedPart.getPrice()));
        partMaxTextField.setText(String.valueOf(selectedPart.getMax()));
        partMinTextField.setText(String.valueOf(selectedPart.getMin()));
    }

    private void resetAddModifyPartForm() {
        addPartLabel.setText("Add Part");
        inHouse.setSelected(true);
        partIdTextField.setText("");
        partIdTextField.setPromptText("Auto Gen - Disabled");
        partNameTextField.setText("");
        partInventoryTextField.setText("");
        partPriceTextField.setText("");
        partMaxTextField.setText("");
        partMinTextField.setText("");
        partIdOrNameLabel.setText("Machine ID");
        partIdOrNameTextField.setText("");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
