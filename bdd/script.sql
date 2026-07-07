CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS historique_fidelite CASCADE;
DROP TABLE IF EXISTS rendez_vous CASCADE;
DROP TABLE IF EXISTS client CASCADE;

CREATE TABLE client (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    points_fidelite INT DEFAULT 0 NOT NULL
);

CREATE TABLE rendez_vous (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL,
    date_rendez_vous TIMESTAMP NOT NULL,
    service VARCHAR(100) NOT NULL,
    statut VARCHAR(20) DEFAULT 'PLANIFIE' NOT NULL,
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

INSERT INTO client (id, nom, email, telephone, points_fidelite)
VALUES
    ('10000000-0000-0000-0000-000000000001', 'Jean Dupont', 'jean.dupont@shopwise.fr', '0612345678', 20),
    ('10000000-0000-0000-0000-000000000002', 'Marie Martin', 'marie.martin@shopwise.fr', '0698765432', 10);

INSERT INTO rendez_vous (id, client_id, date_rendez_vous, service, statut)
VALUES
    ('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP + INTERVAL '1 day', 'Conseil personnalise', 'PLANIFIE'),
    ('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Atelier entretien', 'HONORE');

INSERT INTO historique_fidelite (id, client_id, points_ajoutes, date_transaction, description)
VALUES
    ('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000002', 10, CURRENT_TIMESTAMP - INTERVAL '2 days', 'Points attribues apres rendez-vous honore');
