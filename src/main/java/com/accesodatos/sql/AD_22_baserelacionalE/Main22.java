/*
exercicio 22
baserelacionalE
Conectar como usuario hr á base de datos orcl e listar todos os nomes das columnas da taboa produtos
e introducir o ler o contido de tita taboa nun array de obxectos .to do isto  sen coñecemento previo do numero ,
contido e tipo das suas columnas.
Utilizar para elo un resulset xenerico e a clase ResulSetMetaData.
  notas:
 - getColumnType(i ) e un metodo ca clase ResulSetMetaData que devolta o  tipo de  columna (SQL) correspondente a
 columna (i) dun ResultSet
 - Types:   e a clase que define as constantes que son usadas para identificar typos de dato SQL genéricos , os
 chamados JDBC types . Esta clase non se pode instanciar, so consultar

 */
package com.accesodatos.sql.AD_22_baserelacionalE;

import com.accesodatos.sql.misc.SessionDB;
import com.accesodatos.sql.misc.data.AbstractDao;
import com.accesodatos.sql.misc.data.ProductosDao;
import com.accesodatos.sql.misc.model.Producto;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main22 {

    public static void main(String[] args) {
        System.out.println(AbstractDao.describeTable(SessionDB.getSession(), "productos"));

        ArrayList<Producto> productos = new ArrayList<>();
        int row = 0;
        for (Object[] arr : getTable()) {
            System.out.println("Row: " + row++);
            Producto p = new Producto();
            p.setCodigo((String) arr[0]);
            p.setDescripcion((String) arr[1]);
            p.setPrecio((Integer) arr[2]);
            productos.add(p);
        }

        productos.forEach(System.out::println);
    }

    private static Object[][] getTable() {
        Object[][] table = null;
        if (SessionDB.getSession().connect()) {
            String sql = "SELECT * FROM productos";
            try (Statement ps = SessionDB.getSession().getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                                 ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = ps.executeQuery(sql)) {
                ResultSetMetaData metaData = rs.getMetaData();
                int rows = 0;
                if (rs.last()) {
                    rows = rs.getRow();
                    rs.beforeFirst();
                }
                int cols = metaData.getColumnCount();
                System.out.println("Rows: " + rows + "\nCols: " + cols);
                table = new Object[rows][cols];

                int row = 0;
                while (rs.next()) {
                    for (int col = 0; col < cols; col++) {
                        int type = metaData.getColumnType(col + 1);
                        if (type == JDBCType.VARCHAR.getVendorTypeNumber()) {
                            table[row][col] = rs.getString(col + 1);
                        } else if (type == JDBCType.NUMERIC.getVendorTypeNumber()) {
                            table[row][col] = rs.getInt(col + 1);
                        }
                    }
                    row++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                SessionDB.getSession().close();
            }
        }
        return table;
    }
}
