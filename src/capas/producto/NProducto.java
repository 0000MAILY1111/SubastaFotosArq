/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.producto;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class NProducto {

/**
 *
 * @author valde
 */

    private DProducto DatoProducto;

    public NProducto() {
        this.DatoProducto = new DProducto();
    }

    public void cargarDatos(Map<String, Object> data) {
        DatoProducto.cargarDatos(data);
    }

    public Object[] guardar() {
        return DatoProducto.guardar();
    }

    public Map<String, Object[]> listar() {
        return DatoProducto.listar();
    }

    public Object[] buscar(String id) {
        return DatoProducto.buscar(id);
    }

    public void eliminar(String id) {
        DatoProducto.eliminar(id);
    }
}
