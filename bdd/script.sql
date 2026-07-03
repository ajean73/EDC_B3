CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS historique_fidelite CASCADE;
DROP TABLE IF EXISTS rendez_vous CASCADE;
DROP TABLE IF EXISTS client CASCADE;

CREATE TABLE Client (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(10),
    points_fidelite INT DEFAULT 0 NOT NULL
);

CREATE TABLE Rendez_vous (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL,
    date_rendez_vous TIMESTAMP NOT NULL,
    service VARCHAR(100) NOT NULL,
    statut VARCHAR(20) DEFAULT 'PLANIFIE' NOT NULL, -- 'PLANIFIE', 'HONORE', 'ANNULE'
    CONSTRAINT fk_rendez_vous_client FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE historique_fidelite (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL,
    points_ajoutes INT NOT NULL,
    date_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description VARCHAR(255),
    CONSTRAINT fk_historique_client FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);
