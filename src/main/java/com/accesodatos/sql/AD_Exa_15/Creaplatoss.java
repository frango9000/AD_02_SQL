package com.accesodatos.sql.AD_Exa_15;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Creaplatoss {

    public static void main(String[] args) {
        // Object serialization
        String[] codes = {"p1", "p2"};
        String[] descricion = {"platocarnico1", "platocarnico2 "};

        writePlatos(codes, descricion);

        System.out.println("LE PLATOSS");
        readPlatos();
    }

    public static ArrayList<Platos> readPlatos() {
        ArrayList<Platos> platos = new ArrayList<>();
        try {
            Platos object2;
            FileInputStream fis = new FileInputStream(Exa15.RES_PATH + "platoss");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while ((object2 = (Platos) ois.readObject()) != null) {
                platos.add(object2);
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Exception during deserialization: " + e);
        }
        return platos;
    }

    private static void writePlatos(String[] codes, String[] descricion) {
        try {
            Platos pl = null;
            FileOutputStream fos = new FileOutputStream(Exa15.RES_PATH + "platoss");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (int i = 0; i < codes.length; i++) {
                pl = new Platos();
                pl.setCodigop(codes[i]);
                pl.setNomep(descricion[i]);
                System.out.println("object: " + pl);
                oos.writeObject(pl);
                oos.flush();

            }
            oos.writeObject(null);
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Exception during serialization: " + e);
        }
    }

}
