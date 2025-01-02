package bdio.chms.chms.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import bdio.chms.chms.models.Medicament;
import bdio.chms.chms.dao.MedicationService;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class FournisseurController {


    @FXML
    private TableView<Medicament> medicamentTable;

    @FXML
    private TableColumn<Medicament, String> nomColumn;

    @FXML
    private TableColumn<Medicament, Integer> qtyColumn;

    @FXML
    private TableColumn<Medicament, Double> prixColumn;

    @FXML

    public Button viewMedicationButton;
    public Button dashboardButton;
    public Button calendarButton;
    public Button settingsButton;
    public Button profileButton;
    public VBox tableContainer;
    private MedicationService medicationService;

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur...");
        medicationService = new MedicationService();  // Initialize the MedicationService

        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        chargerMedicaments();
    }

    private void chargerMedicaments() {
        if (medicamentTable != null) {
            // Fetch the list of medicaments from the service
            ObservableList<Medicament> medicaments = FXCollections.observableArrayList(medicationService.getAllMedications());
            medicamentTable.setItems(medicaments);
        } else {
            System.err.println("medicamentTable is null!");
        }
    }

    // Method to increase quantity
    public void augmenterQuantite(ActionEvent event) {
        Medicament selectedMedicament = medicamentTable.getSelectionModel().getSelectedItem();

        if (selectedMedicament != null) {
            int nouvelleQuantite = selectedMedicament.getQuantite() + 10; // Increase quantity by 10
            medicationService.updateQuantite(selectedMedicament.getId(), nouvelleQuantite); // Update quantity in database

            // Update the quantity in the table and refresh
            selectedMedicament.setQuantite(nouvelleQuantite);
            medicamentTable.refresh();
        }
    }

    @FXML
    private Node sidebar;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Method to load views dynamically into the dashboard content area
    private void loadView(String fxmlFile, ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            root = loader.load();

            // Get the current stage and set the new scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            // Manage sidebar visibility based on the view being loaded
            manageSidebarVisibility(fxmlFile);

            // Set the scene to the stage and show it
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile);
            e.printStackTrace();
        }
    }


    private void manageSidebarVisibility(String fxmlFile) {
        if (sidebar != null) {
            boolean isSidebarVisible = fxmlFile.equals("/bdio/chms/chms/fxml/Dashboard.fxml") ||
                    fxmlFile.equals("/bdio/chms/chms/fxml/Medicament.fxml");
            sidebar.setVisible(isSidebarVisible);
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
    private void  goToLogin(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Login.fxml", event);
    }
}
