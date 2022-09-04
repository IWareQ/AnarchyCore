CREATE TABLE IF NOT EXISTS Bans
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    Reason TEXT        NOT NULL,
    Time   int(11)     NOT NULL
);

CREATE TABLE IF NOT EXISTS Mutes
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    Reason TEXT        NOT NULL,
    Time   int(11)     NOT NULL
);

CREATE TABLE IF NOT EXISTS Spectates
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    Target varchar(32) NOT NULL COLLATE NOCASE,
    World  varchar(32) NOT NULL COLLATE NOCASE,
    X      int(16)     NOT NULL,
    Y      int(16)     NOT NULL,
    Z      int(16)     NOT NULL,
    nbtHex TEXT        NOT NULL
);