DROP DATABASE IF EXISTS eco_incident;
CREATE DATABASE eco_incident CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eco_incident;

CREATE TABLE location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(40) NOT NULL,
    name VARCHAR(160) NOT NULL,
    city VARCHAR(120) NOT NULL
);

CREATE TABLE observation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    location_id BIGINT NOT NULL,
    indicator VARCHAR(80) NOT NULL,
    observed_value DOUBLE NOT NULL,
    limit_value DOUBLE NOT NULL,
    scale_score DOUBLE NOT NULL,
    duration_score DOUBLE NOT NULL,
    observed_date DATE NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE incident (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    observation_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    f1 DOUBLE NOT NULL,
    f2 DOUBLE NOT NULL,
    f3 DOUBLE NOT NULL,
    critical_index DOUBLE NOT NULL,
    FOREIGN KEY (observation_id) REFERENCES observation(id)
);

CREATE TABLE response_measure (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    incident_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    planned_date DATE NOT NULL,
    FOREIGN KEY (incident_id) REFERENCES incident(id)
);

INSERT INTO location (code, name, city)
VALUES
    ('MRA-01', 'Пушкарівські озера', 'Полтава'),
    ('MRB-02', 'Мікрорайон Затишне', 'Полтава');

INSERT INTO observation
(location_id, indicator, observed_value, limit_value, scale_score,
duration_score, observed_date)
VALUES
    (1, 'Нафтопродукти у воді', 2.5, 0.3, 60.0, 30.0, '2026-10-15'),
    (2, 'Концентрація чадного газу (CO)', 15.0, 5.0, 80.0, 50.0, '2026-10-16');

INSERT INTO incident (observation_id, status, f1, f2, f3, critical_index)
VALUES
    (1, 'ПЕРЕВІРЕНО', 60.0, 40.0, 30.0, 45.5),
    (2, 'РЕАГУВАННЯ', 80.0, 70.0, 50.0, 69.0);

INSERT INTO response_measure (incident_id, description, planned_date)
VALUES
    (1, 'Встановлення бонових загороджень та очищення води.', '2026-10-18'),
    (2, 'Залучення підрозділів ДСНС для ліквідації загоряння сміття.', '2026-10-16');