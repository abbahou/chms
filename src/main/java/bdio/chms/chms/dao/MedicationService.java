package bdio.chms.chms.dao;

import bdio.chms.chms.models.Medicament;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicationService {

    private static final Logger LOGGER = Logger.getLogger(MedicationService.class.getName());

    // Method to add a new medicament to the database
    public void addMedication(Medicament medicament) {
        String sql = "INSERT INTO medications (id, name, price, description, expiration_time, type, quantite) VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (!isValidDate(medicament.getExpirationTime())) {
            throw new IllegalArgumentException("Invalid date format for expiration time: " + medicament.getExpirationTime());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicament.getId());
            stmt.setString(2, medicament.getName());
            stmt.setDouble(3, medicament.getPrice());
            stmt.setString(4, medicament.getDescription());
            stmt.setDate(5, Date.valueOf(medicament.getExpirationTime())); // Convert to SQL Date
            stmt.setString(6, medicament.getType());
            stmt.setInt(7, 0); // Quantité par défaut à 0

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding medication", e);
        }
    }
    public Medicament getMedicationById(int id) throws SQLException {
        Medicament med = null;
        String query = "SELECT name FROM medications WHERE idmedicament = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    med = new Medicament(
                            resultSet.getString("NomMed")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return med;
    }

    public boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Method to get all medications from the database
    public List<Medicament> getAllMedications() {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medications";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("expiration_time"),
                        rs.getString("type"),
                        rs.getInt("quantite") // Récupération de la quantité
                );
                medicaments.add(medicament);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving medications", e);
        }
        return medicaments;
    }

    // Method to remove a medication by its ID from the database
    public void removeMedication(String id) {
        String sql = "DELETE FROM medications WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing medication", e);
        }
    }

    // Method to update an existing medication in the database
    public void updateMedication(Medicament updatedMedicament) {
        String sql = "UPDATE medications SET name = ?, price = ?, description = ?, expiration_time = ?, type = ?, quantite = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, updatedMedicament.getName());
            stmt.setDouble(2, updatedMedicament.getPrice());
            stmt.setString(3, updatedMedicament.getDescription());
            stmt.setDate(4, Date.valueOf(updatedMedicament.getExpirationTime())); // Convert to SQL Date
            stmt.setString(5, updatedMedicament.getType());
            stmt.setInt(6, updatedMedicament.getQuantite()); // Mise à jour de la quantité
            stmt.setString(7, updatedMedicament.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating medication", e);
        }
    }
    public void updateQuantite(String id, int nouvelleQuantite) {
        String sql = "UPDATE medications SET quantite = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nouvelleQuantite);
            stmt.setString(2, id);
            stmt.executeUpdate();
            System.out.println("Quantité mise à jour avec succès dans la base de données!");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating quantity", e);
        }
    }
}
