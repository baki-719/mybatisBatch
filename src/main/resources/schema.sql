CREATE TABLE "ORDER" (
                      order_id   INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                      product_id VARCHAR(64) NOT NULL,
                      order_qty  INTEGER NOT NULL,
                      create_at  DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE "TOTAL_ORDER_QTY" (
                         summary_id       INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                         product_id       VARCHAR(64) NOT NULL,
                         total_order_qty  INTEGER NOT NULL,
                         create_at        DATETIME NOT NULL DEFAULT NOW()
);
