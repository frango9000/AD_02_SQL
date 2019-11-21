package com.accesodatos.sql.AD_Exa_Enol;

public class Enoloxia {

    public static void main(String[] args) {
        DataAccessObject dao = DataAccessObject.getDAO();
        dao.getAnalisisList().forEach(analisis -> {
            analisis.findUva();
            analisis.findCliente();
            analisis.getCliente().incrementarNumerodeanalisis();
            dao.insertXerado(analisis.produceXerado());
            dao.updateCliente(analisis.getCliente());
            System.out.println(analisis);
        });
    }

}

