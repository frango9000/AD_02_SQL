package com.accesodatos.sql.AD_Exa_15b;

import com.accesodatos.sql.AD_Exa_15.Platos;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class PlatoExtended extends Platos implements Serializable {

    private HashMap<Componente, Integer> componentes = new HashMap<>();

    public PlatoExtended() {
    }

    public PlatoExtended(String codigo, String nome) {
        super(codigo, nome);
    }

    public HashMap<Componente, Integer> getComponentes() {
        return componentes;
    }

    public int getGrasaTotal() {
        int grasaTotal = 0;
        for (Entry<Componente, Integer> entry : getComponentes().entrySet()) {
            grasaTotal += (entry.getKey().getGraxaPercent() * entry.getValue()) / 100;
        }
        return grasaTotal;
    }

    public String toString() {
        return "codigo plato : " + getCodigop() + "\n" +
               "nome plato  : " + getNomep() + "\n" +
               "grasaTotal : " + getGrasaTotal() + "\n" +
               "componentes : " + componentes.toString();
    }

}
