/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.producto;

import capas.DataStore;
import capas.Database;
import java.util.Date;
import java.util.Map;

public class DProducto {
    private Object idProducto;
    private Object idPropietario;
    private String titulo;
    private String descripcion;
    private String urlFoto;
    private double precioBase;
    private Date fechaCreacion;

    private final Database database;

    public DProducto() {
        this.idProducto = null;
        this.idPropietario = null;
        this.titulo = "";
        this.descripcion = "";
        this.urlFoto = "";
        this.precioBase = 0;
        this.fechaCreacion = new Date();
        this.database = DataStore.getInstance().table("producto");
    }

    public void cargarDatos(Map<String, Object> dato) {
        if (dato.containsKey("id_producto")) this.idProducto = dato.get("id_producto");
        if (dato.containsKey("id_propietario")) this.idPropietario = dato.get("id_propietario");
        if (dato.containsKey("titulo")) this.titulo = String.valueOf(dato.get("titulo"));
        if (dato.containsKey("descripcion")) this.descripcion = String.valueOf(dato.get("descripcion"));
        if (dato.containsKey("url_foto")) this.urlFoto = String.valueOf(dato.get("url_foto"));
        if (dato.containsKey("precio_base")) this.precioBase = Double.parseDouble(String.valueOf(dato.get("precio_base")));
        if (dato.containsKey("fecha_creacion") && dato.get("fecha_creacion") instanceof Date) {
            this.fechaCreacion = (Date) dato.get("fecha_creacion");
        }
    }

    public Object[] guardar() {
        Object[] data = { idProducto, idPropietario, titulo, descripcion, urlFoto, precioBase, fechaCreacion };
        return database.store(data);
    }

    public void eliminar(String id) {
        database.delete(id);
    }

    public Object[] buscar(String id) {
        return database.find(id);
    }

    public Map<String, Object[]> listar() {
        return database.list();
    }
}