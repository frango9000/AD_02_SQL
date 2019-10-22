/*
exercicio25
 hiberoracle
 seguindo o exemplo que atoparas na seguinte direción:
http://netbeans.org/kb/docs/java/hibernate-java-se.html
 'Using Hibernate in a Java Swing Application'

Crea un proxecto chamado hiberoracle pero ten en conta que o unico obxectivo que se persegue e que conectes ca base
e sexas capaz de acceder aos seus rexistros. Queda a tua opcion traballar co interface grafica ou non.Se non queres
 traballar con interface grafica debes deterte no punto do exemplo intitulado 'Creating the Application GUI' e
  desenvolver as probas de inserir, modificar, consultar e borrar cos metodos creados a tal efecto
como fixemos ate o de agora .

Cando crees o proxecto lembra incluir a libreria 'hibernate' pero incorporando todos
os seus .jar , non a libreria en bloque. A ruta onde se atopan os .jar e :
 /home/oracle/netbeans-8.0/java/modules/ext/hibernate4/


Ten en conta que debes cambiar o nome da base de datos de  'sakila' a "orcl" e a conexion debe ser co usuario que
 usamos sempre en oracle , e decir "hr" con password "hr", host "ĺocalhost", porto 1521 e driver "jdbc:oracle:thin:"

 A aplicacion debe  realizar  operacions de inserción, modificacion, eliminacion de un obxecto utilizando os metodos
  save, update e delete de hibernate ,  e a consulta e  modificacion masiva createSQLQuery  de varios
   obxectos  na táboa produtos.

metodos a crear :
inserRow(String codigo,String descricion, int prezo)
busca(String cod)
modificaprezo(String cod,int prez)
modificades(String cod,String descri)
borraproduto(String cod)
actualizacion_masiva(String descri, int prezoref)  actualiza todas las descripciones a un valor dado, par todos
 los productos que superen un precio dado)
utilizar creatSQLQuery para este metodo





Lembra que a clase java que manexa os obxectos debe ser serializable e a taboa a mapear asociada debe ter
 clave primaria:

Debes crear no proxecto unha clase  productos que implemente a interfaz serializable
(esta clase produtos obtense directamente dende a base de datos orcl , taboa produtos,
se segues os pasos do exemplo indicado cambiando 'sakila' por 'orcl' e actor por 'produtos').
lembra que antes de facer too isto  debes poñer tamen como clave primaria o campo codigo  da taboa productos en
 caso de que ainda non o tiveras posto e antes de comenzar o proxecto.

Lembra tamen lanzar oracle e o listener (lsnrctl start)

As seguintes propiedades que podes modificar na pestana Design do ficheiro hibernate.cfg.xml, unha vez xenerado
 este segun o indica o exemplo referido ao principio debe ter os valores indicados a continuacion:

optional properties
    configutarion properties

        hibernate.dialect : org.hibernate.dialect.OracleDialect
        show_sql : true

    miscellaneous properties
        hibernate.query.factory_class:  org.hibernate.hql.classic.ClassicQueryTranslatorFactory


notas:
contidos dos ficheiros xenerados:
-- hibernate.cfg.xml : conten entre oustras as caracteristicas da conexion a base de datos
-- hibernateUtil.java conten o metodo para crear un obxecto sessionFactory
-- hibernate.revenge.xml : actua de filtro para decidir sobre que taboas da base vaise facer o mapeo
-- XXX.hbm.xml conten o ficheiro de mapeo, onde se relaciona cada atributo do
obxecto java ( pojo) ca columna correspondente da taboa onde se vai almacenar.



Cada vez que se lanze unha operacion contra a base de datos deberia abrirse e pecharse unha sesion e unha transacion.

Para crear un obxecto sessionFactory (chamdo por exemplo sesfact) debemos usar o seguinte comando (ou similar():
sesfact= new Configuration().configure().buildSessionFactory();

para crear unha sesion debemos invocar o metodo openSession() dun obxecto sessionfactory
session = sesfact.openSession();


metodos da clase Session para manipular obxectos na base :
get(Products.class, id)  leer un obxecto
save(obxecto) gardar un obxecto
update(obxecto)  actualizar un obxecto
delete(obxecto)  borrar un obxecto
createQuery(from taboa where...) consultar unha lista de obxectos con hql
createSQLQuery(select ……) consultar unha lista de obxectos co  sqlquery

notas importantes:

para iniciar unha transaccion usaremos o metodo beginTransaction() do obxecto session
tx= session.beginTransaction();
para pechar a transaccion usaremos o metodo commit() da transaccion
tx.commit();

antes de  gardar datos numericos en hiberoracle hai que pasalos a BigDecimal.

 */
package com.accesodatos.sql.AD_25_hiberoracle;

import com.accesodatos.sql.misc.data.HibernateUtil;
import com.accesodatos.sql.misc.data.ProductHibernateDao;
import com.accesodatos.sql.misc.model.Producto;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import org.hibernate.Session;

public class Main25 {

    public static ProductHibernateDao productstore;

    public static void main(String[] args) {
        Session session = HibernateUtil.openSession();
        productstore = new ProductHibernateDao(session);

        ArrayList<Producto> products = Producto.generateProducts();
        productstore.save(Sets.newHashSet(products));
        productstore.findAll().forEach(v -> System.out.println(v.toString()));

        System.out.println("\nObteniendo p2");
        Producto p2 = productstore.queryById("p2").get();
        System.out.println(p2);
        p2 = productstore.querySqlById("p2");
        System.out.println(p2);

        System.out.println("\nInsertando p4");
        Producto p4 = new Producto("p4", "martelo", 20);
        productstore.save(p4);
        productstore.queryAll().forEach(v -> System.out.println(v.toString()));

        System.out.println("\nModificando p4");
        p4.setDescripcion("Modificado");
        p4.setPrecio(999);
        productstore.update(p4);
        productstore.querySqlAll().forEach(v -> System.out.println(v.toString()));

        System.out.println("\nEliminando p2");
        productstore.delete(p2);
        productstore.queryAll().forEach(v -> System.out.println(v.toString()));

        System.exit(0);
    }

}
