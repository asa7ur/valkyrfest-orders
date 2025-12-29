INSERT IGNORE INTO users (email, password, enabled, first_name, last_name, birth_date, phone)
VALUES ('garik@email.com', '$2a$12$lug0IXfwgn8BYvVetProeODV8c4ZA880jLAWu.DkBZ7rtv.keAonC', true, 'Garik', 'Asatryan',
        '1996-12-30', '666666666'),
       ('extremaydura@email.com', '$2a$12$qWWi4r4PH4ZL52m38Ep0quh7LRppv11eJ50PTSSOCYMBS6oYbCaaO', true, 'Paula',
        'Martín',
        '1995-08-24', '777777777'),
       ('kalerido@email.com', '$2a$12$qWWi4r4PH4ZL52m38Ep0quh7LRppv11eJ50PTSSOCYMBS6oYbCaaO', true, 'Carlos', 'Calero',
        '1997-09-29', '612345678'),
       ('nacho.nachet@email.com', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Nacho',
        'Muñoz',
        '1996-01-27', '798765432'),
       ('bjorn.ironside@email.se', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Bjorn',
        'Ironside', '1990-01-01', '+46 8 555 444'),
       ('elena.rodriguez@email.es', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Elena',
        'Rodríguez', '1995-07-22', '+34 600 000 001'),
       ('pierre.dubois@email.fr', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Pierre',
        'Dubois', '1992-12-05', '+33 6 12 34 56 78'),
       ('maria.rossi@email.it', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Maria', 'Rossi',
        '1994-08-30', '+39 340 123 4567'),
       ('john.smith@email.uk', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'John', 'Smith',
        '1988-02-14', '+44 20 7946 0111'),
       ('sven.svendsen@email.no', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Sven',
        'Svendsen', '2000-10-10', '+47 22 33 44 55'),
       ('olga.petrova@email.ru', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Olga',
        'Petrova', '1997-04-18', '+7 495 777 88 99'),
       ('ahmed.hassan@email.eg', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Ahmed',
        'Hassan', '1993-09-09', '+20 2 2345 6789'),
       ('fatima.zahra@email.ma', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Fatima',
        'Zahra', '1996-06-06', '+212 5 22 33 44 55'),
       ('lucas.silva@email.br', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Lucas', 'Silva',
        '1991-11-11', '+55 11 99999-8888'),
       ('chen.wei@email.cn', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Chen', 'Wei',
        '1989-05-05', '+86 10 6666 8888'),
       ('diego.maradona@email.ar', '$2a$12$gvyvvwNmM7nCPQqm5FMkCeFYqcHHp2O6niVPlLccVXkUT4l1v/Mui', true, 'Diego',
        'González', '1986-10-30', '+54 11 4444 5555');

INSERT IGNORE INTO roles (name)
VALUES ('Admin'),
       ('Manager'),
       ('User');

INSERT IGNORE INTO user_role (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2),
       (4, 3),
       (5, 3),
       (6, 3),
       (7, 3),
       (8, 3),
       (9, 3),
       (10, 3),
       (11, 3),
       (12, 3),
       (13, 3),
       (14, 3),
       (15, 3),
       (16, 3);

INSERT IGNORE INTO ticket_types (name, price, stock_total, stock_available)
VALUES ('Senda del Guerrero (Abono General)', 95.00, 5000, 5000),
       ('Banquete en el Valhalla (Experiencia VIP)', 250.00, 200, 200),
       ('Miércoles - Entrada de Día', 45.00, 1000, 1000),
       ('Jueves - Entrada de Día', 45.00, 1000, 1000),
       ('Viernes - Entrada de Día', 55.00, 1000, 1000),
       ('Sábado - Entrada de Día', 60.00, 1000, 1000);

INSERT IGNORE INTO camping_types (name, price, stock_total, stock_available)
VALUES ('Sombra de Yggdrasil (Camping Sombra)', 30.00, 500, 500),
       ('Morada de Freya (Glamping de Lujo)', 150.00, 50, 50),
       ('Drakkar sobre Ruedas (Zona Caravanas)', 80.00, 100, 100);


