-- Suppression des tables si elles existent déjà
DROP TABLE IF EXISTS Mesure;
DROP TABLE IF EXISTS Capteur;

-- Création de la table Capteur
CREATE TABLE Capteur (
                         codeUnique VARCHAR(255) NOT NULL PRIMARY KEY,
                         coordonneesGPS VARCHAR(255)
);

-- Création de la table Mesure
CREATE TABLE Mesure (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        codeUnique VARCHAR(255),
                        dateHeure DATETIME,
                        temperature DECIMAL(5, 2),
                        humidite DECIMAL(5, 2) CHECK (humidite >= 0 AND humidite <= 100),
                        FOREIGN KEY (codeUnique) REFERENCES Capteur(codeUnique)
);

-- Insérer des données dans la table Capteur
INSERT INTO Capteur (codeUnique, coordonneesGPS)
VALUES
    ('Capteur1', '48.8566, 2.3522'),
    ('Capteur2', '43.6043, 1.4437'),
    ('Capteur3', '45.7640, 4.8357'),
    ('Capteur4', '44.8378, -0.5792'),
    ('Capteur5', '43.2965, 5.3698');

-- Insérer des mesures dans la table Mesure avec des valeurs plus aléatoires
INSERT INTO Mesure (codeUnique, dateHeure, temperature, humidite)
VALUES
    ('Capteur1', '2023-03-01 10:30:00', 15.2, 45.7),
    ('Capteur1', '2023-03-01 14:45:00', 17.8, 50.3),
    ('Capteur2', '2023-03-01 09:15:00', 20.5, 67.8),
    ('Capteur2', '2023-03-01 12:30:00', 22.1, 60.4),
    ('Capteur2', '2023-03-01 17:20:00', 19.7, 72.1),
    ('Capteur3', '2023-03-02 16:30:00', 18.3, 55.5),
    ('Capteur3', '2023-03-02 11:30:00', 21.6, 58.7),
    ('Capteur4', '2023-03-02 07:30:00', 16.4, 85.2),
    ('Capteur4', '2023-03-02 13:00:00', 23.8, 78.9),
    ('Capteur4', '2023-03-02 18:45:00', 14.9, 89.3),
    ('Capteur5', '2023-03-03 06:45:00', 12.7, 65.4),
    ('Capteur5', '2023-03-03 12:30:00', 26.5, 49.8),
    ('Capteur5', '2023-03-03 18:00:00', 20.3, 57.6);


-- Afficher les données des tables Capteur et Mesure
SELECT * FROM Capteur;
SELECT * FROM Mesure;
SELECT * FROM Mesure WHERE codeUnique = 'Capteur1';