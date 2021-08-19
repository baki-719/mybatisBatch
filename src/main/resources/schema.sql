CREATE TABLE "ORDER" (
                      order_id   INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                      product_id VARCHAR(64) NOT NULL,
                      order_qty  INTEGER NOT NULL,
                      create_at  DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE "DELIVERY" (
                         delivery_id      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                         order_id         INTEGER NOT NULL,
                         order_qty        INTEGER NOT NULL,
                         delivery_type    VARCHAR(64) NOT NULL,
                         create_at        DATETIME NOT NULL DEFAULT NOW()
);
