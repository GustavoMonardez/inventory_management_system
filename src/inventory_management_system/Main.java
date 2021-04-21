package inventory_management_system;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 The Main class is the main application
 FUTURE ENHANCEMENT Upon loading the application if a parts
 inventory is less than the minimum, a message shows up letting
 the user know, and suggesting to order/make more parts
 */
public class Main extends Application {
    // Use to assign part id to newly created parts
    private int partsCount = 0;

    // Use to assign product id to newly created products
    private int productsCount = 0;

    // Table that will display all parts available
    private TableView<Part> partsTable = new TableView();

    // Table that will display all parts available
    private TableView<Product> productsTable = new TableView();

    // Column that data will be sorted on
    private TableColumn partIDHeader;
    private TableColumn productIDHeader;

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

    /******************* Add Product Form *******************/
    private TextField productIdTextField = new TextField();
    private TextField productNameTextField = new TextField();
    private TextField productInventoryTextField = new TextField();
    private TextField productPriceTextField = new TextField();
    private TextField productMaxTextField = new TextField();
    private TextField productMinTextField = new TextField();
    private VBox addProductFormLayout = new VBox();
    private Button cancelAddProduct = new Button("Cancel");
    private Scene addProductFormScene = new Scene(addProductFormLayout);


    /**
     *  The  start  method begins the execution of the thread
     * @param primaryStage The main stage of the application
     *  RUNTIME ERROR Exception in thread "JavaFX Application Thread" java.lang.NumberFormatException
     *  This error was caused when passing a "string" parameter when a "double" was expected.
     *  The problem was corrected by passing the parameters in the correct order.
     */
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

        /******************* Create dummy product data to display *******************/
        inventory.addProduct(new Product(productsCount,"Giant Bike", 299.99, 3, 1, 12));
        ++productsCount;
        inventory.addProduct(new Product(productsCount,"Tricycle", 99.99, 5, 1, 12));
        ++productsCount;

        // Initialize Add/Modify form
        initializeAddPartForm();
        initializeAddProductForm();
        cancelAddPart.setOnAction(e -> primaryStage.setScene(mainFormScene));
        cancelAddProduct.setOnAction(e -> primaryStage.setScene(mainFormScene));

