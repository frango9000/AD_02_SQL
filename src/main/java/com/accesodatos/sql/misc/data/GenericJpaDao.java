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

public class GenericJpaDao<K, V extends IPersistable<K>> implements Globals {

    private EntityManager entityManager;
    protected Class<K> clazz; // E = Entity Class
    protected String ID_COL_NAME;


    private CriteriaBuilder criteriaBuilder;

    public GenericJpaDao(EntityManager entityManager) {
        this(entityManager, null);
    }

    public GenericJpaDao(EntityManager entityManager, Class<K> clazz) {
        setEntityManager(entityManager);
        if (clazz != null)
            this.clazz = clazz;
        else
            findGenericClass();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder    = entityManager.getCriteriaBuilder();
    }

    @SuppressWarnings("unchecked")
    public Class<K> findGenericClass() {
        if (clazz == null)
            clazz = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }

    public Optional<K> findById(V id) {
        K v = entityManager.find(clazz, id);
        return v != null ? Optional.of(v) : Optional.empty();
    }

    public Set<K> findAll() {
        CriteriaQuery<K> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<K> entity = criteriaQuery.from(clazz);
        criteriaQuery.select(entity).orderBy(criteriaBuilder.asc(entity.get(ID_COL_NAME)));
        return Sets.newHashSet(entityManager.createQuery(criteriaQuery).getResultList());
    }

    public Set<K> findSome(final Set<V> ids) {
        CriteriaQuery<K> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<K> entity = criteriaQuery.from(clazz);
        criteriaQuery.select(entity)
                     .where(entity.get(ID_COL_NAME).in(ids))
                     .orderBy(criteriaBuilder.asc(entity.get(ID_COL_NAME)));
        return Sets.newHashSet(entityManager.createQuery(criteriaQuery).getResultList());
    }

    public long count() {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<K> entity = query.from(clazz);
        query.select(criteriaBuilder.count(entity));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Optional<K> save(K entity) {
        if (!entityManager.getTransaction().isActive()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
                return Optional.of(entity);
            } catch (EntityExistsException e) {
                Flogger.atInfo().withCause(e).log();
                if (entityManager.getTransaction().isActive())
                    entityManager.getTransaction().rollback();
            }
        }
        return Optional.empty();
    }

    public final Set<K> save(final Set<K> entities) {
        Set<K> returnSet = Sets.newHashSet();
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            for (K entity : entities) {
                try {
                    entityManager.merge(entity);
                } catch (EntityExistsException e) {
                    returnSet.add(entity);
                    Flogger.atInfo().withCause(e).log();
                }
            }
            entityManager.getTransaction().commit();
        }
        return returnSet;
    }


    public boolean refresh(final K entity) {
        if (!entityManager.getTransaction().isActive()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.refresh(entity);
                entityManager.getTransaction().commit();
                return true;
            } catch (Exception e) {
                Flogger.atWarning().withCause(e).log();
                if (entityManager.getTransaction().isActive())
                    entityManager.getTransaction().rollback();
            }
        }
        return false;
    }


    public boolean delete(final V id) {
        return delete(findById(id).orElseThrow(IllegalArgumentException::new));
    }


    public boolean delete(final K object) {
        if (!entityManager.getTransaction().isActive()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.remove(object);
                entityManager.getTransaction().commit();
                return true;
            } catch (IllegalArgumentException e) {
                entityManager.getTransaction().rollback();
                Flogger.atWarning().withCause(e).log();
            }
        }
        return false;
    }


    public void deleteSome(final Set<K> objects) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            for (K object : objects) {
                entityManager.remove(object);
            }
            entityManager.getTransaction().commit();
        }
    }

    public void deleteSomeIds(final Set<V> ids) {
        deleteSome(findSome(ids));
    }


    public void deleteAll() {
        deleteSome(findAll());
    }

    public void flush() {

        entityManager.getTransaction().begin();
        entityManager.flush();
        entityManager.getTransaction().commit();
    }


    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}
