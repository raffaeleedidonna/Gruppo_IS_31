-- Dati di esempio per il database ecommerce.
--
-- Prerequisito: le tabelle devono gia' esistere, quindi va lanciato dopo il
-- primo avvio dell'applicazione (le crea Hibernate con hbm2ddl.auto=update).
--
-- Uso:
--   mysql -h 192.168.178.103 -u ecommerce -p ecommerce < seed.sql
--
-- Lo script e' idempotente: rilanciarlo non crea duplicati. Categorie,
-- prodotti e ordini hanno id espliciti, l'utente e' identificato dall'email
-- (UNIQUE) e il carrello dal cliente: INSERT IGNORE scarta le righe gia'
-- presenti invece di fallire.

-- ---------------------------------------------------------------------------
-- Utenti
-- ---------------------------------------------------------------------------
-- SHA2(x, 256) produce lo stesso hash esadecimale di DigestUtils.sha256Hex,
-- usato da ControllerUtenti: le password restano quelle in chiaro qui sotto.
--
--   admin@ecommerce.it      -> admin123
--   mario.rossi@example.com -> password

INSERT IGNORE INTO Utente (DTYPE, email, passwordHash, nome, cognome, indirizzo, immagineProfilo) VALUES
  ('Amministratore', 'admin@ecommerce.it', SHA2('admin123', 256), 'Ada', 'Lovelace', 'Via del Software 1, Camerino', NULL),
  ('Cliente', 'mario.rossi@example.com', SHA2('password', 256), 'Mario', 'Rossi', 'Via Roma 42, Macerata', NULL);

-- Ogni Cliente creato da Java ha un Carrello (lo costruisce il costruttore di
-- Cliente), quindi il cliente inserito a mano deve averlo anche lui.
-- L'amministratore invece non ne ha uno.
INSERT IGNORE INTO Carrello (cliente_id)
  SELECT id FROM Utente WHERE email = 'mario.rossi@example.com';

-- ---------------------------------------------------------------------------
-- Categorie
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO Categoria (id, nome) VALUES
  (1, 'Libri'),
  (2, 'Abbigliamento');

-- ---------------------------------------------------------------------------
-- Prodotti
-- ---------------------------------------------------------------------------
-- disponibile / inOfferta sono BIT(1): 1 = true, 0 = false.
-- Il catalogo mostra solo i prodotti con disponibile = 1 (RegistroProdotti),
-- quindi id 7 e 13 servono a verificare che restino nascosti.

