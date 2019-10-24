/*

exercicio 24
baserelacionalG


 callableStatement
 proxecto java : pjavafunoracle
 este exercicio ten dous  pasos:

 primeiro: (feito a continuacion)
 desenvolver en oracle unha funcion en pl/sql chamada prezo_produto
con un parametro de entrada que e o codigo dun produto. .
 O procecedemeto retorna no parametro de saida o prezo do produto .

 segundo: a desenvolver por o alumno
 Desenvolver proxecto java que acceda a funcion anterior pasandolle
 un codigo (por exemplo p2) e  imprimindo o resultado devolto pola execucion de dita funcion.:

 notas:
- Para este paso debemos ter oracle e o  listener lanzados, e conectarnos a base orcl no porto 1521
como o usuario hr/hr, como xa fixemos en exercicios anteriores, e dende ahí crear e probar a funcion
- Debes crear un obxecto CallableStatement mediante unha invocacion do metodo prepareCall(chamda_a_funcion)  dun obxecto Connection.
 Este obxecto CallableStatement ten uns metodos que permiten asignar valores aos parametros da funcion
invocado co metodo prepareCall mencionado anteriormente , de xeito parecido a como faciamos cas sentenzas preparadas nun exercicio anterior
- debes buscar informacion sobre todos este metodos na interede




drop function prezo_produto;
CREATE FUNCTION prezo_produto (codigox varchar2) RETURN NUMBER IS
prezo NUMBER;
BEGIN
SELECT price INTO prezo FROM produtos
WHERE codigo = codigox;
RETURN prezo;
END;

/
probar o procedemento dende sqlplus :
SET SERVEROUTPUT on
DECLARE
	a integer := 0;

BEGIN
a:=prezo_produto('p2');
DBMS_OUTPUT.PUT_LINE(a);
end;
/
show errors;

*/
package com.accesodatos.sql.AD_24_baserelacionaG;

public class Main24 {

}
