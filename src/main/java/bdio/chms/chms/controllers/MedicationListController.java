package bdio.chms.chms.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import bdio.chms.chms.models.Medicament;
import bdio.chms.chms.dao.MedicationService;

public class MedicationListController {

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
    public void initialize() {
        MedicationService medicationService = new MedicationService();
        medicationTable.getScene().getStylesheets().add(getClass().getResource("/bdio/chms/chms/fxml/styles.css").toExternalForm());
        // Set the cell value factory for each column
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        expirationTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expirationTimeProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        // Load medications into the table
        medicationTable.getItems().setAll(medicationService.getAllMedications());
    }
}