INSERT INTO sponsors (name, phone, email, contribution, image)
VALUES ('Cruzcampo', '+34 954 979 000', 'sponsorship@heinekenespana.es', 60000.00, 'cruzcampo_logo.png'),
       ('Thomann Music', '+49 9546 9223', 'export@thomann.de', 25000.00, 'thomann_logo.png'),
       ('EMP Mailorder', '+34 911 875 528', 'marketing@emp-online.es', 20000.00, 'emp_logo.png'),
       ('Monster Energy', '+1 800 426 737', 'info@monsterenergy.com', 45000.00, 'monster_logo.png'),
       ('Jägermeister', '+49 5331 810', 'espana@jaegermeister.de', 30000.00, 'jaegermeister_logo.png'),
       ('Marshall Amplification', '+44 1908 375411', 'sponsorship@marshall.com', 15000.00, 'marshall_logo.png'),
       ('Jack Daniels', '+1 888 551 5225', 'events@jackdaniels.com', 28000.00, 'jackdaniels_logo.png'),
       ('Estrella Galicia', '+34 981 901 906', 'patrocinios@estrellagalicia.es', 35000.00, 'estrella_galicia_logo.png'),
       ('Vans España', '+34 932 203 100', 'marketing@vans.es', 18000.00, 'vans_logo.png'),
       ('Fnac España', '+34 902 100 632', 'comunicacion@fnac.es', 12000.00, 'fnac_logo.png');

INSERT INTO stages (name, capacity)
VALUES ('Asgard del Sur', 15000),
       ('Fenrir del Al-Ándalus', 8000),
       ('Drakkar de Guadalquivir', 5000),
       ('Valhalla de Triana', 10000);

INSERT INTO sponsor_stage (sponsor_id, stage_id)
VALUES (1, 1),
       (2, 2),
       (3, 4),
       (4, 1),
       (6, 2),
       (8, 3);

