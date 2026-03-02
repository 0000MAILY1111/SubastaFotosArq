package capas.participante;

import java.util.Map;

public class NParticipanteSubasta {
    private final DParticipanteSubasta dato;

    public NParticipanteSubasta() {
        this.dato = new DParticipanteSubasta();
    }

    public void cargarDatos(Map<String, Object> data) {
        dato.cargarDatos(data);
    }

    public Object[] guardar() {
        return dato.guardar();
    }

    public Map<String, Object[]> listar() {
        return dato.listar();
    }
}

