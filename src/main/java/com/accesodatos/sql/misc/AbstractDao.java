/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accesodatos.sql.misc;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <V>
 * @author NarF
 */
public abstract class AbstractDao<K, V extends IPersistable<K>> implements IDao<K, V> {

    protected final HashMap<K, V> table = new HashMap<>();
    protected String TABLE_NAME;
    protected String ID_COL_NAME;

    protected SessionDB sessionDB = SessionDB.getSession();

    @Override
    public V get(K id) {
        if (id != null) {
            if (table.containsKey(id)) {
                return table.get(id);
            } else {
                return query(id);
            }
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<V> getSome(ArrayList<K> ids) {
        ArrayList<V> list = new ArrayList<>();
        if (ids.size() > 0) {
            ArrayList<K> idsToQuery = new ArrayList<>();
            for (K id : ids) {
                if (!table.containsKey(id)) {
                    idsToQuery.add(id);
                }
            }
            if (ids.size() > 0) {
                query(idsToQuery);
            }
            for (K id : ids) {
                list.add(table.get(id));
            }
        }
        return list;
    }

    @Override
    public HashMap<K, V> getMapOf(ArrayList<K> ids) {
        HashMap<K, V> filteredHashMap = new HashMap<>();
        if (ids.size() > 0) {
            ArrayList<V> objs = getSome(ids);
            for (V t : objs) {
                filteredHashMap.put(t.getId(), t);
            }
        }
        return filteredHashMap;
    }

    @Override
    public HashMap<K, V> getCache() {
        return table;
    }

    @Override
    public int delete(V t) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("DELETE FROM %s WHERE %s = '%s'", TABLE_NAME, ID_COL_NAME, t.getId().toString());
            try (Statement stmt = sessionDB.getConn().createStatement()) {
                rows = stmt.executeUpdate(sql);
                table.remove(t.getId());
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    @Override
    public int delete(K id) {
        return delete(table.get(id));
    }

    @Override
    public int deleteSome(ArrayList<V> toDelete) {
        int rows = 0;
        if (sessionDB.connect() && toDelete.size() > 0) {
            StringBuilder sql = new StringBuilder("DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL_NAME + " IN( 0");
            for (V t : toDelete) {
                sql.append(", ").append(t.getId());
            }
            sql.append(" )");
            try (Statement ps = sessionDB.getConn().createStatement()) {
                rows = ps.executeUpdate(sql.toString());
                toDelete.forEach(e -> {
                    table.remove(e.getId());
                });
                printSql(sql.toString());
            } catch (SQLException ex) {
                Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, sql.toString(), ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    protected static void printSql(String sql) {
        if (Globals.SQL_DEBUG) {
            System.out.println(sql);
        }
    }
}
