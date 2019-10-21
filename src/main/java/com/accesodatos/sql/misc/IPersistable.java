/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accesodatos.sql.misc;

/**
 * @author NarF
 */
public interface IPersistable<K> {

    K getId();

    <E extends IPersistable<K>> AbstractDao<K, E> getDataStore();

    default int insertIntoDB() {
        return getDataStore().insert(this);
    }

    default int updateOnDb() {
        return getDataStore().update(this);
    }

    default int refreshFromDb() {
        return getDataStore().updateDao(this);
    }

    default int deleteFromDb() {
        return getDataStore().delete(this);
    }

}
