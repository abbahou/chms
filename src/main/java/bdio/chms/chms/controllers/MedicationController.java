package bdio.chms.chms.controllers;

import bdio.chms.chms.models.Medicament;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import bdio.chms.chms.dao.MedicationService;

import java.io.IOException;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class MedicationController {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane mainContent;

    @FXML
    private VBox viewMedicationSection;

    @FXML
    private VBox addMedicationSection;

    @FXML
    private TableView<Medicament> medicationTable;

    @FXML
    private TableColumn<Medicament, String> idColumn;
    @FXML
    private TableColumn<Medicament, String> nameColumn;
    @FXML
    private TableColumn<Medicament, String> descriptionColumn;
    @FXML
    private TableColumn<Medicament, String> expirationTimeColumn;
    @FXML
    private TableColumn<Medicament, Double> priceColumn;
    @FXML
    private TableColumn<Medicament, String> typeColumn;
    @FXML
    private TableColumn<Medicament, Void> actionColumn;

    @FXML
    private Button saveMedicationButton;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField expirationTimeTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField typeTextField;

    @FXML
    private Label formTitleLabel;
    @FXML
    private VBox sidebar;
    public Button closeAddMedicationButton;

    public Medicament editingMedicament;
    public MedicationService medicationService;

    private boolean isAddMedicationVisible = false;

    public MedicationController() {
        this.medicationService = new MedicationService();
    }


    @FXML
    public void loadBlankPage(ActionEvent event) {
        viewMedicationSection.setVisible(false);
        addMedicationSection.setVisible(false);
    }



    @FXML
    public void initialize() {
        // Ensure the properties are used for binding
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty()); // StringProperty
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty()); // StringProperty
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty()); // StringProperty
        expirationTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expirationTimeProperty()); // StringProperty
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject()); // DoubleProperty
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty()); // StringProperty

        addActionButtons();
        refreshMedicationTable();
    }


    public Scene scene;
    public Parent root;
    // Method to load views dynamically into the dashboard content area
    private void loadView(String fxmlFile, javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            root = loader.load();

            // Get the current stage and set the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            // Check if you need to hide/show the sidebar or other persistent components
            // For example, you could manage sidebar visibility based on the current view
            // Make sure the sidebar is visible for dashboard and medication views
            // Hide sidebar for other views (if needed)
            sidebar.setVisible(fxmlFile.equals("/bdio/chms/chms/fxml/Dashboard.fxml") || fxmlFile.equals("/bdio/chms/chms/fxml/Medicament.fxml"));  // Assuming 'sidebar' is your sidebar component

            // Set the scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Profile.fxml", event);
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Dashboard.fxml", event);
    }

    @FXML
    private void showViewMedication(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Medicament.fxml", event);
    }

    @FXML
    private void goToOrdonnance(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Ordonnance.fxml", event);
    }
    @FXML
    private void goToFournisseur(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Fournisseur.fxml", event);
    }

    @FXML
    public void showAddMedication() {
        // Reset to "Add Medicament" mode
        editingMedicament = null;
        formTitleLabel.setText("Add Medicament");
        saveMedicationButton.setText("Save Medicament");
        saveMedicationButton.setOnAction(event -> handleSaveMedication());
        clearFormFields();

        // Show the form
        switchToFormView();
    }

    public void closeAddMedication() {
        if (!isAddMedicationVisible) return;

        isAddMedicationVisible = false; // Reset the visibility flag

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), addMedicationSection);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(fadeEvent -> { // Ensure correct transition
            addMedicationSection.setVisible(false); // Hide the Add/Edit section
            clearFormFields(); // Clear the form fields

            // Ensure the view medication section is visible and on top
            viewMedicationSection.setVisible(true);
            viewMedicationSection.toFront();

            // Reset save button for the next use
            saveMedicationButton.setText("Save Medicament");
            saveMedicationButton.setOnAction(actionEvent -> handleSaveMedication());
        });
        fadeOut.play();
    }

    @FXML
    public void deleteSelectedMedication(Medicament selectedMedicament) {
        if (selectedMedicament == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Medicament");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this medication?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                medicationService.removeMedication(selectedMedicament.getId());
                refreshMedicationTable();
            }
        });
    }

    @FXML
    public void modifySelectedMedication(Medicament selectedMedicament) {
        if (selectedMedicament == null) return;

        // Switch to "Edit Medicament" mode
        editingMedicament = selectedMedicament;
        formTitleLabel.setText("Update Medicament");
        saveMedicationButton.setText("Update Medicament");
        saveMedicationButton.setOnAction(event -> handleUpdateMedication(selectedMedicament));

        // Populate form with the selected medication's data
        idTextField.setText(selectedMedicament.getId());
        idTextField.setEditable(false); // Prevent ID editing
        nameTextField.setText(selectedMedicament.getName());
        descriptionTextField.setText(selectedMedicament.getDescription());
        expirationTimeTextField.setText(selectedMedicament.getExpirationTime());
        priceTextField.setText(String.valueOf(selectedMedicament.getPrice()));
        typeTextField.setText(selectedMedicament.getType());

        // Show the form
        switchToFormView();
    }

    private void switchToFormView() {
        isAddMedicationVisible = true;
        addMedicationSection.setVisible(true);
        viewMedicationSection.setVisible(false);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), addMedicationSection);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    public void handleUpdateMedication(Medicament selectedMedicament) {
        if (validateInputFields()) return;

        // Update selected medication properties
        selectedMedicament.setName(nameTextField.getText());
        selectedMedicament.setDescription(descriptionTextField.getText());
        selectedMedicament.setExpirationTime(expirationTimeTextField.getText());
        selectedMedicament.setPrice(Double.parseDouble(priceTextField.getText()));
        selectedMedicament.setType(typeTextField.getText());

        // Update in service
        medicationService.updateMedication(selectedMedicament);
        refreshMedicationTable();

        // Reset the save button and form fields
        resetSaveButton();
        clearFormFields();
        closeAddMedication(); // Return to  list
    }

    @FXML
    public void handleSaveMedication() {
        // Valider les champs de saisie
        if (validateInputFields()) return;

        try {
            // Générer un ID unique pour le médicament
            String id = UUID.randomUUID().toString();

            // Créer une instance de Medicament avec une quantité par défaut de 0
            Medicament medicament = new Medicament(
                    id,
                    nameTextField.getText(),
                    Double.parseDouble(priceTextField.getText()),
                    descriptionTextField.getText(),
                    expirationTimeTextField.getText(),
                    typeTextField.getText(),
                    0 // Quantité par défaut
            );

            // Ajouter le médicament à la base de données
            medicationService.addMedication(medicament);

            // Rafraîchir la table des médicaments (si applicable)
            refreshMedicationTable();

            // Effacer les champs du formulaire
            clearFormFields();

            // Fermer la fenêtre d'ajout de médicament
            closeAddMedication();

            // Afficher un message de succès

        } catch (Exception e) {
            // Gérer les erreurs (par exemple, erreurs de conversion ou de base de données)
            showErrorMessage("Erreur", "Une erreur s'est produite lors de l'ajout du médicament : " + e.getMessage());
        }
    }


    private void resetSaveButton() {
        // Reset the save button for adding a new medication
        saveMedicationButton.setText("Save Medicament");
        saveMedicationButton.setOnAction(event -> handleSaveMedication());
    }

    private boolean validateInputFields() {
        if (nameTextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty() ||
                expirationTimeTextField.getText().isEmpty() ||
                priceTextField.getText().isEmpty() ||
                typeTextField.getText().isEmpty()) {
            showErrorMessage("Input Error", "All fields must be filled out.");
            return true;
        }
        try {
            double price;
            price = Double.parseDouble(priceTextField.getText());
            if (price <= 0) {
                showErrorMessage("Input Error", "Price must be positive.");
                return true;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Input Error", "Price must be numeric.");
            return true;
        }
        return false;
    }

    private void refreshMedicationTable() {
        medicationTable.getItems().setAll(medicationService.getAllMedications());
    }

    private void clearFormFields() {
        idTextField.clear();
        idTextField.setEditable(true);
        nameTextField.clear();
        descriptionTextField.clear();
        expirationTimeTextField.clear();
        priceTextField.clear();
        typeTextField.clear();
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showViewMedication() {
        addMedicationSection.setVisible(false);
        viewMedicationSection.setVisible(true);
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = createActionButton("#5A9BD5", FontAwesomeSolid.WRENCH, event -> {
                Medicament selectedMedicament = getTableView().getItems().get(getIndex());
                modifySelectedMedication(selectedMedicament);
            });

            private final Button deleteButton = createActionButton("#E57373", FontAwesomeSolid.TRASH, event -> {
                Medicament selectedMedicament = getTableView().getItems().get(getIndex());
                deleteSelectedMedication(selectedMedicament);
            });

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10);
                    buttonBox.getChildren().addAll(editButton, deleteButton);
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private Button createActionButton(String color, FontAwesomeSolid icon, EventHandler<ActionEvent> actionEvent) {
        Button button = new Button("");
        FontIcon iconView = new FontIcon(icon);
        iconView.setIconSize(16);  // Set the icon size

        // Set icon color based on the button color
        if ("#5A9BD5".equals(color)) {
            iconView.setIconColor(Color.web("#F2F2F2"));  // Light Blue for edit (matches theme)
        } else if ("#E57373".equals(color)) {
            iconView.setIconColor(Color.web("#F2F2F2"));  // Light Red for delete (matches theme)
        }

        // Make sure the icon is properly centered in the button
        button.setGraphic(iconView);
        button.setStyle("-fx-background-color:" + color + "; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-alignment: center;");
        button.setOnAction(actionEvent);
        return button;
    }
}
