/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accesodatos.sql.misc.data;

import com.accesodatos.sql.misc.model.IPersistable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @param <V>
 * @author NarF
 */
public interface IDao<K, V extends IPersistable> {

    V query(K id);

    HashMap<K, V> query(ArrayList<K> ids);

    HashMap<K, V> queryAll();

    V get(K id);

    ArrayList<V> getSome(ArrayList<K> ids);

    HashMap<K, V> getMapOf(ArrayList<K> ids);

    HashMap<K, V> getCache();

    int insert(V objecT);

    int update(V objecT);

    int updateDao(V objectT);

    int delete(V objecT);

    int delete(K id);

    int deleteSome(ArrayList<V> toDelete);
}
