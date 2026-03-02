package capas.usuario;

import java.util.Map;

public class NUsuario {
    private final DUsuario datoUsuario;

    public NUsuario() {
        this.datoUsuario = new DUsuario();
    }

    public void cargarDatos(Map<String, Object> data) {
        datoUsuario.cargarDatos(data);
    }

    public Object[] guardar() {
        return datoUsuario.guardar();
    }

    public Map<String, Object[]> listar() {
        return datoUsuario.listar();
    }

    public Object[] buscar(String id) {
        return datoUsuario.buscar(id);
    }

    public void eliminar(String id) {
        datoUsuario.eliminar(id);
    }
}

