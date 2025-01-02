package bdio.chms.chms.controllers;

import bdio.chms.chms.models.Medicament;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import bdio.chms.chms.dao.MedicationService;
import javafx.scene.control.Alert;

import java.util.UUID;

public class AddMedicationController {

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
    private TextField quantiteTextField; // Nouveau champ pour la quantité

    private final MedicationService medicationService;

    public AddMedicationController() {
        this.medicationService = new MedicationService();
    }

    @FXML
    public void initialize() {
        // Code d'initialisation, si nécessaire
    }

    @FXML
    public void handleSaveMedication() {
        if (!validateInputFields()) return;

        String id = UUID.randomUUID().toString();
        Medicament medicament = new Medicament(
                id,
                nameTextField.getText(),
                Double.parseDouble(priceTextField.getText()),
                descriptionTextField.getText(),
                expirationTimeTextField.getText(),
                typeTextField.getText(),
                Integer.parseInt(quantiteTextField.getText()) // Gestion de la quantité
        );
        medicationService.addMedication(medicament);
        clearFormFields();
    }

    private boolean validateInputFields() {
        if (nameTextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty() ||
                expirationTimeTextField.getText().isEmpty() ||
                priceTextField.getText().isEmpty() ||
                typeTextField.getText().isEmpty() ||
                quantiteTextField.getText().isEmpty()) { // Vérification du champ quantité
            showErrorMessage("All fields must be filled out.");
            return false;
        }
        try {
            double price = Double.parseDouble(priceTextField.getText());
            if (price <= 0) {
                showErrorMessage("Price must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Price must be numeric.");
            return false;
        }
        try {
            int quantite = Integer.parseInt(quantiteTextField.getText());
            if (quantite < 0) {
                showErrorMessage("Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Quantity must be numeric.");
            return false;
        }
        return true;
    }

    private void clearFormFields() {
        nameTextField.clear();
        descriptionTextField.clear();
        expirationTimeTextField.clear();
        priceTextField.clear();
        typeTextField.clear();
        quantiteTextField.clear(); // Effacer le champ quantité
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
