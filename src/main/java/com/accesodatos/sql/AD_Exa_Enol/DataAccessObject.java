package com.accesodatos.sql.AD_Exa_Enol;

import com.accesodatos.sql.misc.Globals;
import com.accesodatos.sql.misc.SessionDB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccessObject {

    public static final String ANALISIS_TXT = new File("").getAbsolutePath() + "/src/main/java/com/accesodatos/sql/AD_Exa_Enol/analisis.txt";
    private static DataAccessObject instance;

    private SessionDB sessionDB = SessionDB.getSession();

    private DataAccessObject() {
    }

    public static DataAccessObject getDAO() {
        if (instance == null) {
            synchronized (DataAccessObject.class) {
                if (instance == null) {
                    instance = new DataAccessObject();
                }
            }
        }
        return instance;
    }


    public ArrayList<Analisis> getAnalisisList() {
        ArrayList<Analisis> lista = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(ANALISIS_TXT))) {
            in.lines().forEach(action -> {
                String[] fields = action.split(",");
                lista.add(Analisis.build(fields));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Cliente getCliente(String dni) {
        Cliente cliente = null;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT * FROM clientes WHERE dni = '%s'", dni);
            try (Statement ps = sessionDB.getConn().createStatement();
                 ResultSet rs = ps.executeQuery(sql)) {
                if (rs.next()) {
                    cliente = Cliente.build(rs);
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return cliente;
    }

    public Uva getUva(char tipo) {
        Uva uva = null;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT * FROM uvas WHERE tipo = '%s'", tipo);
            try (Statement ps = sessionDB.getConn().createStatement();
                 ResultSet rs = ps.executeQuery(sql)) {
                if (rs.next()) {
                    uva = Uva.build(rs);
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return uva;
    }

    public int insertXerado(Xerado xerado) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = "INSERT INTO xerado VALUES(?, ?, ?, ?)";
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setString(1, xerado.getNum());
                pstmt.setString(2, xerado.getNomeuva());
                pstmt.setString(3, xerado.getTrataacidez());
                pstmt.setInt(4, xerado.getTotal());
                rows = pstmt.executeUpdate();
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, sql, ex);
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

    public int updateCliente(Cliente cliente) {

        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("UPDATE clientes SET nome = ?, telf = ?, numerodeanalisis = ? WHERE dni = ?");
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setString(1, cliente.getNome());
                pstmt.setInt(2, cliente.getTelf());
                pstmt.setInt(3, cliente.getNumerodeanalisis());
                pstmt.setString(4, cliente.getDni());
                rows = pstmt.executeUpdate();
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;

    }
}