        // Apply external css styling to forms and its elements
        mainFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
        addPartFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
        addProductFormScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());

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
        //FilteredList<Part>filteredPartsList = new FilteredList<>(inventory.getAllParts(), p -> true);
        Label partsPaneTitle = new Label("Parts");
        partsPaneTitle.setId("parts-pane-title");
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by part ID or Name");
        partsSearchBox.setFocusTraversable(false);

        // Auto-complete search event handler
        partsSearchBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String searchInput = partsSearchBox.getText().toLowerCase();
                    if (searchInput.length() == 0) {
                        partsTable.setItems(inventory.getAllParts());
                    } else if (searchInput.matches("^[0-9].*$")) {
                        for (int i = 0; i < partsTable.getItems().size(); i++) {
                            if (partsTable.getItems().get(i).getId() == Integer.parseInt(searchInput)) {
                                partsTable.requestFocus();
                                partsTable.getSelectionModel().select(i);
                                partsTable.getFocusModel().focus(0);
                            }
                        }
                    } else {
                        System.out.println("starts wit letters");
                        partsTable.setItems(inventory.lookupPart(searchInput));
                    }

                }
            }
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


        partsTable.setItems(inventory.getAllParts());

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

                        if (newPart != null) {
                            // Add new part to our main inventory object
                            inventory.addPart(newPart);

                            // Increase part id
                            ++partsCount;

                            // Redirect to main screen
                            primaryStage.setScene(mainFormScene);
                        }
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
                            Part modifiedPart = createNewPart();

                            if (modifiedPart != null) {
                                inventory.updatePart(selectedIndex, modifiedPart);
                                partsTable.getSortOrder().addAll(partIDHeader);
                                // Redirect back to the main form
                                primaryStage.setScene(mainFormScene);
                            }
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

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm part deletion");
                    String deleteMsg = "You are about to delete the '" + selected.getName() + "' part";
                    alert.setHeaderText(deleteMsg);
                    alert.setContentText("Are you sure you want to delete the selected part?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        // Delete selected part from main inventory
                        inventory.deletePart(selected);
                    } else {
                        // ... user chose CANCEL or closed the dialog
                    }

                }
            }
        });

        // Add all buttons to the wrapper container
        crudButtonsPane.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);

        /******************* Products pane layout *******************/
        // Parts pane label and search box container
        HBox productsLabelAndSearchBox = new HBox();        // top
        // table view middle
        HBox crudButtonsPane2 = new HBox();              // bottom
        productsPane.getChildren().addAll(productsLabelAndSearchBox, productsTable, crudButtonsPane2);

        // Products pane top elements
        Label productsPaneTitle = new Label("Products");
        productsPaneTitle.setId("products-pane-title");
        TextField productsSearchBox = new TextField();
        productsSearchBox.setPromptText("Search by product ID or Name");
        productsSearchBox.setFocusTraversable(false);

        // Auto-complete search event handler
        productsSearchBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String searchInput = productsSearchBox.getText().toLowerCase();
                    if (searchInput.length() == 0) {
                        productsTable.setItems(inventory.getAllProducts());
                    } else if (searchInput.matches("^[0-9].*$")) {
                        for (int i = 0; i < productsTable.getItems().size(); i++) {
                            if (productsTable.getItems().get(i).getId() == Integer.parseInt(searchInput)) {
                                productsTable.requestFocus();
                                productsTable.getSelectionModel().select(i);
                                productsTable.getFocusModel().focus(0);
                            }
                        }
                    } else {
                        System.out.println("starts wit letters");
                        productsTable.setItems(inventory.lookupProduct(searchInput));
                    }

                }
            }
        });

        // Assign id for css styling
        productsLabelAndSearchBox.setId("products-label-and-search-box");

        // Add title and search box panes to parent container
        productsLabelAndSearchBox.getChildren().addAll(productsPaneTitle, productsSearchBox);

        // Products pane middle elements
        productIDHeader = new TableColumn("Product ID");
        productIDHeader.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));

        TableColumn productNameHeader = new TableColumn("Product Name");
        productNameHeader.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        TableColumn inventoryProdHeader = new TableColumn("Inventory Level");
        inventoryProdHeader.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));

        TableColumn priceProdHeader = new TableColumn("Price/Cost per Unit");
        priceProdHeader.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));


        productsTable.setItems(inventory.getAllProducts());
        System.out.println("Printing prods");
        for (Product p : inventory.getAllProducts()) {
            System.out.println(p.getId());
        }
        // Assign css class for styling
        productsTable.getStyleClass().add("parts-and-products-table");

        // Add columns to the parts table
        productsTable.getColumns().addAll(productIDHeader, productNameHeader, inventoryProdHeader, priceProdHeader);


        // Products pane bottom elements

        // Assign css class for styling
        crudButtonsPane2.getStyleClass().add("crud-buttons-pane");

        // Add part button and event handler
        Button addProductButton = new Button("Add");
        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // Make sure the add part form is clear and does not hold
                // values from previous states
                //resetAddModifyPartForm();
                // Add new part event handler
