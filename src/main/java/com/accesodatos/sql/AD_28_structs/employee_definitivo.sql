drop table empregadosbdor;
drop type tipo_emp;
create type tipo_emp as object
(
    nome  varchar2(10),
    edade number
);
/
create table empregadosbdor
(
    id      number,
    emp     tipo_emp,
    salario number
);

insert into empregadosbdor
values (1, tipo_emp('luis', 23), 1800);
insert into empregadosbdor
values (2, tipo_emp('ana', 30), 2100);
insert into empregadosbdor
values (3, tipo_emp('pedro', 40), 1800);
commit;

