package com.accesodatos.sql.misc.data;

import com.accesodatos.sql.misc.model.Producto;
import org.hibernate.Session;

public class ProductHibernateDao extends GenericHibernateDao<String, Producto> {

    public ProductHibernateDao(Session session) {
        super(session, Producto.class, "codigo");
    }
}
