/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subastafotosarq;

import capas.modelo.RolUsuario;
import capas.producto.NProducto;
import capas.puja.NPuja;
import capas.subasta.NSubasta;
import capas.usuario.NUsuario;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author valde
 */
public class SubastaFotosArq {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NUsuario nUsuario = new NUsuario();
        NProducto nProducto = new NProducto();
        NSubasta nSubasta = new NSubasta();
        NPuja nPuja = new NPuja(nSubasta);

        // 1) Usuarios (propietario + participantes)
        Object[] uProp = crearUsuario(nUsuario, "Ana Propietaria", "ana@demo.com", "123", RolUsuario.PROPIETARIO);
        Object[] u1 = crearUsuario(nUsuario, "Bruno", "bruno@demo.com", "123", RolUsuario.PARTICIPANTE);
        Object[] u2 = crearUsuario(nUsuario, "Carla", "carla@demo.com", "123", RolUsuario.PARTICIPANTE);

        System.out.println("Usuarios creados:");
        imprimirUsuario(uProp);
        imprimirUsuario(u1);
        imprimirUsuario(u2);
        System.out.println();

        // 2) Producto (foto) con precio base
        Map<String, Object> prod = new HashMap<>();
        prod.put("id_propietario", uProp[0]);
        prod.put("titulo", "Foto: Atardecer en la playa");
        prod.put("descripcion", "Fotografía original en alta resolución.");
        prod.put("url_foto", "assets/fotos/atardecer.jpg");
        prod.put("precio_base", 100.0);
        nProducto.cargarDatos(prod);
        Object[] producto = nProducto.guardar();

        System.out.println("Producto creado:");
        System.out.println("  id_producto=" + producto[0] + ", titulo=" + producto[2] + ", url_foto=" + producto[4] + ", precio_base=" + producto[5]);
        System.out.println();

        // 3) Subasta
        Map<String, Object> sub = new HashMap<>();
        sub.put("id_producto", producto[0]);
        sub.put("fecha_inicio", new Date());
        sub.put("fecha_fin", new Date(System.currentTimeMillis() + 300_000)); // 5 min
        sub.put("precio_inicial", 100.0);
        sub.put("precio_actual", 100.0);
        sub.put("estado_subasta", "PROGRAMADA");
        nSubasta.cargarDatos(sub);
        Object[] subasta = nSubasta.guardar();

        System.out.println("Subasta creada:");
        imprimirSubasta(subasta);

        // 4) Activar, registrar participantes
        nSubasta.activar(String.valueOf(subasta[0]));
        subasta = nSubasta.buscar(String.valueOf(subasta[0]));
        System.out.println("\nSubasta activada:");
        imprimirSubasta(subasta);

        nSubasta.registrarParticipante(String.valueOf(subasta[0]), String.valueOf(u1[0]));
        nSubasta.registrarParticipante(String.valueOf(subasta[0]), String.valueOf(u2[0]));

        System.out.println("\nParticipantes registrados: " + nSubasta.listarParticipantes(String.valueOf(subasta[0])).size());
        System.out.println();

        // 5) Pujas (visibles para todos: notificaciones)
        System.out.println("=== PUJAS ===");
        Object[] p1 = nPuja.hacerPuja(String.valueOf(subasta[0]), String.valueOf(u1[0]), 120.0);
        System.out.println("Puja 1: " + (p1 != null ? "OK" : "RECHAZADA"));

        Object[] p2 = nPuja.hacerPuja(String.valueOf(subasta[0]), String.valueOf(u2[0]), 150.0);
        System.out.println("Puja 2: " + (p2 != null ? "OK" : "RECHAZADA"));

        Object[] p3 = nPuja.hacerPuja(String.valueOf(subasta[0]), String.valueOf(u1[0]), 140.0);
        System.out.println("Puja 3 (menor que actual): " + (p3 != null ? "OK" : "RECHAZADA"));

        subasta = nSubasta.buscar(String.valueOf(subasta[0]));
        System.out.println("\nEstado final (antes de finalizar):");
        imprimirSubasta(subasta);

        // 6) Finalizar subasta
        nSubasta.finalizar(String.valueOf(subasta[0]));
        subasta = nSubasta.buscar(String.valueOf(subasta[0]));
        System.out.println("\nSubasta finalizada:");
        imprimirSubasta(subasta);
    }

    private static Object[] crearUsuario(NUsuario nUsuario, String nombre, String email, String password, RolUsuario rol) {
        Map<String, Object> u = new HashMap<>();
        u.put("nombre", nombre);
        u.put("email", email);
        u.put("password", password);
        u.put("rol", rol.name());
        u.put("fecha_registro", new Date());
        nUsuario.cargarDatos(u);
        return nUsuario.guardar();
    }

    private static void imprimirUsuario(Object[] u) {
        System.out.println("  id_usuario=" + u[0] + ", nombre=" + u[1] + ", email=" + u[2] + ", rol=" + u[4]);
    }

    private static void imprimirSubasta(Object[] s) {
        System.out.println("  id_subasta=" + s[0]
                + ", id_producto=" + s[1]
                + ", precio_inicial=" + s[4]
                + ", precio_actual=" + s[5]
                + ", estado=" + s[6]);
    }
    
}