//                saveAddProduct.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override public void handle(ActionEvent e) {
//                        Product newProduct = createNewPart();
//
//                        if (newPart != null) {
//                            // Add new part to our main inventory object
//                            inventory.addPart(newPart);
//
//                            // Increase part id
//                            ++partsCount;
//
//                            // Redirect to main screen
//                            primaryStage.setScene(mainFormScene);
//                        }
//                    }
//                });
                // Redirect to the add part form
                primaryStage.setScene(addProductFormScene);
            }
        });


        // Modify product button and event handler
        Button modifyProductButton = new Button("Modify");
        modifyProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int index = productsTable.getSelectionModel().getSelectedIndex();
                // If no product has been selected, display an error message letting the
                // user know that a part needs to be selected
                if (index == -1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must select a product to modify");
                    errorAlert.showAndWait();
                } else {
                    // Get the current product selected and prefill the "modify part" fields
                    // with the corresponding data
                    Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
                    int selectedIndex = productsTable.getSelectionModel().getSelectedIndex();
                    //updateAddModifyPartForm(selectedProduct.getId());

                    // Save changes event handler
//                    saveAddPart.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override public void handle(ActionEvent e) {
//                            // Update changes made
//                            Part modifiedPart = createNewPart();
//
//                            if (modifiedPart != null) {
//                                inventory.updatePart(selectedIndex, modifiedPart);
//                                partsTable.getSortOrder().addAll(partIDHeader);
//                                // Redirect back to the main form
//                                primaryStage.setScene(mainFormScene);
//                            }
//                        }
//                    });
                    // Redirect to the modify part form
                    primaryStage.setScene(addProductFormScene);
                }
            }
        });

        // Delete product button and event handler
        Button deleteProductButton = new Button("Delete");

        deleteProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int index = productsTable.getSelectionModel().getSelectedIndex();

                // If no product has been selected, display an error message letting the
                // user know that a part needs to be selected
                if (index == -1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must select a product to delete");
                    errorAlert.showAndWait();
                } else {
                    // Get the product selected to be deleted
                    Product selected = (Product) productsTable.getSelectionModel().getSelectedItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm product deletion");
                    String deleteMsg = "You are about to delete the '" + selected.getName() + "' product";
                    alert.setHeaderText(deleteMsg);
                    alert.setContentText("Are you sure you want to delete the selected product?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        // Delete selected product from main inventory
                        inventory.deleteProduct(selected);
                    } else {
                        // ... user chose CANCEL or closed the dialog
                    }

                }
            }
        });

        // Add all buttons to the wrapper container
        crudButtonsPane2.getChildren().addAll(addProductButton, modifyProductButton, deleteProductButton);













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
        initializeAddPartFormTextFieldsValidation();
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

    private void initializeAddProductForm() {
        // id
        Label productIdLabel = new Label("ID");
        productIdLabel.getStyleClass().add("add-part-field-labels");
        productIdTextField.setPromptText("Auto Gen - Disabled");
        productIdTextField.setDisable(true);
        HBox productIdPane = new HBox();
        productIdPane.getStyleClass().add("add-part-field-panes");
        productIdPane.getChildren().addAll(productIdLabel, productIdTextField);

        // Name
        Label productNameLabel = new Label("Name");
        productNameLabel.getStyleClass().add("add-part-field-labels");
        HBox productNamePane = new HBox();
        productNamePane.getStyleClass().add("add-part-field-panes");
        productNamePane.getChildren().addAll(productNameLabel, productNameTextField);

        // Inventory
        Label productInventoryLabel = new Label("Inv");
        productInventoryLabel.getStyleClass().add("add-part-field-labels");
        HBox productInventoryPane = new HBox();
        productInventoryPane.getStyleClass().add("add-part-field-panes");
        productInventoryPane.getChildren().addAll(productInventoryLabel, productInventoryTextField);

        // Price/Cost
        Label productPriceLabel = new Label("Price/Cost");
        productPriceLabel.getStyleClass().add("add-part-field-labels");
        HBox productPricePane = new HBox();
        productPricePane.getStyleClass().add("add-part-field-panes");
        productPricePane.getChildren().addAll(productPriceLabel, productPriceTextField);

        // Max and Min
        Label productMaxLabel = new Label("Max");
        productMaxLabel.getStyleClass().add("add-part-field-labels");
        Label productMinLabel = new Label("Min");
        productMinLabel.setId("add-part-min-label");
        HBox productMaxAndMinPane = new HBox();
        productMaxAndMinPane.getStyleClass().add("add-part-field-panes");
        productMaxAndMinPane.getChildren().addAll(productMaxLabel, productMaxTextField, productMinLabel,
                productMinTextField);

        VBox addProductFieldsPane = new VBox();

        addProductFieldsPane.getChildren().addAll(productIdPane,productNamePane, productInventoryPane, productPricePane,
                productMaxAndMinPane);

        /******************* addProductFormLayout - bottom *******************/
        HBox saveAndCancelProdButtonsPane = new HBox();
        saveAndCancelProdButtonsPane.setId("add-product-save-cancel-buttons-pane");
        saveAndCancelProdButtonsPane.getChildren().addAll(cancelAddProduct);

        addProductFormLayout.getStyleClass().add("add-form-main-pane");
        addProductFormLayout.getChildren().addAll(addProductFieldsPane, saveAndCancelProdButtonsPane);
    }

    private void initializeAddPartFormTextFieldsValidation() {
        partInventoryTextField.setText("");
        partInventoryTextField.setPromptText("Please enter a number");
        partInventoryTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    partInventoryTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        partPriceTextField.setText("");
        partPriceTextField.setPromptText("Please enter a number");
        partPriceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    partPriceTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        partMaxTextField.setText("");
        partMaxTextField.setPromptText("Please enter a number");
        partMaxTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    partMaxTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        partMinTextField.setText("");
        partMinTextField.setPromptText("Please enter a number");
        partMinTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    partMinTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

    /**
     *  The updateAddModifyPartForm function loads the selected
     *  part's data into the modify part form
     *  @param partId Specifies the id of the selected part.
     */
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

    /**
     *  The resetAddModifyPartForm function resets all the
     *  values from the add/modify part form
     */
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

    /**
     *  The createNewPart function creates a new part from the
     *  data entered into the add part form
     *  @return The newly created part
     */
    private Part createNewPart() {
        boolean allFieldsValid = true;
        boolean numeric = true;
        boolean numericInv = true;
        boolean numericMax = true;
        boolean numericMin = true;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Invalid data");

        Part newPart;
        String partName = partNameTextField.getText();
        Double partPrice = -1.0;
        int partInventory = -1;
        int partMax = -1;
        int partMin = -1;

        // part name
        if (partName.length() == 0) {
            alert.setContentText("You must enter a part name");
            alert.showAndWait();
            allFieldsValid = false;
        }

        // inventory
        numericInv = partInventoryTextField.getText().matches("-?\\d+(\\.\\d+)?");
        if(numericInv) partInventory = Integer.parseInt(partInventoryTextField.getText());
        else {
            alert.setContentText("You must enter a number for part inventory");
            alert.showAndWait();
            allFieldsValid = false;
        }

        // price
        numeric = partPriceTextField.getText().matches("-?\\d+(\\.\\d+)?");
        System.out.println("price: " + numeric);
        if(numeric) partPrice = Double.parseDouble(partPriceTextField.getText());
        else {
            alert.setContentText("You must enter a number for part price/cost");
            alert.showAndWait();
            allFieldsValid = false;
        }

        // max
        numericMax = partMaxTextField.getText().matches("-?\\d+(\\.\\d+)?");
        if(numericMax) partMax = Integer.parseInt(partMaxTextField.getText());
        else {
            alert.setContentText("You must enter a number for part max");
            alert.showAndWait();
            allFieldsValid = false;
        }

        // min
        numericMin = partMinTextField.getText().matches("-?\\d+(\\.\\d+)?");
        if(numericMin) partMin = Integer.parseInt(partMinTextField.getText());
        else {
            alert.setContentText("You must enter a number for part min");
            alert.showAndWait();
            allFieldsValid = false;
        }
        // if all values are numbers, check to see if they're in the correct
        // range of values
        if (numericInv && numericMax && numericMin) {
            if (partMin > partMax) {
                alert.setContentText("The min value must be lesser or equal to the max value");
                alert.showAndWait();
                allFieldsValid = false;
            }
            // Inventory must be within min and max
            else if (partInventory < partMin || partInventory > partMax) {
                alert.setContentText("The inventory value must be greater or equal to the min value" +
                        " and lesser or equal to the max value");
                alert.showAndWait();
                allFieldsValid = false;
            }
        }
        int partMachineId = -1;
        if (inHouse.isSelected()) {
            numeric = partIdOrNameTextField.getText().matches("-?\\d+(\\.\\d+)?");
            if(numeric) partMachineId = Integer.parseInt(partInventoryTextField.getText());
            else {
                alert.setContentText("You must enter a number for Machine ID");
                alert.showAndWait();
                allFieldsValid = false;
            }
        } else {
            if (partIdOrNameTextField.getText().length() == 0) {
                alert.setContentText("You must enter a Company Name");
                alert.showAndWait();
                allFieldsValid = false;
            }
        }
        if (allFieldsValid) {
            if (inHouse.isSelected()) {
                newPart = new InHouse(
                        partsCount,
                        partName,
                        partPrice,
                        partInventory,
                        partMax,
                        partMin,
                        partMachineId
                );
            } else {
                newPart = new Outsourced(
                        partsCount,
                        partName,
                        partPrice,
                        partInventory,
                        partMax,
                        partMin,
                        partIdOrNameTextField.getText()
                );

            }
            return newPart;
        } else {

            return null;
        }
    }

    /**
     *  The modifyPart function captures all the data from
     *  the modify part form, creates a temporary part variable
     *  and it passes that information with the index to the
     *  inventory object to update changes of the selected part
     * @param selectedIndex Specifies the index of the
     *                      part to modify
     */
    private void modifyPart(int selectedIndex) {
        Part modifiedPart = createNewPart();
        System.out.println(modifiedPart);
        inventory.updatePart(selectedIndex, modifiedPart);
        partsTable.getSortOrder().addAll(partIDHeader);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
