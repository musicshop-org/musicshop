--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;

INSERT INTO tbl_invoice (id, invoiceid, paymentMethod, date)
VALUES
    (1, 111, 'CASH', to_date('2022-04-04','YYYY-MM-DD')),
    (2, 222, 'CREDIT_CARD', to_date('2022-04-01','YYYY-MM-DD'));

INSERT INTO tbl_invoiceLineItem(id, mediumType, name, quantity, price, invoiceid, idx)
VALUES
    (1, 'CD', 'Seeed', 2, 20.00, 1, 0),
    (2, 'CD', 'Thriller', 3, 15.00, 1, 1),
    (3, 'CD', 'Beautiful', 5, 10.00, 2, 0);