INSERT IGNORE INTO artists (name, phone, email, genre, country)
VALUES ('Gojira', '+33 1 42 67 00 00', 'contact@gojira-music.com', 'Progressive Death Metal', 'France'),
       ('Loathe', '+44 20 7946 0000', 'info@loathe.uk', 'Metalcore', 'UK'),
       ('Rammstein', '+49 30 2091 0000', 'management@rammstein.de', 'Industrial Metal', 'Germany'),
       ('Faetooth', '+1 213 555 0198', 'faetoothband@gmail.com', 'Doom Metal', 'USA'),
       ('Deftones', '+1 916 555 0123', 'contact@deftones.com', 'Alternative Metal', 'USA'),
       ('Behemoth', '+48 22 654 00 00', 'behemoth.mgmt@new-east.pl', 'Blackened Death Metal', 'Poland'),
       ('Fit For An Autopsy', '+1 201 555 0144', 'ffaa@metal-management.com', 'Deathcore', 'USA'),
       ('Lorna Shore', '+1 732 555 0166', 'lornashore@touring.com', 'Deathcore', 'USA'),
       ('Opeth', '+46 8 123 456 00', 'management@opeth.com', 'Progressive Metal', 'Sweden'),
       ('Jinjer', '+380 44 234 5678', 'jinjer.booking@gmail.com', 'Progressive Metal', 'Ukraine'),
       ('Metallica', '+1 415 555 0101', 'hq@metallica.com', 'Thrash Metal', 'USA'),
       ('Mastodon', '+1 404 555 0188', 'info@mastodonrocks.com', 'Sludge Metal', 'USA'),
       ('Evanescence', '+1 501 555 0155', 'mgmt@evanescence.com', 'Alternative Metal', 'USA'),
       ('Tool', '+1 310 555 0199', 'toolband@management.com', 'Progressive Metal', 'USA'),
       ('Whitechapel', '+1 865 555 0177', 'whitechapel@metalblade.com', 'Deathcore', 'USA'),
       ('VOLA', '+45 31 12 34 56', 'contact@volaband.com', 'Progressive Metal', 'Denmark'),
       ('Deafheaven', '+1 415 555 0122', 'info@deafheaven.com', 'Blackgaze', 'USA'),
       ('Judas Priest', '+44 20 7946 0555', 'mgmt@judaspriest.com', 'Heavy Metal', 'UK'),
       ('Pantera', '+1 817 555 0199', 'info@pantera.com', 'Groove Metal', 'USA'),
       ('Danheim', '+45 70 10 20 30', 'contact@danheimmusic.com', 'Neofolk', 'Denmark'),
       ('Wardruna', '+47 55 12 34 56', 'info@wardruna.com', 'Nordic Folk', 'Norway'),
       ('Allt', '+46 8 505 123 00', 'alltband@gmail.com', 'Progressive Metalcore', 'Sweden'),
       ('Soen', '+46 70 123 45 67', 'management@soenmusic.com', 'Progressive Metal', 'Sweden'),
       ('Igorrr', '+33 1 40 20 50 50', 'igorrr.contact@gmail.com', 'Avant-garde Metal', 'France'),
       ('Devin Townsend', '+1 604 555 0192', 'info@hevydevy.com', 'Progressive Metal', 'Canada'),
       ('Linkin Park', '+1 310 555 0110', 'contact@linkinpark.com', 'Nu Metal', 'USA'),
       ('Imperial Triumphant', '+1 718 555 0144', 'it@gileadmedia.net', 'Avant-garde Black Metal', 'USA'),
       ('Slipknot', '+1 515 555 0166', 'mgmt@slipknot1.com', 'Nu Metal', 'USA'),
       ('Alice in Chains', '+1 206 555 0188', 'info@aliceinchains.com', 'Grunge', 'USA'),
       ('Sepultura', '+55 11 3060 0000', 'contact@sepultura.com.br', 'Groove Metal', 'Brazil'),
       ('Periphery', '+1 301 555 0122', 'periphery.mgmt@gmail.com', 'Progressive Metal', 'USA'),
       ('Spiritbox', '+1 250 555 0133', 'spiritbox@palechord.com', 'Progressive Metalcore', 'Canada'),
       ('Amira Elfeky', '+1 213 555 0177', 'amira.elfeky@mgmt.com', 'Nu-gaze', 'USA'),
       ('HEALTH', '+1 323 555 0144', 'health@youwillloveeachother.com', 'Industrial Rock', 'USA'),
       ('Bad Omens', '+1 804 555 0155', 'badomens@sumerianrecords.com', 'Alternative Metal', 'USA'),
       ('Jutes', '+1 416 555 0199', 'jutes.mgmt@gmail.com', 'Alt Rock', 'Canada'),
       ('In Mourning', '+46 23 123 45 00', 'info@inmourning.net', 'Progressive Melodic Death Metal', 'Sweden'),
       ('Alcest', '+33 4 91 12 34 56', 'alcest.band@gmail.com', 'Blackgaze', 'France'),
       ('Acid Bath', '+1 504 555 0122', 'acidbath@rotten.com', 'Sludge Metal', 'USA'),
       ('Meshuggah', '+46 90 123 45 67', 'meshuggah@management.se', 'Extreme Progressive Metal', 'Sweden'),
       ('Derby Motoreta\'s Burrito Kachimba', '+34 954 12 34 56', 'kinkidelia@derbymotoretas.com', 'Psychedelic Rock',
        'Spain'),
       ('Limp Bizkit', '+1 904 555 0100', 'bizkit.mgmt@gmail.com', 'Nu Metal', 'USA'),
       ('Slaughter to Prevail', '+7 495 123 45 67', 'alex.terrible@stp.com', 'Deathcore', 'Russia'),
       ('Leprous', '+47 35 55 66 77', 'management@leprous.net', 'Progressive Rock', 'Norway'),
       ('Poppy', '+1 617 555 0188', 'poppy@sumerianrecords.com', 'Avant-pop / Metal', 'USA'),
       ('Lamb of God', '+1 804 555 0111', 'log@epicrecords.com', 'Groove Metal', 'USA'),
       ('Knocked Loose', '+1 502 555 0122', 'knockedloose@purenoise.net', 'Hardcore Punk', 'USA'),
       ('Bambie Thug', '+353 1 800 1234', 'bambie@ouijapop.com', 'Ouija-pop', 'Ireland'),
       ('Ghost', '+46 58 123 45 67', 'tobias.forge@lomavista.com', 'Occult Rock', 'Sweden'),
       ('Batushka', '+48 85 123 45 67', 'batushkaband@gmail.com', 'Black Metal', 'Poland'),
       ('Bring Me The Horizon', '+44 114 555 0199', 'bmth@rawpower-mgmt.com', 'Alternative Metal', 'UK'),
       ('Three Days Grace', '+1 416 555 0177', 'info@threedaysgrace.com', 'Alternative Rock', 'Canada'),
       ('Iron Maiden', '+44 20 8946 0000', 'phantom@ironmaiden.com', 'Heavy Metal', 'UK');

