CREATE TABLE IF NOT EXISTS Bans
(
    ID     INTEGER PRIMARY KEY AUTOINCREMENT,
    Name   varchar(32) NOT NULL COLLATE NOCASE,
    Reason TEXT        NOT NULL,
    Time   int(11)     NOT NULL
);


/*===========================================*/

CREATE TABLE `Bans`
(
    `ID`     int(16)     NOT NULL,
    `Name`   varchar(32) NOT NULL,
    `Reason` text,
    `Time`   int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Mutes`
(
    `ID`     int(16)     NOT NULL,
    `Name`   varchar(32) NOT NULL,
    `Reason` text,
    `Time`   int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Spectates`
(
    `ID`       int(16)     NOT NULL,
    `Name`     varchar(32) NOT NULL,
    `Target`   varchar(32) NOT NULL,
    `World`    varchar(16) NOT NULL,
    `X`        int(16)     NOT NULL,
    `Y`        int(16)     NOT NULL,
    `Z`        int(16)     NOT NULL,
    `namedTag` text        NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;