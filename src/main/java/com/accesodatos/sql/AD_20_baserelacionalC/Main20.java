/*
exercicio 20
baserelacionalC

Neste exercicio imos traballar as insercions , modificacions consulta   mediante sentenzas preparadas.
Unha sentenza preparada e unha sentenza SQL na que en lugar de valores concretos situamos interrogantes .
Antes de executar a sentenza debemos sustituir ditos interogantes polos valores concretos con que queremos
que se execute.
A ventaxa disto e que a base de datos pode subir a cache e reusar as sentenzas preparadas co que se executan
mais rapido que as sentenzas normais.

Para crea unha  sentenzas preparadas , no intre de usar o metodo createStatement() do obxecto Connection ,
debemos usar o metodo
preparedStatement(String sentenza), onde sentenza e unha sentenza SQL na que poremos interrogantes no intre de
 valores. Por exemplo
insert into tenda values( ?,?,?) where codtenda=?

Unha vez creada a sentenza preparada debemos sustituir os  interrogantes da sua sentenza por valores concretos para
o cal usamos os metodos adecuados da sentenza preparada en funcion do tipo de dato .
Por exemplo , se o primeiro interrogante da sentenza anterior esta sustituindo a un campo de tipo varchar2, o metodo
 a usar sera setString(int i, String p), onde a tetra "i" e a posicion que ocupa o interrogante na sentenza preparada
  e a letra "p" o valor que se vai ssutituir en lugar de dito interrogante. Concretamdo mais , se o a sentenza
   preparada se chama ps e o valor que quero sustituir e "froitas_neves " , a preparacion da sentenza para este
    valor será así:

ps.setString(1,"froitas__neves"),   o cal seria equivalente a ter feito:
insert into tenda values ("froitas__neves", ?,?) where codtenda =?)




1- metodo que insira filas  mediante sentenzas preparadas (insireprep) .

2- metodo que actualize unha fila  coñecido o seu codigo , mediante sentenzas preparadas (actuprep) .

3- realizar unha consulta a totalidade mediante resulset con sentenza preparada.

nota:
os metodos das sentenzas preparadas  para executar consultas e desenvolver operacions de actualizacion (insercion,
 modificacion e borrado)
chamanse igaula que os metodos vistos para sentenzas normais , xa vistos no exercicio BaserelaionalA e que son:
executeUpdate()
executeQuery()
pero sen parametros como se pode observar , posto que a sentenza a executar xa foi pasada como parametro cando
se creou a sentenza preparada


 */
package com.accesodatos.sql.AD_20_baserelacionalC;

import com.accesodatos.sql.AD_18_baserelacionalA.Producto;
import com.accesodatos.sql.AD_19_baserelacionalB.Main19;
import com.accesodatos.sql.AD_19_baserelacionalB.ProductosDaoNav;
import com.accesodatos.sql.misc.SessionDB;
import java.util.ArrayList;

public class Main20 {

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
            System.out.println("\nCreando tabla productos e insertando productos");
            SessionDB.getSession().createTables(Main19.class.getResource("/sql/tablaproductos18.sql").getPath());

            for (Producto producto : products) {
                producto.insertIntoDB();
            }
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));

            Producto producto = new Producto("p5", "alicate", 11);

            System.out.println("\nInsertando nuevo producto");
            ProductosDaoNav.getSession().insert(producto);
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));

            System.out.println("\nModificando producto");
            producto.setDescripcion("pinza");
            producto.setPrecio(15);
            ProductosDaoNav.getSession().update(producto);
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));

            System.out.println("\nEliminando producto");

            ProductosDaoNav.getSession().deleteConPreparedStatement(producto);
            ProductosDaoNav.getSession().queryAll().values().forEach(p -> System.out.println(p.toString()));
        }
    }

}
