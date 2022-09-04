CREATE TABLE IF NOT EXISTS Auth
(
    ID       INTEGER PRIMARY KEY AUTOINCREMENT,
    Name     varchar(32) NOT NULL COLLATE NOCASE,
    DateReg  varchar(32) NOT NULL,
    IpReg    varchar(32) NOT NULL,
    DateLast varchar(32) DEFAULT NULL,
    IpLast   varchar(32) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS Users
(
    ID         INTEGER PRIMARY KEY AUTOINCREMENT,
    Name       varchar(32) NOT NULL COLLATE NOCASE,
    Xuid       varchar(32) NOT NULL,
    Permission varchar(16) NOT NULL DEFAULT 'player',
    Money      REAL        NOT NULL DEFAULT '0.0',
    GameTime   int(64)     NOT NULL DEFAULT '0'
);