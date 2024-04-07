DELIMITER //

CREATE PROCEDURE GetSensorStats(
    IN sensor_code VARCHAR(255),
    IN time_window VARCHAR(10)
)
BEGIN
    DECLARE time_diff DATETIME;

    -- Calcul de la différence de temps en fonction de la fenêtre temporelle
    IF time_window = 'last_hour' THEN
        SET time_diff = NOW() - INTERVAL 1 HOUR;
    ELSEIF time_window = 'last_day' THEN
        SET time_diff = NOW() - INTERVAL 1 DAY;
ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid time window. Use "last_hour" or "last_day".';
END IF;

    -- Calcul de la moyenne de la température et de l'humidité pour le capteur spécifié
SELECT
    AVG(temperature) AS avg_temperature,
    AVG(humidite) AS avg_humidity,
    CASE
        WHEN MAX(temperature) > MIN(temperature) THEN 'hausse'
        WHEN MAX(temperature) < MIN(temperature) THEN 'baisse'
        ELSE 'stable'
        END AS temperature_trend,
    CASE
        WHEN MAX(humidite) > MIN(humidite) THEN 'hausse'
        WHEN MAX(humidite) < MIN(humidite) THEN 'baisse'
        ELSE 'stable'
        END AS humidity_trend
FROM Mesure
WHERE codeUnique = sensor_code AND dateHeure >= time_diff;
END //

DELIMITER ;
