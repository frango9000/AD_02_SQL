
/*
exercicio 23
baserelacionaF
callableStatement
proxecto java : pjavaprocracle
este exercicio ten dous  pasos:

primeiro: (feito a continuacion)
desenvolver en oracle un procedemento en pl/sql chamado pjavaprocoracle
con dous  parametros , o primeiro so dentrada (in) e o segundo de
entrada saida (in out) , ambos de tipo integer .
O procecedemeto retorna no parametro de saida a suma
dos dous parametros .

segundo: a desenvolver por o alumno
Desenvolver proxecto java que acceda ao procedemento  anterior pasandolle
dous numeros calesqueira e  imprimindo o resultado devolto pola execucion de dito procedemento.:

notas:
- Para este paso debemos ter oracle e o  listener lanzados, e conectarnos a base orcl no porto 1521
como o usuario hr/hr, como xa fixemos en exercicios anteriores, e dende ahí crer e probar o procedemento co dodigo que se achega
- Debes crear un obxecto CallableStatement mediante unha invocacion do metodo prepareCall(chamda_ao_procedemento)  dun obxecto Connection.
Este obxecto CallableStatement ten uns metodos que permiten asignar valores aos parametros do porcedemento
invocado co metodo prepareCall mencionado anteriormente , de xeito parecido a como faciamos cas sentenzas preparadas nun exercicio anterior
- debes buscar informacion sobre todos este metodos na interede



SET SERVEROUTPUT ON;
Create or replace procedure pjavaprocoracle (entrada integer, saida in out integer) as

BEGIN
saida := saida+entrada;
end;


/

probar o procedemento dende sqlplus
DECLARE
a integer := 3;
b integer := 7;

BEGIN
pjavaprocoracle(a,b);
DBMS_OUTPUT.PUT_LINE(b);
end;
/
show errors;
*/
package com.accesodatos.sql.AD_23_baserelacionaF;

public class Main23 {

}