-- MIÉRCOLES 13 DE AGOSTO
-- Escenario: Asgard del Sur
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-13 19:00:00', '2025-08-13 20:00:00', 52, 1), -- Three Days Grace
       ('2025-08-13 20:30:00', '2025-08-13 21:45:00', 13, 1), -- Evanescence
       ('2025-08-13 22:30:00', '2025-08-14 00:30:00', 11, 1), -- Metallica (Headliner)
       ('2025-08-14 01:15:00', '2025-08-14 02:30:00', 42, 1);
-- Limp Bizkit

-- Escenario: Valhalla de Triana
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-13 19:30:00', '2025-08-13 20:30:00', 44, 4), -- Leprous
       ('2025-08-13 21:00:00', '2025-08-13 22:15:00', 23, 4), -- Soen
       ('2025-08-13 23:00:00', '2025-08-14 00:30:00', 14, 4), -- Tool
       ('2025-08-14 01:00:00', '2025-08-14 02:15:00', 24, 4);
-- Igorrr

-- Escenario: Fenrir del al-andalus
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-13 20:00:00', '2025-08-13 21:00:00', 43, 2), -- Slaughter to Prevail
       ('2025-08-13 21:30:00', '2025-08-13 22:45:00', 8, 2),  -- Lorna Shore
       ('2025-08-13 23:30:00', '2025-08-14 01:00:00', 6, 2),  -- Behemoth
       ('2025-08-14 01:30:00', '2025-08-14 02:45:00', 50, 2);
-- Batushka

-- JUEVES 14 DE AGOSTO
-- Escenario: Asgard del Sur
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-14 19:00:00', '2025-08-14 20:00:00', 19, 1), -- Pantera
       ('2025-08-14 20:30:00', '2025-08-14 21:45:00', 18, 1), -- Judas Priest
       ('2025-08-14 22:30:00', '2025-08-15 00:30:00', 53, 1), -- Iron Maiden (Headliner)
       ('2025-08-15 01:15:00', '2025-08-15 02:30:00', 30, 1);
-- Sepultura

-- Escenario: Valhalla de Triana
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-14 19:30:00', '2025-08-14 20:30:00', 12, 4), -- Mastodon
       ('2025-08-14 21:00:00', '2025-08-14 22:15:00', 1, 4),  -- Gojira
       ('2025-08-14 23:00:00', '2025-08-15 00:30:00', 9, 4),  -- Opeth
       ('2025-08-15 01:00:00', '2025-08-15 02:15:00', 40, 4);
-- Meshuggah

-- Escenario: Fenrir del al-andalus
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-14 20:00:00', '2025-08-14 21:00:00', 7, 2),  -- Fit For An Autopsy
       ('2025-08-14 21:30:00', '2025-08-14 22:45:00', 15, 2), -- Whitechapel
       ('2025-08-14 23:30:00', '2025-08-15 01:00:00', 46, 2), -- Lamb of God
       ('2025-08-15 01:30:00', '2025-08-15 02:45:00', 47, 2);
