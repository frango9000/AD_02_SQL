package com.accesodatos.sql.AD_Exa_15b;

import com.accesodatos.sql.AD_Exa_15.Platos;
import java.util.HashMap;
import java.util.Map.Entry;

public class PlatoExtended extends Platos {

    public PlatoExtended() {
    }

    private HashMap<Componente, Integer> componentes = new HashMap<>();

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

}
