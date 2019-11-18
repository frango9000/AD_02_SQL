package com.accesodatos.sql.misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Clase Session se encagra de mantener la informacion de una conexion a una DB especifica. Para multiples bases de
 * datos crearemos multiples objetos SessionDB
 *
 * @author fsancheztemprano
 */
public class SessionDB implements Globals {

    //Syncronized Singleton
    private static SessionDB instance;

    private SessionDB() {

    }

    public static SessionDB getSession() {
        if (instance == null) {
            synchronized (SessionDB.class) {
                if (instance == null) {
                    instance = new SessionDB();
                }
            }
        }
        return instance;
    }

    protected Connection conn;

    protected String jdbcDriver = "jdbc:oracle:thin:";
    protected String jdbcIP = "10.0.9.26";
    //    protected String jdbcIP = "192.168.1.196";
    protected String jdbcPort = "1521";
    protected String jdbcCatalog = "orcl";

    protected String jdbcUser = "hr";
    protected String jdbcPassword = "hr";

    private boolean autoclose = true;

    /**
     * Getter para la clase Conexion
     *
     * @return Conexion
     */
    public Connection getConn() {
        return conn;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcIP() {
        return jdbcIP;
    }

    public void setJdbcIP(String jdbcIP) {
        this.jdbcIP = jdbcIP;
    }

    public String getJdbcPort() {
        return jdbcPort;
    }

    public void setJdbcPort(String jdbcPort) {
        this.jdbcPort = jdbcPort;
    }

    public String getJdbcCatalog() {
        return jdbcCatalog;
    }

    public void setJdbcCatalog(String jdbcCatalog) {
        this.jdbcCatalog = jdbcCatalog;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public static void printSql(Object sql) {
        if (Globals.SQL_DEBUG) {
            Flogger.atInfo().log(sql.toString());
        }
    }

    /**
     * establece la conexion a la DB
     *
     * @return true si la conexion fue establecida correctamente
     */
    public boolean connect(String jdbcIP, String jdbcPort, String jdbcCatalog, String jdbcUser, String jdbcPassword) {
        boolean success = false;
        try {
            if (conn == null || conn.isClosed()) {
                String string = jdbcDriver + "@" + jdbcIP + ":" + jdbcPort + ":" + jdbcCatalog;
                System.out.println(string);
                conn = DriverManager.getConnection(string, jdbcUser, jdbcPassword);
                if (Globals.SQL_CONN) {
                    System.out.println("Connection to " + conn.getMetaData().getDriverName() + " has been established. Catalog: " + conn.getCatalog());
                }
            } else {
                if (Globals.SQL_CONN) {
                    System.out.println("Connection to " + conn.getMetaData().getDriverName() + " already active.");
                }
            }
            success = true;
        } catch (SQLException e) {
            Flogger.atSevere().withCause(e).log(toString());
        }
        return success;
    }

    public boolean connect(String catalog) {
        return connect(jdbcIP, jdbcPort, catalog, jdbcUser, jdbcPassword);
    }

    public boolean connect() {
        return connect(jdbcIP, jdbcPort, jdbcCatalog, jdbcUser, jdbcPassword);
    }

    public boolean connectTry() {
        return connect(jdbcIP, jdbcPort, "", jdbcUser, jdbcPassword);
    }

    public boolean isLinkValid() {
        boolean valid = false;
        System.out.println(toString());
        if (connectTry()) {
            try {
                valid = conn.isValid(30);
            } catch (SQLException ex) {
                Flogger.atSevere().withCause(ex).log();
            } finally {
                close();
            }
        }
        return valid;
    }

    public boolean isSessionValid() {
        boolean valid = false;
        if (connect()) {
            try {
                valid = conn.isValid(30);
            } catch (SQLException ex) {
                Flogger.atSevere().withCause(ex).log();
            } finally {
                close();
            }
        }
        return valid;
    }

    /**
     * Finaliza una conexion a la DB
     */
    public void close() {
        try {
            if (autoclose) {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    if (Globals.SQL_CONN) {
                        System.out.println("Connection has been closed.");
                    }
                } else {
                    if (Globals.SQL_CONN) {
                        System.out.println("Connection was already closed.");
                    }
                }
            } else {
                if (Globals.SQL_CONN) {
                    System.out.println("Keeping conection alive.");
                }
            }
        } catch (SQLException e) {
            Flogger.atSevere().withCause(e).log();
        }
    }

    /**
     * @param autoclose
     */
    public void setAutoclose(boolean autoclose, String catalog) {
        if (autoclose) {
            this.autoclose = autoclose;
            close();
        } else {
            connect(catalog);
            this.autoclose = autoclose;
        }
        if (Globals.SQL_CONN) {
            System.out.println("Autoclose? " + autoclose);
        }
    }

    public void setAutoclose(boolean autoclose) {
        setAutoclose(autoclose, jdbcCatalog);
    }


    /**
     * Devuelve Una lista con los nombres de las tablas en una DB
     *
     * @return
     */
    public ArrayList<String> listTables(String catalogName) {
        String sql = "SELECT table_name from all_tables where OWNER = '" + jdbcUser.toUpperCase() + "'";
        ArrayList<String> tableNames = new ArrayList<>();
        if (connect(catalogName)) {
            try (Statement stmt = conn.createStatement()) {
                printSql(sql);
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    tableNames.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Flogger.atSevere().withCause(ex).log(sql);
            } finally {
                close();
            }
        }
        return tableNames;
    }

    public ArrayList<String> listTables() {
        return listTables(jdbcCatalog);
    }

    public int numOfTables(String catalogName) {
        return listTables(catalogName).size();
    }

    public int numOfTables() {
        return numOfTables(jdbcCatalog);
    }

//Mysql catalog creation
//
//
//    /**
//     * Devuelve Una lista con los nombres de las tablas en una DB
//     *
//     * @return
//     */
//    public ArrayList<String> listCatalogs() {
//        return listPrefixedCatalogs("");
//    }
//
//    /**
//     * Devuelve Una lista con los nombres de las tablas en una DB
//     *
//     * @return
//     */
//    public ArrayList<String> listPrefixedCatalogs(String catalogPrefix) {
//        String sql = "SHOW DATABASES like '" + catalogPrefix + "%'";
//        ArrayList<String> dbNames = new ArrayList<>();
//        if (connectTry()) {
//            try (Statement stmt = conn.createStatement()) {
//                printSql(sql);
//                ResultSet rs = stmt.executeQuery(sql);
//                while (rs.next()) {
//                    dbNames.add(rs.getString(1));
//                }
//            } catch (SQLException ex) {
//                Flogger.atSevere().withCause(ex).log(sql);
//            } finally {
//                close();
//            }
//        }
//        return dbNames;
//    }
//
//
//    /**
//     * devuelve true si la estructura de la DB activa es valida (coincide con la inicializada)
//     *
//     * @return true si es valida
//     */
//    public boolean isCatalogValid(String catalogName, String catalogModel) {
//        ArrayList<String> tables = listTables(catalogName);
//        StringBuilder tablesString = new StringBuilder();
//        tables.forEach(cnsmr -> tablesString.append(cnsmr).append("\n"));
//        printSql(tablesString);
//        Flogger.atInfo().log("Valid Schema: " + catalogName + "->" + catalogModel.matches(tablesString.toString()));
//        return catalogModel.matches(tablesString.toString());
//    }
//
//    public boolean isCatalogValid(String catalogModel) {
//        return isCatalogValid(getJdbcCatalog(), catalogModel);
//    }
//
//    public ArrayList<String> listValidCatalogs(String catalogModel) {
//        setAutoclose(false);
//        ArrayList<String> list = listCatalogs();
//        ArrayList<String> validList = new ArrayList<>();
//        for (String catalogName : list) {
//            if (isCatalogValid(catalogName, catalogModel)) {
//                validList.add(catalogName);
//            }
//        }
//        setAutoclose(true);
//        return validList;
//    }
//
//    public boolean createCatalog(String catalogName) {
//        boolean success = false;
//        if (connectTry() && catalogName.length() > 1) {
//            String sql = "CREATE DATABASE " + catalogName;
//            try (Statement statement = conn.createStatement()) {
//                printSql(sql);
//                success = statement.executeUpdate(sql) > 0;
//            } catch (SQLException e) {
//                Flogger.atSevere().withCause(e).log(sql);
//            } finally {
//                close();
//            }
//        }
//        return success;
//    }
//
//    public boolean createCatalog() {
//        return createCatalog(getJdbcCatalog());
//    }
//
//    public boolean dropCatalog(String catalogName) {
//        boolean success = false;
//        if (connectTry() && catalogName.trim().length() > 1) {
//            String sql = "DROP DATABASE " + catalogName;
//            try (Statement statement = conn.createStatement()) {
//                printSql(sql);
//                success = statement.executeUpdate(sql) > 0;
//            } catch (SQLException e) {
//                Flogger.atSevere().withCause(e).log(sql);
//            } finally {
//                close();
//            }
//        }
//        return success;
//    }
//
//    public boolean dropCatalog() {
//        return createCatalog(getJdbcCatalog());
//    }
//    public boolean hasCatalog(String catalogName) {
//        boolean hasCatalog = false;
//        if (connectTry() && catalogName.trim().length() > 1) {
//            String sql = "SHOW DATABASES like '" + catalogName.trim() + "'";
//            try (Statement statement = conn.createStatement()) {
//                printSql(sql);
//                hasCatalog = statement.execute(sql);
//            } catch (SQLException e) {
//                Flogger.atSevere().withCause(e).log(sql);
//            } finally {
//                close();
//            }
//        }
//        return hasCatalog;
//    }
//
//    public boolean useCatalog(String catalogName) {
//        boolean hasCatalog = false;
//        if (connectTry() && catalogName.trim().length() > 1) {
//            String sql = "USE '" + catalogName.trim() + "'";
//            try (Statement statement = conn.createStatement()) {
//                printSql(sql);
//                hasCatalog = statement.execute(sql);
//            } catch (SQLException e) {
//                Flogger.atSevere().withCause(e).log(sql);
//            } finally {
//                close();
//            }
//        }
//        return hasCatalog;
//    }

    //Catalog Creation Code

    public int createTables(String filepath) {
        int rows = 0;
        File sqlFile = new File(filepath);
        StringBuilder sqlcmd = new StringBuilder();
        try (Scanner scan = new Scanner(new BufferedInputStream(new FileInputStream(sqlFile)))) {
            while (scan.hasNext()) {
                sqlcmd.append(scan.nextLine()).append("\n");
            }
            String multicmd = sqlcmd.toString();
            String[] cmds = multicmd.split(";");
            if (connect()) {
                for (String sql : cmds) {
                    if (sql.length() > 1) {
                        try (Statement stmt = getConn().createStatement()) {
                            printSql(sql);
                            stmt.executeUpdate(sql.trim());
                            rows++;
                        } catch (SQLException ex) {
                            Flogger.atSevere().withCause(ex).log(sql);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Flogger.atSevere().withCause(e).log(sqlcmd.toString());
        } finally {
            printSql("Tables created: " + rows);
            close();
        }
        return rows;
    }

    public int createViews(String filepath) {
        int rows = 0;
        File sqlFile = new File(filepath);
        StringBuilder sqlcmd = new StringBuilder();
        try (Scanner scan = new Scanner(new BufferedInputStream(new FileInputStream(sqlFile)))) {
            while (scan.hasNext()) {
                sqlcmd.append(scan.nextLine()).append("\n");
            }
            String multicmd = sqlcmd.toString();
            String[] cmds = multicmd.split(";");
            if (connect()) {
                for (String sql : cmds) {
                    try (Statement stmt = getConn().createStatement()) {
                        printSql(sql);
                        stmt.executeUpdate(sql.trim());
                        rows++;
                    } catch (SQLException ex) {
                        Flogger.atSevere().withCause(ex).log(sql);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Flogger.atSevere().withCause(e).log(sqlcmd.toString());
        } finally {
            if (Globals.SQL_DEBUG) {
                System.out.println("Views created: " + rows);
            }
            close();
        }
        return rows;
    }

    public int insertData(String filepath) {
        int rows = 0;
        File sqlFile = new File(filepath);
        StringBuilder sqlcmd = new StringBuilder();
        try (Scanner scan = new Scanner(new BufferedInputStream(new FileInputStream(sqlFile)))) {
            while (scan.hasNext()) {
                sqlcmd.append(scan.nextLine()).append("\n");
            }
            String multicmd = sqlcmd.toString();
            String[] cmds = multicmd.split(";");
            if (connect()) {
                for (String sql : cmds) {
                    try (Statement stmt = conn.createStatement()) {
                        printSql(sql);
                        rows += stmt.executeUpdate(sql.trim());
                    } catch (SQLException ex) {
                        Flogger.atSevere().withCause(ex).log(sql);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Flogger.atSevere().withCause(e).log(sqlcmd.toString());
        } finally {
            if (Globals.SQL_DEBUG) {
                System.out.println("Inserts: " + rows);
            }
            close();
        }
        return rows;
    }

    public boolean dropTable(String tableName, boolean cascade) {
        boolean droped = false;
        if (connect() && tableName.trim().length() > 1) {
            String sql = "DROP TABLE " + tableName.trim() + (cascade ? " CASCADE CONSTRAINTS" : "");
            try (Statement statement = conn.createStatement()) {
                printSql(sql);
                droped = statement.execute(sql);
            } catch (SQLException e) {
                Flogger.atSevere().withCause(e).log(sql);
            } finally {
                close();
            }
        }
        return droped;
    }

    public boolean dropTable(String tableName) {
        return dropTable(tableName, true);
    }


    public int dropTables(List<String> tables) {
        int rows = 0;
        for (String table : tables) {
            if (dropTable(table))
                rows++;
        }
        return rows;
    }

    public boolean createRoutine(String sql) {
        boolean success = false;
        if (connect() && sql.trim().length() > 1) {
            try (Statement statement = conn.createStatement()) {
                printSql(sql);
                success = statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }
        return success;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SessionDB.class.getSimpleName() + "[", "]")
            .add("jdbcDriver='" + jdbcDriver + "'")
            .add("jdbcIP='" + jdbcIP + "'")
            .add("jdbcPort='" + jdbcPort + "'")
            .add("jdbcCatalog='" + jdbcCatalog + "'")
            .add("jdbcUser='" + jdbcUser + "'")
            .add("jdbcPassword='" + jdbcPassword + "'")
            .add("autoclose=" + autoclose)
            .toString();
    }
}

