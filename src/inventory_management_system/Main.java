package inventory_management_system;
// Exception in thread "JavaFX Application Thread" java.lang.NumberFormatException

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    // Use to assign part id to newly created parts
    private int partsCount = 0;

    // Table that will display all parts available
    private TableView partsTable = new TableView();

    // Column that data will be sorted on
    private TableColumn partIDHeader;

    // Main inventory object
    private Inventory inventory = new Inventory();
    //private ObservableList<Part> data;

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
        primaryStage.setTitle("Software I Task");
        Scene mainFormScene = new Scene(new Group());

        /******************* Create dummy part data to display *******************/
        inventory.addPart(new InHouse(partsCount,"Rim", 2.18, 3, 1, 12, 2));
        ++partsCount;
        inventory.addPart(new InHouse(partsCount,"Brake", 5.37, 7, 1, 12, 1));
        ++partsCount;
        inventory.addPart(new InHouse(partsCount,"Light", 1.31, 1, 1, 12, 5));
        ++partsCount;
        inventory.addPart(new Outsourced(partsCount,"Bumper", 35.20, 1, 1, 12,
                "Super Bikes"));
        ++partsCount;
        /******************* End of dummy data creation *******************/

        // Initialize Add/Modify form
        initializeAddPartForm();
        cancelAddPart.setOnAction(e -> primaryStage.setScene(mainFormScene));

        // Apply external css styling to forms and its elements
        mainFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
        addPartFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());

        /******************* Main Pane/Form top *******************/
        Label mainFormTitle = new Label("Inventory Management System");

        // Assign id for css styling
        mainFormTitle.setId("mainFormTitle");

        /******************* Main Pane/Form middle *******************/
        HBox partsAndProductsPane = new HBox();
        VBox partsPane = new VBox();    // left
        VBox productsPane = new VBox(); // right

        // Assign css class for styling
        partsPane.getStyleClass().add("parts-products-pane");
        productsPane.getStyleClass().add("parts-products-pane");
        partsAndProductsPane.getStyleClass().add("parts-products-parent-pane");

        // Add parts and products panes to parent wrapper pane
        partsAndProductsPane.getChildren().addAll(partsPane, productsPane);

        /*******************  Main Pane/Form bottom *******************/

        // Exit button closes the application
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.close();
            }
        });

        // Assign id for css styling
        exitButton.setId("exit-button");

        // Create exit button container pane
        HBox exitButtonPane = new HBox();

        // Assign css class for styling
        exitButtonPane.getStyleClass().add("exit-button-pane");

        // Add exit button to container pane
        exitButtonPane.getChildren().addAll(exitButton);

        /******************* Parts pane layout *******************/

        // Parts pane label and search box container
        HBox partsLabelAndSearchBox = new HBox();        // top
        // table view middle
        HBox crudButtonsPane = new HBox();              // bottom
        partsPane.getChildren().addAll(partsLabelAndSearchBox, partsTable, crudButtonsPane);

        // Parts pane top elements
        FilteredList<Part>filteredPartsList = new FilteredList<>(inventory.getAllParts(), p -> true);
        Label partsPaneTitle = new Label("Parts");
        partsPaneTitle.setId("parts-pane-title");
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by part ID or Name");
        partsSearchBox.setFocusTraversable(false);

        // Auto-complete search event handler
        partsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPartsList.setPredicate(currPart -> {
                // If filter text is empty, display all parts
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Ignore casing
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(currPart.getName()).toLowerCase().startsWith(lowerCaseFilter)) {
                    return true; // Filter matches part name.

                } else if (String.valueOf(currPart.getId()).toLowerCase().startsWith(lowerCaseFilter)) {
                    return true; // Filter matches part id.
                }

                return false; // Does not match.
            });
        });

        // Assign id for css styling
        partsLabelAndSearchBox.setId("parts-label-and-search-box");

        // Add title and search box panes to parent container
        partsLabelAndSearchBox.getChildren().addAll(partsPaneTitle, partsSearchBox);

        // Parts pane middle elements
        partIDHeader = new TableColumn("Part ID");
        partIDHeader.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));

        TableColumn partNameHeader = new TableColumn("Part Name");
        partNameHeader.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        TableColumn inventoryHeader = new TableColumn("Inventory Level");
        inventoryHeader.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        TableColumn priceHeader = new TableColumn("Price/Cost per Unit");
        priceHeader.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Create sorted list based on filtered parts list
        SortedList<Part> sortedData = new SortedList<>(filteredPartsList);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(partsTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        partsTable.setItems(sortedData);

        // Assign css class for styling
        partsTable.getStyleClass().add("parts-and-products-table");

        // Add columns to the parts table
        partsTable.getColumns().addAll(partIDHeader, partNameHeader, inventoryHeader, priceHeader);


        // Parts pane bottom elements

        // Assign css class for styling
        crudButtonsPane.getStyleClass().add("crud-buttons-pane");

        // Add part button and event handler
        Button addPartButton = new Button("Add");
        addPartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // Make sure the add part form is clear and does not hold
                // values from previous states
                resetAddModifyPartForm();

                // Add new part event handler
                saveAddPart.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        Part newPart = createNewPart();

                        // Add new part to our main inventory object
                        inventory.addPart(newPart);

                        // Increase part id
                        ++partsCount;

                        // Redirect to main screen
                        primaryStage.setScene(mainFormScene);
                    }
                });
                // Redirect to the add part form
                primaryStage.setScene(addPartFormScene);
            }
        });

        // Modify part button and event handler
        Button modifyPartButton = new Button("Modify");
        modifyPartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int index = partsTable.getSelectionModel().getSelectedIndex();
                // If no part has been selected, display an error message letting the
                // user know that a part needs to be selected
                if (index == -1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must select a part to modify");
                    errorAlert.showAndWait();
                } else {
                    // Get the current part selected and prefill the "modify part" fields
                    // with the corresponding data
                    Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
                    int selectedIndex = partsTable.getSelectionModel().getSelectedIndex();
                    updateAddModifyPartForm(selectedPart.getId());

                    // Save changes event handler
                    saveAddPart.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            // Update changes made
                            modifyPart(selectedIndex);

                            // Redirect back to the main form
                            primaryStage.setScene(mainFormScene);
                        }
                    });
                    // Redirect to the modify part form
                    primaryStage.setScene(addPartFormScene);
                }
            }
        });

        // Delete part button and event handler
        Button deletePartButton = new Button("Delete");
        deletePartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int index = partsTable.getSelectionModel().getSelectedIndex();

                // If no part has been selected, display an error message letting the
                // user know that a part needs to be selected
                if (index == -1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must select a part to delete");
                    errorAlert.showAndWait();
                } else {
                    // Get the part selected to be deleted
                    Part selected = (Part) partsTable.getSelectionModel().getSelectedItem();

                    // Delete selected part from main inventory
                    inventory.deletePart(selected);
                }
            }
        });

        // Add all buttons to the wrapper container
        crudButtonsPane.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);

        // Main pane config
        VBox mainPane = new VBox();

        // Add css class for styiling
        mainPane.getStyleClass().add("main-pane");

        // Add all wrapper containers to the main pane
        mainPane.getChildren().addAll(mainFormTitle, partsAndProductsPane, exitButtonPane);

        // Add main pane to scene and main scene to the stage
        ((Group) mainFormScene.getRoot()).getChildren().addAll(mainPane);
        primaryStage.setScene(mainFormScene);
        primaryStage.show();
    }

    private void initializeAddPartForm() {
        /******************* addPartFormLayout - top *******************/
        HBox addPartTypePane = new HBox();

        // Add css class and id for styling
        addPartLabel.setId("add-part-main-label");
        addPartLabel.getStyleClass().add("add-part-field-labels");

        // Part type radio buttons group event handler
        ToggleGroup partTypeGroup = new ToggleGroup();
        partTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {

                RadioButton rb = (RadioButton)partTypeGroup.getSelectedToggle();
                if (rb != null) {
                    String selection = rb.getText();
                    // Update add/modify part label based on selection
                    if (selection == "In-House") {
                        partIdOrNameLabel.setText("Machine ID");
                    } else {
                        partIdOrNameLabel.setText("Company Name");
                    }

                }
            }
        });

        // Add id for css styling
        inHouse.setId("add-part-in-house");

        // Assign radio button group
        inHouse.setToggleGroup(partTypeGroup);

        // Set "In-House" selected by default
        inHouse.setSelected(true);

        // Assign radio button group
        outsourced.setToggleGroup(partTypeGroup);

        // Assign css class and id for styling
        addPartTypePane.getStyleClass().add("add-part-field-panes");
        addPartTypePane.setId("add-part-type-pane");

        // Add all components to the wrapper container
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

    // TODO add comments/documentation for functions
    private void updateAddModifyPartForm(int partId) {
        addPartLabel.setText("Modify Part");
        Part selectedPart = inventory.lookupPart(partId);
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

    private Part createNewPart() {
        Part newPart;
        if (inHouse.isSelected()) {
            newPart = new InHouse(
                    partsCount,
                    partNameTextField.getText(),
                    Double.parseDouble(partPriceTextField.getText()),
                    Integer.parseInt(partInventoryTextField.getText()),
                    Integer.parseInt(partMaxTextField.getText()),
                    Integer.parseInt(partMinTextField.getText()),
                    Integer.parseInt(partIdOrNameTextField.getText())
            );
        } else {
            newPart = new Outsourced(
                    partsCount,
                    partNameTextField.getText(),
                    Double.parseDouble(partPriceTextField.getText()),
                    Integer.parseInt(partInventoryTextField.getText()),
                    Integer.parseInt(partMaxTextField.getText()),
                    Integer.parseInt(partMinTextField.getText()),
                    partIdOrNameTextField.getText()
            );

        }
        return newPart;
    }

    private void modifyPart(int selectedIndex) {
        Part modifiedPart = createNewPart();
        inventory.updatePart(selectedIndex, modifiedPart);
        partsTable.getSortOrder().addAll(partIDHeader);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
