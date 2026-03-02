/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.puja;

import capas.DataStore;
import capas.Database;
import capas.modelo.EstadoPuja;
import java.util.Date;
import java.util.Map;

public class DPuja {
    private Object idPuja;
    private Object idSubasta;
    private Object idUsuario;
    private double monto;
    private Date fechaHora;
    private EstadoPuja estadoPuja;

    private final Database database;

    public DPuja() {
        this.idPuja = null;
        this.idSubasta = null;
        this.idUsuario = null;
        this.monto = 0;
        this.fechaHora = new Date();
        this.estadoPuja = EstadoPuja.PENDIENTE;
        this.database = DataStore.getInstance().table("puja");
    }

    public void cargarDatos(Map<String, Object> dato) {
        if (dato.containsKey("id_puja")) this.idPuja = dato.get("id_puja");
        if (dato.containsKey("id_subasta")) this.idSubasta = dato.get("id_subasta");
        if (dato.containsKey("id_usuario")) this.idUsuario = dato.get("id_usuario");
        if (dato.containsKey("monto")) this.monto = Double.parseDouble(String.valueOf(dato.get("monto")));
        if (dato.containsKey("estado_puja")) this.estadoPuja = EstadoPuja.valueOf(String.valueOf(dato.get("estado_puja")));
        if (dato.containsKey("fecha_hora") && dato.get("fecha_hora") instanceof Date) this.fechaHora = (Date) dato.get("fecha_hora");
    }

    public Object[] guardar() {
        Object[] data = { idPuja, idSubasta, idUsuario, monto, fechaHora, estadoPuja.name() };
        return database.store(data);
    }

    public Object[] actualizar(Object[] data) {
        return database.update(data);
    }

    public Map<String, Object[]> listar() {
        return database.list();
    }
}
