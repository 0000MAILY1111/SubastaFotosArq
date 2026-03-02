/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.subasta;

import capas.modelo.EstadoParticipante;
import capas.modelo.EstadoSubasta;
import capas.participante.NParticipanteSubasta;
import capas.usuario.NUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author valde
 */
public class NSubasta {
    private final DSubasta datoSubasta;
    private final NParticipanteSubasta nParticipante;
    private final NUsuario nUsuario;

    private final Map<String, List<PujaListener>> listenersPorSubasta = new HashMap<>();

    public NSubasta() {
        this.datoSubasta = new DSubasta();
        this.nParticipante = new NParticipanteSubasta();
        this.nUsuario = new NUsuario();
    }

    public void cargarDatos(Map<String, Object> data) {
        datoSubasta.cargarDatos(data);
    }

    public Object[] guardar() {
        return datoSubasta.guardar();
    }

    public Object[] buscar(String id) {
        return datoSubasta.buscar(id);
    }

    public Map<String, Object[]> listar() {
        return datoSubasta.listar();
    }

    public Object[] actualizarSubasta(Object[] subasta) {
        if (subasta == null) return null;
        return datoSubasta.actualizar(subasta);
    }

    public Object[] activar(String idSubasta) {
        Object[] s = datoSubasta.buscar(idSubasta);
        if (s == null) return null;
        EstadoSubasta estado = EstadoSubasta.valueOf(String.valueOf(s[6]));
        if (estado != EstadoSubasta.PROGRAMADA) return s;
        s[6] = EstadoSubasta.ACTIVA.name();
        return datoSubasta.actualizar(s);
    }

    public Object[] finalizar(String idSubasta) {
        Object[] s = datoSubasta.buscar(idSubasta);
        if (s == null) return null;
        EstadoSubasta estado = EstadoSubasta.valueOf(String.valueOf(s[6]));
        if (estado != EstadoSubasta.ACTIVA) return s;
        s[6] = EstadoSubasta.FINALIZADA.name();
        return datoSubasta.actualizar(s);
    }

    public Object[] cancelar(String idSubasta) {
        Object[] s = datoSubasta.buscar(idSubasta);
        if (s == null) return null;
        s[6] = EstadoSubasta.CANCELADA.name();
        return datoSubasta.actualizar(s);
    }

    public Object[] registrarParticipante(String idSubasta, String idUsuario) {
        Map<String, Object> data = new HashMap<>();
        data.put("id_subasta", Integer.parseInt(idSubasta));
        data.put("id_usuario", Integer.parseInt(idUsuario));
        data.put("estado_participante", EstadoParticipante.ACTIVO.name());
        nParticipante.cargarDatos(data);
        Object[] fila = nParticipante.guardar();

        Object[] usuario = nUsuario.buscar(idUsuario);
        String nombre = (usuario != null) ? String.valueOf(usuario[1]) : ("Usuario#" + idUsuario);
        addListener(idSubasta, new ConsoleParticipanteListener(nombre));

        return fila;
    }

    public List<Object[]> listarParticipantes(String idSubasta) {
        List<Object[]> out = new ArrayList<>();
        for (Object[] row : nParticipante.listar().values()) {
            if (row != null && String.valueOf(row[1]).equals(idSubasta)) {
                out.add(row);
            }
        }
        return out;
    }

    public boolean esParticipanteActivo(String idSubasta, String idUsuario) {
        for (Object[] row : nParticipante.listar().values()) {
            if (row == null) continue;
            boolean match = String.valueOf(row[1]).equals(idSubasta) && String.valueOf(row[2]).equals(idUsuario);
            if (!match) continue;
            EstadoParticipante estado = EstadoParticipante.valueOf(String.valueOf(row[4]));
            return estado == EstadoParticipante.ACTIVO;
        }
        return false;
    }

    public void addListener(String idSubasta, PujaListener listener) {
        List<PujaListener> ls = listenersPorSubasta.get(idSubasta);
        if (ls == null) {
            ls = new ArrayList<>();
            listenersPorSubasta.put(idSubasta, ls);
        }
        ls.add(listener);
    }

    public void notificarNuevaPuja(String idSubasta, Object[] subasta, Object[] puja) {
        List<PujaListener> ls = listenersPorSubasta.get(idSubasta);
        if (ls == null) return;
        for (PujaListener l : ls) {
            l.onNuevaPuja(subasta, puja);
        }
    }
}
