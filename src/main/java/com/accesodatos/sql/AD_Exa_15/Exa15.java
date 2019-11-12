package com.accesodatos.sql.AD_Exa_15;

import com.accesodatos.sql.misc.SessionDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class Exa15 {

    static final String RES_PATH = new File("").getAbsolutePath() + "/src/main/java/com/accesodatos/sql/AD_Exa_15/";
    private static SessionDB sessionDB = SessionDB.getSession();

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, XMLStreamException {
        //codigo aqui
        HashSet<PlatoGraso> platosGrasos = new HashSet<>();
        try {
            Platos plato;
            FileInputStream fis = new FileInputStream(Exa15.RES_PATH + "platoss");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while ((plato = (Platos) ois.readObject()) != null) {
                HashMap<String, Integer> composicion = new HashMap<>();
                if (sessionDB.connect()) {
                    System.out.println(plato.toString());
                    String sql = "select * from composicion where codp = ?";
                    try (PreparedStatement ps = sessionDB.getConn().prepareStatement(sql)) {
                        ps.setString(1, plato.getCodigop());
                        ResultSet resultSet = ps.executeQuery();
                        while (resultSet.next()) {
                            composicion.put(resultSet.getString(2), resultSet.getInt(3));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                int grasatotal = 0;
                for (Entry<String, Integer> entry : composicion.entrySet()) {
                    String codc = entry.getKey();
                    int peso = entry.getValue();
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
                }
                PlatoGraso platoGraso = new PlatoGraso(plato.getCodigop(), plato.getNomep(), grasatotal);
                platosGrasos.add(platoGraso);
                System.out.println("Total en grasas del plato: " + grasatotal);
                System.out.println();
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Exception during deserialization: " + e);
        }
        sessionDB.close();
        writePlatosGrasosSAX(platosGrasos);
    }

    private static void writePlatosGrasosSAX(HashSet<PlatoGraso> platoGrasos) {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter out = null;
        try {
            out = xmlOutputFactory.createXMLStreamWriter(new FileWriter(RES_PATH + "platosgrasos.xml"));
            out.writeStartDocument("1.0");
            out.writeStartElement("platos");
            for (PlatoGraso platoGraso : platoGrasos) {
                writePlatoSAX(platoGraso, out);
            }
            out.writeEndElement();
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    System.out.println("Platos grasos guardados en XML");
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writePlatoSAX(PlatoGraso platoGraso, XMLStreamWriter out) throws XMLStreamException {
        out.writeStartElement("plato");
        out.writeAttribute("codigop", platoGraso.getCodigop());
        out.writeStartElement("nomep");
        out.writeCharacters(platoGraso.getNomep());
        out.writeEndElement();
        out.writeStartElement("graxatotal");
        out.writeCharacters(Integer.toString(platoGraso.getGrasa()));
        out.writeEndElement();
        out.writeEndElement();
    }
}

   