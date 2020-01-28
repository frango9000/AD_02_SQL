/*
exercicio30 : bdor5


desenvolver un proxecto java chmado bdor5 que cree os seguintes metodos traballando cas taboas o exercicio bdorexemploalumnos

a)metodo insireLinea que insira unha linea de pedido pasandolle como minimo os seguintes datos :
ordnum, linum,item,cantidad,descuento

probar a inserir unha linea de pedido para o pedido de numero de orden 4001 cos seguintes datos:
linum: 48
item : 2004
cantidad: 20
descuento: 10

b)
metodo modificaLinea  que modifique o nome dun cliente pasandolle como minimo o numero do cliente.
probar a modificar o nome del cliente 5 para que pase a chamarse'Alvaro Luna'

c) metodo borraLinea que pasandolle como minimo o numero de orde dun pedido e un numero de linea,
 borre dita linea de dito pedido probar a borrar a linea de pedido (linum)  48 do
 pedido cuxo ordnum e igual a 4001


 */
package com.accesodatos.sql.AD_30_bdor5;

import com.accesodatos.sql.misc.SessionDB;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main30 {

    public static void main(String[] args) {
        // 1
        //insireLinea(4001, 48, 2004, 20, 10);
        int a = insireLinea(4001, 48, 2004, 20, 10);
        System.out.println("Lineas insertadas: " + a);

        // 2
        int b = modificaLinea(5, "Alvaro Luna");
        System.out.println("Clientes modificados: " + b);

        //3
        int c = borrarLinea(4001, 48);
        System.out.println("Linea borrada: " + c);
    }

    public static int insireLinea(int numOrden, int linum, int item, int cantidad, int descuento) {
        int rows = 0;
        SessionDB sessionDB = SessionDB.getSession();
        if (sessionDB.connect()) {
            String sql = "INSERT INTO THE (" +
                         "    SELECT P.pedido" +
                         "    FROM pedido_tab P" +
                         "    WHERE P.ordnum = ?" +
                         "                )" +
                         "                  SELECT ?, REF(S), ?, ?" +
                         "                  FROM item_tab S" +
                         "                  WHERE S.itemnum = ?";
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setInt(1, numOrden);
                pstmt.setInt(2, linum);
                pstmt.setInt(3, cantidad);
                pstmt.setInt(4, descuento);
                pstmt.setInt(5, item);
                rows = pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    public static int modificaLinea(int numCliente, String nuevoNombre) {
        int rows = 0;
        SessionDB sessionDB = SessionDB.getSession();
        if (sessionDB.connect()) {
            String sql = "UPDATE cliente_tab SET clinomb = ? WHERE clinum = ?";
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setString(1, nuevoNombre);
                pstmt.setInt(2, numCliente);
                rows = pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    public static int borrarLinea(int numPedido, int numeroLinea) {
        int rows = 0;
        //
        SessionDB sessionDB = SessionDB.getSession();
        if (sessionDB.connect()) {
            String sql = "delete from the(select p.pedido from pedido_tab p where p.ordnum = ?) where linum = ?";
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setInt(1, numPedido);
                pstmt.setInt(2, numeroLinea);
                rows = pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }
}

