INSERT INTO tbl_invoice (id, invoice_id, paymentMethod, customerType)
VALUES
    (1, 1, 'CASH', 'EXISTING'),
    (2,2,'CREDIT_CARD', 'ANONYMOUS');

INSERT INTO tbl_invoiceLineItems(id, mediumType, name, quantity, price)
VALUES
    (1, 'CD', 'Seeed', 2, 20.00),
    (2, 'CD', 'Thriller', 3, 15.00);

INSERT INTO invoice_invoiceLineItems (idx, invoice_id, invoiceLineItems_id)
VALUES
    (0,1,2),
    (0,2,1);