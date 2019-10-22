/*

exercicio 21
baserelacionalD
 utilizando a conexion ao usuario hr sobre  a base orcl e creando previamente
 un  resulset sobre a taboa produtos ,  obter e amosar os  nomes tipos e
 tamaños das columnas de dito  resulset utilizando  a clase ResulSetMetaData. p resultado
 deberría parecerse a isto:

CODIGO,VARCHAR2,3
DESCRICION,VARCHAR2,15
PRICE,NUMBER,39

notas:

 * como obter un obxecto ResultSetMetaData dende un resultSet:
 debe aplicarse o metodo getMetaData() ao resultSet
 * como obter informacion dos metadatos dende o obxecto ResultSetMetaData:
 deben aplicarse calquera de estes metodos segun a informacion que quera obter:
 getColumnCount()-- devolta o numero de columnas da taboa
 getColumnName(int)-- devolta o nome da columna cuxa posicion se pase
 getColumnTypeName(int)-- devolta o tipo de columna cuxa posicion se pase


 */
package com.accesodatos.sql.AD_21_baserelacionalD;

import com.accesodatos.sql.AD_18_baserelacionalA.ProductosDao;

public class Main21 {

    public static void main(String[] args) {
        System.out.println(ProductosDao.getSession().describeTable());
    }

}
