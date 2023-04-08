SHOW tables;

DESC tb_user;

SELECT * FROM tb_user;
SELECT * FROM tb_authority;
SELECT * FROM tb_user_authorities;

SELECT * FROM tb_display;

DELETE * FROM tb_display_detail;

INSERT INTO tb_display_detail(id,seat_count, visit_date, display_dp_seq) values
(1, 2, 20230420, 707),
(2, 2, 20230421, 707),
(3, 2, 20230422, 707),
(4, 2, 20230423, 707),
(5, 2, 20230424, 707),
(6, 2, 20230425, 707),
(7, 2, 20230426, 707),
(8, 2, 20230427, 707),
(9, 2, 20230428, 707),
(10, 2, 20230429, 707),
(11, 2, 20230407, 704),
(12, 2, 20230411, 704),
(13, 2, 20230412, 704),
(14, 2, 20230413, 703),
(15, 2, 20230414, 703),
(16, 2, 20230415, 703),
(17, 2, 20230416, 703),
(18, 2, 20230417, 704),
(19, 2, 20230418, 705),
(20, 2, 20230419, 706),
(26, 2, 20230430, 707)
;

commit;


SELECT * FROM tb_display_detail;

SELECT * FROM tb_display;

DELETE FROM tb_display_detail;

