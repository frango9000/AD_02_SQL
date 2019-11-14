alter session set nls_date_format = 'dd.mm.yyyy hh24:mi:ss';

drop table componentes cascade constraints;


create table componentes
(
    codc  varchar2(3),
    nomec varchar2(15),
    graxa integer,
    primary key (codc)
);
insert into componentes
values ('c1', 'vacuno', 5);
insert into componentes
values ('c2', 'ovino', 20);
insert into componentes
values ('c3', 'avicola', 10);
insert into componentes
values ('c4', 'avicola', 5);


commit;
select *
from componentes;


/*
 @/home/oracle/NetBeansProjects/examen1evb/taboasexameb.sql



componentes

COD NOMEC		 GRAXA
--- --------------- ----------
c1  vacuno		     5
c2  ovino		    20
c3  avicola		    10
c4  avicola		     5




*/
