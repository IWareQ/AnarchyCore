CREATE TABLE IF NOT EXISTS Clans
(
    ID   INTEGER PRIMARY KEY AUTOINCREMENT,
    Name varchar(32) NOT NULL COLLATE NOCASE
);

CREATE TABLE IF NOT EXISTS Members
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    Role   varchar(32) NOT NULL COLLATE NOCASE,
    ClanID int(16)     NOT NULL
);

CREATE TABLE IF NOT EXISTS RequestsClan
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    ClanID int(16)     NOT NULL
);

CREATE TABLE IF NOT EXISTS RequestsPlayer
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    ClanID int(16)     NOT NULL
);
