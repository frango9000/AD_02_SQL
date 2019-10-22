/*

exercicio 19
baserelacionalB

a partir da taboa produtos creada no exerciio anterior e usando un resultset de tipo scrollable, updatable que devolte todas as
filas da taboa , desenvolver catro metodos
que permitan
    - listar o contido completo do resultset
    - actualizar dende dentro do resultet : por exemplo a fila do producto p2 facendo que o seu precio pase a ser 8
    - inserir dende dentro do resultset unha fila de valores : por exemplo o produto  p4, martelo, 20
    - borrar : por exemplo a  fila de codigo p3 tamen dende dentro do resultset

Lembrar que para consultar  con posibilidade de actualizar o resultado do consultado debo usar un obxecto Statement creado cas opcions
 scrollable, updatable , e aplicarlle despois o  metodo executeQuery("consulta") para obter o resultset . E dicir
    Statement statement= conn.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
    e despois crear o obxecto ResultSet a partir de aplicar o metodo executeQuery("consulta") a o obxecto Statement anterior
         para inserir un rexistro:
                invocar o metodo moveToInsertRow() do obxecto ResultSet
                introducir valores nos campos do rexistro que queremos inserir
                    usar metodo updateString("nome_campo", valor) do ResultSet ( se e in enteiro sera updateInt("nome_campo", valor) )
                inserir a fila : invocar o metodo insertRow() do obxecto ResultSet
        para actualizar un rexistro:
                unha vez situado nun rexisro do obxecto ResultSet:
                    actualizar un campo: invocar o metodo updateString("nome_campo", valor) do  ResultSet (se e un un enteiro sera :
                    updateInt("nome_campo", valor))
		    a continuación para actualizar a fila que conten os campos actualizados ; invocar o metodo updateRow()

        para borrar un rexistro (sobre o que estamos situados : invocar o metodo deleteRow()


outros metodos deste resultset (scrollable and updatable):
    first() : move o cursor a primeira fila do obxecto ResultSet
    last() : move o cursor a ultima  fila do obxecto ResultSet
    isLast(): retorna un valor verdadeiro se estamos posicionados na ultima fila do obxecto ResultSet
    close()
    next():   move o cursor a proxima  fila do obxecto ResultSet
    previous(): move o cursor a  fila previa a actual do obxecto ResultSet


IMPORTANTE: a consulta de todos os campos dunha fila debe facerse explicitando o nome da tabao antes do * , e decir:  select produtos.* from produtos . . .

 */
package com.accesodatos.sql.AD_19_baserelacionalB;

import com.accesodatos.sql.AD_18_baserelacionalA.Producto;
import com.accesodatos.sql.misc.SessionDB;
import java.util.ArrayList;

public class Main19 {

    public static void main(String[] args) {
        String[] cod = {"p1", "p2", "p3"};
        String[] desc = {"parafusos", "cravos", "tachas"};
        int[] prezo = {3, 4, 5};
        ArrayList<Producto> products = new ArrayList<>();
        for (int i = 0; i < cod.length; i++) {
            products.add(new Producto(cod[i], desc[i], prezo[i]));
        }
        if (SessionDB.getSession().connect()) {

            //establish  connection
            SessionDB.getSession().setAutoclose(false);

            //clean table
            SessionDB.getSession().dropTable("productos");
            SessionDB.getSession().createTables(Main19.class.getResource("/sql/tablaproductos18.sql").getPath());

            for (Producto producto : products) {
                producto.insertIntoDB();
            }

            System.out.println("Actualizamos p2");
            ProductosDaoNav.getSession().updateEnResultSet("p2", 8);
            System.out.println(ProductosDaoNav.getSession().query("p2"));

            System.out.println("Insertamos p4");
            Producto p4 = new Producto("p4", "martelo", 20);
            ProductosDaoNav.getSession().insertEnResultSet(p4);
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));

            System.out.println("Eliminamos p1");
            ProductosDaoNav.getSession().deleteEnResultSet("p1");
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));

            SessionDB.getSession().setAutoclose(true);
        }

    }

}