-- Libri
INSERT IGNORE INTO Prodotto (id, nome, descrizione, prezzo, quantitaMagazzino, disponibile, inOfferta, categoria_id) VALUES
  (1, 'Refactoring: Improving the Design of Existing Code', 'Martin Fowler, 2a edizione. Il catalogo dei refactoring, dal codice che puzza a quello che si legge.', 49.90, 11, 1, 1, 1),
  (2, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Gamma, Helm, Johnson, Vlissides. La Gang of Four e i suoi 23 pattern.', 54.00, 7, 1, 0, 1),
  (3, 'Patterns of Enterprise Application Architecture', 'Martin Fowler. Active Record, Data Mapper, Unit of Work: i pattern dietro un ORM come Hibernate.', 52.50, 5, 1, 0, 1),
  (4, 'Domain-Driven Design', 'Eric Evans. Come far parlare il modello a oggetti la stessa lingua del dominio.', 57.00, 4, 1, 0, 1),
  (5, 'Clean Code', 'Robert C. Martin. Se il codice va commentato per essere capito, forse va riscritto.', 44.90, 13, 1, 1, 1),
  (6, 'The Pragmatic Programmer', 'Hunt e Thomas, 20th Anniversary Edition. Il libro dell''anatra di gomma.', 46.00, 9, 1, 0, 1),
  (7, 'The Mythical Man-Month', 'Fred Brooks. Nove donne non fanno un bambino in un mese. Esaurito.', 39.00, 0, 0, 0, 1);

-- Abbigliamento
INSERT IGNORE INTO Prodotto (id, nome, descrizione, prezzo, quantitaMagazzino, disponibile, inOfferta, categoria_id) VALUES
  (8,  'Rick Owens Geobasket', 'Sneaker alta in pelle, suola in gomma bianca. Nero/bianco, taglie 40-46.', 1290.00, 5, 1, 0, 2),
  (9,  'Rick Owens Ramones', 'Sneaker bassa in pelle con punta arrotondata. Nero, taglie 40-46.', 890.00, 7, 1, 1, 2),
  (10, 'Rick Owens Bauhaus Sneaks', 'Sneaker bassa in pelle martellata, linguetta oversize. Milk, taglie 41-45.', 950.00, 4, 1, 0, 2),
  (11, 'Rick Owens Geth Runner', 'Runner in pelle e mesh con suola a carrarmato. Nero, taglie 41-46.', 1150.00, 3, 1, 1, 2),
  (12, 'Rick Owens DRKSHDW Jumbo Lace', 'Sneaker in canvas con laccio jumbo e suola Vibram. Taglie 40-45.', 720.00, 8, 1, 0, 2),
  (13, 'Rick Owens x Veja Runner Style 2', 'Runner in pelle riciclata, collaborazione con Veja. In riassortimento.', 640.00, 0, 0, 0, 2);

-- ---------------------------------------------------------------------------
-- Ordini di Mario Rossi
-- ---------------------------------------------------------------------------
-- Ogni ordine copia l'indirizzo del cliente al momento dell'acquisto
-- (lo fa il costruttore di Ordine) e ogni riga copia il prezzo del prodotto
-- in prezzoDiAcquisto: qui i prezzi coincidono con quelli a listino.
-- Le quantita' ordinate sono gia' scalate da quantitaMagazzino qui sopra,
-- come farebbe Ordine.scaricaMagazzino().

INSERT IGNORE INTO Ordine (id, dataCreazione, indirizzoSpedizione, stato, totaleComplessivo, cliente_id)
  SELECT 1, '2026-06-12 10:32:15', 'Via Roma 42, Macerata', 'CONSEGNATO', 103.90, id
  FROM Utente WHERE email = 'mario.rossi@example.com';

INSERT IGNORE INTO Ordine (id, dataCreazione, indirizzoSpedizione, stato, totaleComplessivo, cliente_id)
  SELECT 2, '2026-07-02 18:04:51', 'Via Roma 42, Macerata', 'SPEDITO', 1379.80, id
  FROM Utente WHERE email = 'mario.rossi@example.com';

INSERT IGNORE INTO Ordine (id, dataCreazione, indirizzoSpedizione, stato, totaleComplessivo, cliente_id)
  SELECT 3, '2026-07-13 09:15:00', 'Via Roma 42, Macerata', 'INSERITO', 936.00, id
  FROM Utente WHERE email = 'mario.rossi@example.com';

-- Ordine 1: Refactoring + Design Patterns = 49.90 + 54.00 = 103.90
INSERT IGNORE INTO RigaOrdine (id, quantitaAcquistata, prezzoDiAcquisto, prodotto_id, ordine_id) VALUES
  (1, 1, 49.90, 1, 1),
  (2, 1, 54.00, 2, 1);

-- Ordine 2: Geobasket + 2x Clean Code = 1290.00 + 89.80 = 1379.80
INSERT IGNORE INTO RigaOrdine (id, quantitaAcquistata, prezzoDiAcquisto, prodotto_id, ordine_id) VALUES
  (3, 1, 1290.00, 8, 2),
  (4, 2, 44.90, 5, 2);

-- Ordine 3: Ramones + The Pragmatic Programmer = 890.00 + 46.00 = 936.00
INSERT IGNORE INTO RigaOrdine (id, quantitaAcquistata, prezzoDiAcquisto, prodotto_id, ordine_id) VALUES
  (5, 1, 890.00, 9, 3),
  (6, 1, 46.00, 6, 3);
