/*
O tipo java.sql.Struct permite analizar o contido dun campo tipo obxecto (creado por o suario)

para ler dende java  un campo tipo obxecto que se atopa entre outros campos dun resulset (rs) debemos facer :

rs.getObject(n) , sendo n a posicion que ocupa o campo no resulset contando dende 1.

o resultado de esta lectura temos que convertilo a un tipo Struct deste xeito:


 java.sql.Struct x = rs.getObject(n)

despois desto debemos crear un array cos campos do obxecto lido

Object[] campos = x.getAttributes();

e xa podemos pasar  os atributos do obxecto a os tipos oracle
por exemplo se o primeiro campo e un String e o segundo e un enteiro faremos

      String z = (String) empValues[0];
      java.math.BigDecimal y = (java.math.BigDecimal) empValues[1];

Exercicio 28 : bdor3
-dispomos dun script chamado employee.sql que crea duas taboas . lanzao como o usario hr de oracle
- desenvolve un proxecto java chamado bdor3 que  amose todos os datos de todos os empregados da taboa empregados



 */
package com.accesodatos.sql.AD_28_structs;

import com.accesodatos.sql.misc.SessionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;

public class Employee28 {

    public static void main(String[] args) {
        SessionDB sessionDB = SessionDB.getSession();
        if (sessionDB.connect()) {
            String sql = "SELECT * FROM empregadosbdor";
            try (Statement statement = sessionDB.getConn().createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        System.out.println("Empleado:");
                        System.out.println("id: " + resultSet.getInt("id"));
                        Struct tipoemp = (Struct) resultSet.getObject("emp");
                        Object[] campos = tipoemp.getAttributes();
                        for (Object o : campos) {
                            System.out.println(o);
                        }
                        System.out.println("salario: " + resultSet.getInt("salario") + "\n");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
