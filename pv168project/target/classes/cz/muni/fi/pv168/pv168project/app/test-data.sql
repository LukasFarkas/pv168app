-- my-test-data.sql

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('Jozko Mamradmrkvu', 2, 400.00, 'ENGLAND');

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('Bo Borovsky', 8, 400.00, 'ENGLAND');

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('La Lfsf', 4, 100.00, 'ENGLAND');

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('Pu Papi', 4, 800.00, 'ENGLAND');

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('Igi Ga', 4, 400.00, 'INDIA');

INSERT INTO TEACHER (FULLNAME, SKILL, PRICE, REGION) VALUES ('Igi Ga', 4, 400.00, 'INDIA');

INSERT INTO TEACHER (FULLNAME, SKILL, PRICE, REGION) VALUES ('Lala Po', 10, 1000.00, 'RUSSIA');

INSERT INTO STUDENT (FULLNAME, SKILL, PRICE, REGION) VALUES ('Petka Plastova', 4, 400.00, 'ENGLAND');

--bacha na vkladani lessonu takto, nemusim tam mit teachera ani studenta s pozadovanym id:
INSERT INTO LESSON (PRICE, SKILL, REGION, STUDENTID, TEACHERID) VALUES (400.00, 2, 'ENGLAND', 1, 1);

INSERT INTO LESSON (PRICE, SKILL, REGION, STUDENTID, TEACHERID) VALUES (500.00, 3, 'ENGLAND', 2, 1);
