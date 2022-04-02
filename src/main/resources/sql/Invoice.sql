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


INSERT INTO tbl_employee (id, employee_id,"name", username)
VALUES
    (1, '3b51d1bb-c4af-4553-99e8-c5dc827b9ef9','Benjamin', 'benjamin_2022'),
    (2, 'fc1a6b93-08a1-46ae-821f-1104066ee0c3','Adrian', 'adrian_2022'),
    (3, 'd295f480-30ed-4031-94a5-8d93475cb81b','Achim', 'achim_2022'),
    (4, '0de95b56-5f37-44e4-86de-b8d20537262c','Matthias', 'matthias_2022'),
    (5, 'b2e3834b-c1cb-449c-b481-3a9f317880bd','Marco', 'marco_2022'),
    (6, 'cb2e23b2-8f38-4ebe-9f40-0ef295385efa','FabianD', 'fabianD_2022'),
    (7, 'd71c9abc-2e56-4d5c-bf03-cce572dd667b','FabianE', 'fabianE_2022');