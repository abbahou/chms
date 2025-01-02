# Pharmacy Management System

This project is a Pharmacy Management System built using Java and JavaFX. It allows users to manage medications, including adding, modifying, and deleting medication records.

## Prerequisites

- Java 21
- Maven
- MySQL


## Database Setup



```sql
DROP TABLE IF EXISTS `medicament`;

CREATE TABLE `medicament` (
  `IDMedicament` int NOT NULL,
  `NomMed` varchar(50) DEFAULT NULL,
  `DescMed` varchar(255) DEFAULT NULL,
  `Prix_Unit` double DEFAULT NULL,
  `DateExpiration` datetime DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Qty` int DEFAULT NULL,
  PRIMARY KEY (`IDMedicament`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```