package com.accesodatos.sql;

import com.accesodatos.sql.misc.SessionDB;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SessionDB session = SessionDB.getSession();
        System.out.println(session.connect());

//        Producto p =
    }
}
