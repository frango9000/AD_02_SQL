package com.accesodatos.sql.AD_Exa_15;

import com.accesodatos.sql.misc.SessionDB;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.stream.XMLStreamException;


public class Exa15 {

    public static Connection conexion = null;
    protected static SessionDB sessionDB = SessionDB.getSession();

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }


    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, XMLStreamException {
        //codigo aqui

        ArrayList<Platos> platos = Creaplatoss.readPlatos();

        platos.forEach(v -> {
            HashMap<String, Integer> composicion = new HashMap<>();
            System.out.println(v.toString());
            if (sessionDB.connect()) {
                String sql = "select * from composicion where codp = ?";
                try (PreparedStatement ps = sessionDB.getConn().prepareStatement(sql)) {
                    ps.setString(1, v.getCodigop());
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        composicion.put(resultSet.getString(2), resultSet.getInt(3));

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            final int grasatotal = 0;
            composicion.forEach((codc, peso) -> {
                System.out.println("Codigo componente: " + codc);

                String sql = "select graxa from componentes where codc = ?";
                int grasa = 0;
                try (PreparedStatement ps = sessionDB.getConn().prepareStatement(sql)) {
                    ps.setString(1, codc);
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        grasa = resultSet.getInt(1);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("Grasa cada 100g " + grasa);
                System.out.println("Peso " + peso);
                int grasaparcial = ((grasa * peso) / 100);
                System.out.println("Total de grasa del componente " + grasaparcial);
                grasatotal += grasaparcial;


            });
            System.out.println();

        });

    }


}

   