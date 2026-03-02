/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas.puja;

import capas.modelo.EstadoPuja;
import capas.modelo.EstadoSubasta;
import capas.subasta.NSubasta;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author valde
 */
public class NPuja {
    private final DPuja datoPuja;
    private final NSubasta nSubasta;

    public NPuja(NSubasta nSubasta) {
        this.datoPuja = new DPuja();
        this.nSubasta = nSubasta;
    }

    public Map<String, Object[]> listar() {
        return datoPuja.listar();
    }

    public Object[] hacerPuja(String idSubasta, String idUsuario, double monto) {
        Object[] subasta = nSubasta.buscar(idSubasta);
        if (subasta == null) return null;

        EstadoSubasta estado = EstadoSubasta.valueOf(String.valueOf(subasta[6]));
        if (estado != EstadoSubasta.ACTIVA) return null;

        if (!nSubasta.esParticipanteActivo(idSubasta, idUsuario)) return null;

        double precioActual = Double.parseDouble(String.valueOf(subasta[5]));
        double precioInicial = Double.parseDouble(String.valueOf(subasta[4]));
        double piso = Math.max(precioActual, precioInicial);
        if (monto <= piso) return null;

        for (Object[] p : datoPuja.listar().values()) {
            if (p == null) continue;
            if (!String.valueOf(p[1]).equals(idSubasta)) continue;
            EstadoPuja ep = EstadoPuja.valueOf(String.valueOf(p[5]));
            if (ep == EstadoPuja.GANADORA || ep == EstadoPuja.PENDIENTE) {
                p[5] = EstadoPuja.SUPERADA.name();
                datoPuja.actualizar(p);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id_subasta", Integer.parseInt(idSubasta));
        data.put("id_usuario", Integer.parseInt(idUsuario));
        data.put("monto", monto);
        data.put("estado_puja", EstadoPuja.GANADORA.name());
        datoPuja.cargarDatos(data);
        Object[] puja = datoPuja.guardar();

        subasta[5] = monto;
        nSubasta.actualizarSubasta(subasta);

        nSubasta.notificarNuevaPuja(idSubasta, subasta, puja);
        return puja;
    }
}
