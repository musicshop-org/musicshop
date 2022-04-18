
-- users
insert into tbl_user VALUES (0, 'admin');
insert into tbl_user VALUES (1, 'essiga');
insert into tbl_user VALUES (2, 'prescherm');
insert into tbl_user VALUES (3, 'mayerb');
insert into tbl_user VALUES (4, 'dreherf');
insert into tbl_user VALUES (5, 'egartnerf');
insert into tbl_user VALUES (6, 'meierm');
insert into tbl_user VALUES (7, 'unterkoflera');
insert into tbl_user VALUES (8, 'tf-test');


-- topics

-- all topics (admin)
insert into tbl_topic VALUES (0, 'system', 0, 0);
insert into tbl_topic VALUES (1, 'order', 0, 1);

-- salesperson & operator (essiga)
insert into tbl_topic VALUES (2, 'system', 1, 0);
insert into tbl_topic VALUES (3, 'order', 1, 1);

-- salesperson (perscherm)
insert into tbl_topic VALUES (4, 'system', 2, 0);

-- operator (mayerb)
insert into tbl_topic VALUES (5, 'system', 3, 0);
insert into tbl_topic VALUES (6, 'order', 3, 1);

-- salesperson & operator (dreherf)
insert into tbl_topic VALUES (7, 'system', 4, 0);
insert into tbl_topic VALUES (8, 'order', 4, 1);

-- operator (egartnerf)
insert into tbl_topic VALUES (9, 'system', 5, 0);
insert into tbl_topic VALUES (10, 'order', 5, 1);

-- salesperson (meierm)
insert into tbl_topic VALUES (11, 'system', 6, 0);

-- nothing (unterkoflera)

-- all topics (tf-test)
insert into tbl_topic VALUES (12, 'system', 8, 0);
insert into tbl_topic VALUES (13, 'order', 8, 1);