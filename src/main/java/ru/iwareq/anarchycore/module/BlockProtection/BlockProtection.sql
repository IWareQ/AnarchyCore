CREATE TABLE IF NOT EXISTS Regions
(
    ID    INTEGER PRIMARY KEY AUTOINCREMENT,
    Name  varchar(32) NOT NULL COLLATE NOCASE,

    MainX INT         NOT NULL,
    MainY INT         NOT NULL,
    MainZ INT         NOT NULL,

    Pos1X INT         NOT NULL,
    Pos1Y INT         NOT NULL,
    Pos1Z INT         NOT NULL,

    Pos2X INT         NOT NULL,
    Pos2Y INT         NOT NULL,
    Pos2Z INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS Members
(
    ID       INTEGER PRIMARY KEY AUTOINCREMENT,
    Name     varchar(32) NOT NULL COLLATE NOCASE,
    RegionID INT         NOT NULL
);