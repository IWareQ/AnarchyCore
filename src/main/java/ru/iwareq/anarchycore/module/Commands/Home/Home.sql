CREATE TABLE IF NOT EXISTS Homes
(
    ID   INTEGER PRIMARY KEY AUTOINCREMENT,
    Name varchar(32) NOT NULL COLLATE NOCASE,
    X    int(16)     NOT NULL,
    Y    int(16)     NOT NULL,
    Z    int(16)     NOT NULL
);