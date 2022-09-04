CREATE TABLE IF NOT EXISTS Items
(
    ID          INTEGER PRIMARY KEY AUTOINCREMENT,
    SellerName  varchar(32) NOT NULL COLLATE NOCASE,
    Price       REAL        NOT NULL,
    ItemID      INT         NOT NULL,
    ItemDamage  INT         NOT NULL,
    ItemCount   INT         NOT NULL,
    NbtHex      varchar(32) NOT NULL,
    ExpiredTime INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS Storage
(
    ID         INTEGER PRIMARY KEY AUTOINCREMENT,
    Name       varchar(32) NOT NULL COLLATE NOCASE,
    ItemID     int(16)     NOT NULL,
    ItemDamage int(16)     NOT NULL,
    ItemCount  int(16)     NOT NULL,
    NbtHex     varchar(32) DEFAULT NULL
);