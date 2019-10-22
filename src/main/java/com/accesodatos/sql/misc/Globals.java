/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accesodatos.sql.misc;

import java.util.logging.Level;

/**
 * @author NarF
 */
public interface Globals {

    boolean DEBUG = true;
    boolean SQL_DEBUG = true;
    boolean SQL_CONN = false;
    boolean VERBOSE_FACTORY = true;

    Level LOG_LEVEL = Level.ALL;

    String DB_PREFIX = "tdc_";

    String ROOT_PATH = "src/main/java/tiendaclub/";

    boolean SAFE_UPDATE = true;//enforce objects to have a backup to fallback to in case update statement fails
}
