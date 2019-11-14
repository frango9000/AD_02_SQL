package com.accesodatos.sql.AD_Exa_15b;

import com.accesodatos.sql.AD_Exa_15.Platos;
import com.accesodatos.sql.misc.SessionDB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Main15b {

    static final String RES_PATH = new File("").getAbsolutePath() + "/src/main/java/com/accesodatos/sql/AD_Exa_15b/";
    private static SessionDB sessionDB = SessionDB.getSession();


    public static void main(String[] args) {

        getPlatosXML().forEach(e -> System.out.println(e));

    }

    private static ArrayList<Platos> getPlatosXML() {
        ArrayList<Platos> platos = new ArrayList<>();
        XMLStreamReader in = null;
        try {
            in = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(RES_PATH + "platos.xml"));
            Platos plato = null;
            while (in.hasNext()) {
                if (in.getEventType() == XMLStreamConstants.START_ELEMENT) {
//                    System.out.println(in.getEventType() + " " + in.getLocalName());
                    if (in.getLocalName().equals("Plato")) {
                        plato = new Platos();
                        platos.add(plato);
                        plato.setCodigop(in.getAttributeValue(0));
                    } else if (in.getLocalName().equals("nomep")) {
                        plato.setNomep(in.getElementText());
                    }
                }
                in.next();
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SAX List OK");
        return platos;
    }

    public void findComponentes(PlatoExtended plato) {
        try (BufferedReader in = new BufferedReader(new FileReader(RES_PATH + "composicion.txt"))) {
            in.lines().forEach(action -> {
                String[] fields = action.split("#");
                if (fields[0].equals(plato.getCodigop()))
                    plato.getComponentes().put(new Componente(fields[1]), Integer.parseInt(fields[2]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findGrasaPercent(Componente componente) {
        if (sessionDB.connect()) {
            String sql = "select graxa from componentes where codc = ?";
            int grasa = 0;
            try (PreparedStatement ps = sessionDB.getConn().prepareStatement(sql)) {
                ps.setString(1, componente.getCodc());
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    componente.setGraxaPercent(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