-- Knocked Loose

-- VIERNES 15 DE AGOSTO
-- Escenario: Asgard del Sur
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-15 19:00:00', '2025-08-15 20:00:00', 51, 1), -- Bring Me The Horizon
       ('2025-08-15 20:30:00', '2025-08-15 21:45:00', 49, 1), -- Ghost
       ('2025-08-15 22:30:00', '2025-08-16 00:30:00', 3, 1),  -- Rammstein (Headliner)
       ('2025-08-16 01:15:00', '2025-08-16 02:30:00', 35, 1);
-- Bad Omens

-- Escenario: Valhalla de Triana
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-15 19:30:00', '2025-08-15 20:30:00', 32, 4), -- Spiritbox
       ('2025-08-15 21:00:00', '2025-08-15 22:15:00', 10, 4), -- Jinjer
       ('2025-08-15 23:00:00', '2025-08-16 00:30:00', 5, 4),  -- Deftones
       ('2025-08-16 01:00:00', '2025-08-16 02:15:00', 34, 4);
-- HEALTH

-- Escenario: Drakkar de Guadalquivir
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-15 19:00:00', '2025-08-15 20:00:00', 41, 3), -- Derby Motoreta's Burrito Kachimba
       ('2025-08-15 20:30:00', '2025-08-15 21:30:00', 20, 3), -- Danheim
       ('2025-08-15 22:00:00', '2025-08-15 23:30:00', 21, 3), -- Wardruna
       ('2025-08-16 00:00:00', '2025-08-16 01:15:00', 4, 3);
-- Faetooth

-- SÁBADO 16 DE AGOSTO
-- Escenario: Asgard del Sur
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-16 19:00:00', '2025-08-16 20:15:00', 29, 1), -- Alice in Chains
       ('2025-08-16 21:00:00', '2025-08-16 22:30:00', 28, 1), -- Slipknot
       ('2025-08-16 23:30:00', '2025-08-17 01:30:00', 26, 1);
-- Linkin Park (Headliner)

-- Escenario: Valhalla de Triana
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-16 19:30:00', '2025-08-16 20:30:00', 31, 4), -- Periphery
       ('2025-08-16 21:00:00', '2025-08-16 22:15:00', 16, 4), -- VOLA
       ('2025-08-16 23:00:00', '2025-08-17 00:30:00', 25, 4);
-- Devin Townsend

-- Escenario: Fenrir del al-andalus
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-16 20:00:00', '2025-08-16 21:00:00', 48, 2), -- Bambie Thug
       ('2025-08-16 21:30:00', '2025-08-16 22:30:00', 45, 2), -- Poppy
       ('2025-08-16 23:00:00', '2025-08-17 00:15:00', 2, 2),  -- Loathe
       ('2025-08-17 01:00:00', '2025-08-17 02:15:00', 27, 2);
-- Imperial Triumphant

-- Escenario: Drakkar de Guadalquivir
INSERT INTO performances (start_time, end_time, artist_id, stage_id)
VALUES ('2025-08-16 19:00:00', '2025-08-16 20:00:00', 22, 3), -- Allt
       ('2025-08-16 20:30:00', '2025-08-16 21:30:00', 37, 3), -- In Mourning
       ('2025-08-16 22:00:00', '2025-08-16 23:15:00', 38, 3), -- Alcest
       ('2025-08-16 23:45:00', '2025-08-17 01:00:00', 17, 3), -- Deafheaven
       ('2025-08-17 01:30:00', '2025-08-17 02:30:00', 39, 3), -- Acid Bath
       ('2025-08-16 18:00:00', '2025-08-16 19:00:00', 33, 3), -- Amira Elfeky (Early bird)
       ('2025-08-16 17:00:00', '2025-08-16 18:00:00', 36, 3); -- Jutes (Afternoon vibes)