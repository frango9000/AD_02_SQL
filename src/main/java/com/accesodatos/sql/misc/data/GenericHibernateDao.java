package com.accesodatos.sql.misc.data;

import com.accesodatos.sql.misc.Flogger;
import com.accesodatos.sql.misc.Globals;
import com.accesodatos.sql.misc.model.IPersistable;
import com.google.common.collect.Sets;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

public class GenericHibernateDao<K, V extends IPersistable<K>> implements Globals {

    protected Session session;
    protected Class<V> clazz; // E = Entity Class
    protected String ID_COL_NAME;


    private CriteriaBuilder criteriaBuilder;

    public GenericHibernateDao(Session session) {
        this(session, null);
    }

    public GenericHibernateDao(Session session, Class<V> clazz) {
        setSession(session);
        if (clazz != null)
            this.clazz = clazz;
        else
            findGenericClass();
    }

    public GenericHibernateDao(Session session, Class<V> clazz, String ID_COL_NAME) {
        this(session, clazz);
        this.ID_COL_NAME = ID_COL_NAME;
    }

    public EntityManager getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session    = session;
        criteriaBuilder = session.getCriteriaBuilder();
    }

    @SuppressWarnings("unchecked")
    public Class<V> findGenericClass() {
        if (clazz == null)
            clazz = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }

    public Optional<V> findById(K id) {
        V v = session.find(clazz, id);
        return v != null ? Optional.of(v) : Optional.empty();
    }


    public Optional<V> queryById(K id) {
        Query<V> query = session.createQuery(String.format("SELECT p FROM %s p WHERE %s = '%s'", clazz.getSimpleName(), ID_COL_NAME, id.toString()));
        return Optional.of(query.uniqueResult());
    }

    public V querySqlById(K id) {
        NativeQuery<V> query = session.createNativeQuery(String.format("SELECT p.* FROM %ss p WHERE %s = '%s'", clazz.getSimpleName().toLowerCase(), ID_COL_NAME, id.toString()), clazz);
        return query.getSingleResult();
    }

    public Set<V> queryAll() {
        Query<V> query = session.createQuery("SELECT p FROM " + clazz.getSimpleName() + " p");
        return Sets.newHashSet(query.list());
    }

    public Set<V> querySqlAll() {
        NativeQuery<V> query = session.createNativeQuery("SELECT * FROM " + clazz.getSimpleName().toLowerCase() + "s p", clazz);
        return Sets.newHashSet(query.list());
    }

    public Set<V> findAll() {
        CriteriaQuery<V> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<V> entity = criteriaQuery.from(clazz);
        criteriaQuery.select(entity).orderBy(criteriaBuilder.asc(entity.get(ID_COL_NAME)));
        return Sets.newHashSet(session.createQuery(criteriaQuery).getResultList());
    }

    public Set<V> findSome(final Set<K> ids) {
        CriteriaQuery<V> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<V> entity = criteriaQuery.from(clazz);
        criteriaQuery.select(entity)
                     .where(entity.get(ID_COL_NAME).in(ids))
                     .orderBy(criteriaBuilder.asc(entity.get(ID_COL_NAME)));
        return Sets.newHashSet(session.createQuery(criteriaQuery).getResultList());
    }

    public long count() {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<V> entity = query.from(clazz);
        query.select(criteriaBuilder.count(entity));
        return session.createQuery(query).getSingleResult();
    }

    public Optional<V> save(V entity) {
        if (!session.getTransaction().isActive()) {
            try {
                session.getTransaction().begin();
                session.save(entity);
                session.getTransaction().commit();
                return Optional.of(entity);
            } catch (EntityExistsException e) {
                Flogger.atInfo().withCause(e).log();
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
            }
        }
        return Optional.empty();
    }

    public final Set<V> save(final Set<V> entities) {
        Set<V> returnSet = Sets.newHashSet();
        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
            for (V entity : entities) {
                try {
                    session.save(entity);
                    returnSet.add(entity);
                } catch (EntityExistsException e) {
                    Flogger.atInfo().withCause(e).log();
                }
            }
            session.getTransaction().commit();
        }
        return returnSet;
    }


    public boolean update(final V entity) {
        if (!session.getTransaction().isActive()) {
            try {
                session.getTransaction().begin();
                session.update(entity);
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                Flogger.atWarning().withCause(e).log();
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
            }
        }
        return false;
    }


    public boolean delete(final K id) {
        return delete(findById(id).orElseThrow(IllegalArgumentException::new));
    }


    public boolean delete(final V object) {
        if (!session.getTransaction().isActive()) {
            try {
                session.getTransaction().begin();
                session.delete(object);
                session.getTransaction().commit();
                return true;
            } catch (IllegalArgumentException e) {
                session.getTransaction().rollback();
                Flogger.atWarning().withCause(e).log();
            }
        }
        return false;
    }


    public void deleteSome(final Set<V> objects) {
        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
            for (V object : objects) {
                session.delete(object);
            }
            session.getTransaction().commit();
        }
    }

    public void deleteSomeIds(final Set<K> ids) {
        deleteSome(findSome(ids));
    }


    public void deleteAll() {
        deleteSome(findAll());
    }

    public void flush() {
        session.getTransaction().begin();
        session.flush();
        session.getTransaction().commit();
    }


    public void flushAndClear() {
        session.flush();
        session.clear();
    }

}
