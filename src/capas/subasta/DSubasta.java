/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.subasta;

import capas.DataStore;
import capas.Database;
import capas.modelo.EstadoSubasta;
import java.util.Date;
import java.util.Map;

public class DSubasta {
    private Object idSubasta;
    private Object idProducto;
    private Date fechaInicio;
    private Date fechaFin;
    private double precioInicial;
    private double precioActual;
    private EstadoSubasta estadoSubasta;

    private final Database database;

    public DSubasta() {
        this.idSubasta = null;
        this.idProducto = null;
        this.fechaInicio = new Date();
        this.fechaFin = new Date(System.currentTimeMillis() + 60_000);
        this.precioInicial = 0;
        this.precioActual = 0;
        this.estadoSubasta = EstadoSubasta.PROGRAMADA;
        this.database = DataStore.getInstance().table("subasta");
    }

    public void cargarDatos(Map<String, Object> dato) {
        if (dato.containsKey("id_subasta")) this.idSubasta = dato.get("id_subasta");
        if (dato.containsKey("id_producto")) this.idProducto = dato.get("id_producto");
        if (dato.containsKey("precio_inicial")) this.precioInicial = Double.parseDouble(String.valueOf(dato.get("precio_inicial")));
        if (dato.containsKey("precio_actual")) this.precioActual = Double.parseDouble(String.valueOf(dato.get("precio_actual")));
        if (dato.containsKey("estado_subasta")) this.estadoSubasta = EstadoSubasta.valueOf(String.valueOf(dato.get("estado_subasta")));
        if (dato.containsKey("fecha_inicio") && dato.get("fecha_inicio") instanceof Date) this.fechaInicio = (Date) dato.get("fecha_inicio");
        if (dato.containsKey("fecha_fin") && dato.get("fecha_fin") instanceof Date) this.fechaFin = (Date) dato.get("fecha_fin");
    }

    public Object[] guardar() {
        Object[] data = { idSubasta, idProducto, fechaInicio, fechaFin, precioInicial, precioActual, estadoSubasta.name() };
        return database.store(data);
    }

    public Object[] buscar(String id) {
        return database.find(id);
    }

    public Map<String, Object[]> listar() {
        return database.list();
    }

    public Object[] actualizar(Object[] data) {
        return database.update(data);
    }
}
