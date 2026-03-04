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

import java.util.*;

/**
 * Sistema de Subastas de Fotos - Versión Refactorizada
 * @author valdez
 */
public class SubastaFotosArq {

    // ─────────────────────────────────────────────
    //  CONSTANTES
    // ─────────────────────────────────────────────
    private static final int ANCHO_PANTALLA = 42;
    private static final String SEPARADOR = "==========================================";
    private static final String SEPARADOR_DELGADO = "------------------------------------------";
    
    // Opciones de menú principal
    private static final int OP_USUARIOS = 1;
    private static final int OP_PRODUCTOS = 2;
    private static final int OP_SUBASTAS = 3;
    private static final int OP_PUJAS = 4;
    private static final int OP_SALIR = 0;
    
    // Estados de subasta
    private static final String ESTADO_PROGRAMADA = "PROGRAMADA";
    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_FINALIZADA = "FINALIZADA";
    
    // Roles
    private static final String ROL_PROPIETARIO = "PROPIETARIO";
    private static final String ROL_PARTICIPANTE = "PARTICIPANTE";

    // ─────────────────────────────────────────────
    //  COMPONENTES DEL SISTEMA
    // ─────────────────────────────────────────────
    private static final Scanner sc = new Scanner(System.in);
    private static final NUsuario nUsuario = new NUsuario();
    private static final NProducto nProducto = new NProducto();
    private static final NSubasta nSubasta = new NSubasta();
    private static final NPuja nPuja = new NPuja(nSubasta);

    // Cache en memoria (simula persistencia)
    private static final List<Object[]> cacheUsuarios = new ArrayList<>();
    private static final List<Object[]> cacheProductos = new ArrayList<>();
    private static final List<Object[]> cacheSubastas = new ArrayList<>();

    // ─────────────────────────────────────────────
    //  PUNTO DE ENTRADA
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        mostrarEncabezado("SISTEMA DE SUBASTAS DE FOTOS");
        ejecutarMenuPrincipal();
        mostrarDespedida();
        sc.close();
    }

    private static void ejecutarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerEnteroSeguro();
            
            switch (opcion) {
                case OP_USUARIOS:
                    ejecutarMenuUsuarios();
                    break;
                case OP_PRODUCTOS:
                    ejecutarMenuProductos();
                    break;
                case OP_SUBASTAS:
                    ejecutarMenuSubastas();
                    break;
                case OP_PUJAS:
                    ejecutarMenuPujas();
                    break;
                case OP_SALIR:
                    continuar = false;
                    break;
                default:
                    System.out.println("  ⚠ Opción no válida. Intente nuevamente.");
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔" + repetirCaracter('═', ANCHO_PANTALLA - 2) + "╗");
        System.out.println("║" + centrarTexto("MENÚ PRINCIPAL", ANCHO_PANTALLA - 2) + "║");
        System.out.println("╠" + repetirCaracter('═', ANCHO_PANTALLA - 2) + "╣");
        System.out.println("║  " + formatearOpcion("1. Gestión de Usuarios") + "║");
        System.out.println("║  " + formatearOpcion("2. Gestión de Productos") + "║");
        System.out.println("║  " + formatearOpcion("3. Gestión de Subastas") + "║");
        System.out.println("║  " + formatearOpcion("4. Realizar Puja") + "║");
        System.out.println("║  " + formatearOpcion("0. Salir") + "║");
        System.out.println("╚" + repetirCaracter('═', ANCHO_PANTALLA - 2) + "╝");
        System.out.print("\n  Seleccione una opción: ");
    }

    // ─────────────────────────────────────────────
    //  MENÚ: USUARIOS
    // ─────────────────────────────────────────────
    private static void ejecutarMenuUsuarios() {
        boolean volver = false;
        
        while (!volver) {
            mostrarSubMenu("GESTIÓN DE USUARIOS", 
                "1. Crear usuario", 
                "2. Listar usuarios", 
                "0. Volver");
            
            switch (leerEnteroSeguro()) {
                case 1: crearUsuarioInteractivo(); break;
                case 2: listarUsuarios(); break;
                case 0: volver = true; break;
                default: System.out.println("  ⚠ Opción inválida.");
            }
        }
    }

    private static void crearUsuarioInteractivo() {
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📝 NUEVO USUARIO");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        String nombre = leerTexto("Nombre");
        String email = leerTexto("Email");
        String password = leerTexto("Password");
        RolUsuario rol = seleccionarRol();
        
        // Construir datos
        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("email", email);
        datos.put("password", password);
        datos.put("rol", rol.name());
        datos.put("fecha_registro", new Date());
        
        // Persistir
        nUsuario.cargarDatos(datos);
        Object[] resultado = nUsuario.guardar();
        
        if (resultado != null) {
            cacheUsuarios.add(resultado);
            System.out.println("\n  ✅ Usuario creado exitosamente:");
            imprimirUsuario(resultado);
        } else {
            System.out.println("\n  ❌ Error al crear el usuario.");
        }
    }

    private static RolUsuario seleccionarRol() {
        RolUsuario rol = null;
        while (rol == null) {
            System.out.print("  Rol (1=PROPIETARIO / 2=PARTICIPANTE): ");
            int opcion = leerEnteroSeguro();
            if (opcion == 1) {
                rol = RolUsuario.PROPIETARIO;
            } else if (opcion == 2) {
                rol = RolUsuario.PARTICIPANTE;
            } else {
                System.out.println("  ⚠ Ingrese 1 o 2.");
            }
        }
        return rol;
    }

    private static void listarUsuarios() {
        if (cacheUsuarios.isEmpty()) {
            System.out.println("\n  ℹ No hay usuarios registrados.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📋 LISTA DE USUARIOS");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        for (Object[] usuario : cacheUsuarios) {
            imprimirUsuario(usuario);
        }
    }

    // ─────────────────────────────────────────────
    //  MENÚ: PRODUCTOS
    // ─────────────────────────────────────────────
    private static void ejecutarMenuProductos() {
        boolean volver = false;
        
        while (!volver) {
            mostrarSubMenu("GESTIÓN DE PRODUCTOS",
                "1. Crear producto",
                "2. Listar productos", 
                "0. Volver");
            
            switch (leerEnteroSeguro()) {
                case 1: crearProductoInteractivo(); break;
                case 2: listarProductos(); break;
                case 0: volver = true; break;
                default: System.out.println("  ⚠ Opción inválida.");
            }
        }
    }

    private static void crearProductoInteractivo() {
        List<Object[]> propietarios = filtrarUsuariosPorRol(ROL_PROPIETARIO);
        
        if (propietarios.isEmpty()) {
            System.out.println("\n  ⚠ Primero debe registrar un usuario con rol PROPIETARIO.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📝 NUEVO PRODUCTO");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        System.out.println("\n  Propietarios disponibles:");
        for (Object[] u : propietarios) {
            System.out.printf("    ▶ [%s] %s%n", u[0], u[1]);
        }
        
        String idPropietario = leerTexto("ID del propietario");
        String titulo = leerTexto("Título de la foto");
        String descripcion = leerTexto("Descripción");
        String urlFoto = leerTexto("URL / ruta de la foto");
        double precioBase = leerDoubleSeguro("Precio base");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("id_propietario", idPropietario);
        datos.put("titulo", titulo);
        datos.put("descripcion", descripcion);
        datos.put("url_foto", urlFoto);
        datos.put("precio_base", precioBase);
        
        nProducto.cargarDatos(datos);
        Object[] resultado = nProducto.guardar();
        
        if (resultado != null) {
            cacheProductos.add(resultado);
            System.out.println("\n  ✅ Producto creado:");
            imprimirProducto(resultado);
        } else {
            System.out.println("\n  ❌ Error al crear el producto.");
        }
    }

    private static void listarProductos() {
        if (cacheProductos.isEmpty()) {
            System.out.println("\n  ℹ No hay productos registrados.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📋 LISTA DE PRODUCTOS");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        for (Object[] producto : cacheProductos) {
            imprimirProducto(producto);
        }
    }

    // ─────────────────────────────────────────────
    //  MENÚ: SUBASTAS
    // ─────────────────────────────────────────────
    private static void ejecutarMenuSubastas() {
        boolean volver = false;
        
        while (!volver) {
            mostrarSubMenu("GESTIÓN DE SUBASTAS",
                "1. Crear subasta",
                "2. Activar subasta",
                "3. Registrar participante",
                "4. Ver participantes",
                "5. Finalizar subasta",
                "6. Listar subastas",
                "0. Volver");
            
            switch (leerEnteroSeguro()) {
                case 1: crearSubastaInteractiva(); break;
                case 2: activarSubasta(); break;
                case 3: registrarParticipante(); break;
                case 4: verParticipantes(); break;
                case 5: finalizarSubasta(); break;
                case 6: listarSubastas(); break;
                case 0: volver = true; break;
                default: System.out.println("  ⚠ Opción inválida.");
            }
        }
    }

    private static void crearSubastaInteractiva() {
        if (cacheProductos.isEmpty()) {
            System.out.println("\n  ⚠ Primero debe registrar un producto.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📝 NUEVA SUBASTA");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        System.out.println("\n  Productos disponibles:");
        for (Object[] p : cacheProductos) {
            System.out.printf("    ▶ [%s] %s — Base: $%.2f%n", p[0], p[2], p[5]);
        }
        
        String idProducto = leerTexto("ID del producto");
        int duracionMin = leerEnteroPositivo("Duración en minutos");
        double precioInicial = leerDoublePositivo("Precio inicial");
        
        Date fechaInicio = new Date();
        Date fechaFin = new Date(fechaInicio.getTime() + (long) duracionMin * 60_000);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("id_producto", idProducto);
        datos.put("fecha_inicio", fechaInicio);
        datos.put("fecha_fin", fechaFin);
        datos.put("precio_inicial", precioInicial);
        datos.put("precio_actual", precioInicial);
        datos.put("estado_subasta", ESTADO_PROGRAMADA);
        
        nSubasta.cargarDatos(datos);
        Object[] resultado = nSubasta.guardar();
        
        if (resultado != null) {
            cacheSubastas.add(resultado);
            System.out.println("\n  ✅ Subasta creada:");
            imprimirSubasta(resultado);
        } else {
            System.out.println("\n  ❌ Error al crear la subasta.");
        }
    }

    private static void activarSubasta() {
        String id = solicitarIdSubasta();
        if (id == null) return;
        
        nSubasta.activar(id);
        sincronizarSubastaEnCache(id);
        System.out.println("  ✅ Subasta #" + id + " activada correctamente.");
        
        Object[] actualizada = nSubasta.buscar(id);
        if (actualizada != null) imprimirSubasta(actualizada);
    }

    private static void registrarParticipante() {
        String idSubasta = solicitarIdSubasta();
        if (idSubasta == null) return;
        
        List<Object[]> participantes = filtrarUsuariosPorRol(ROL_PARTICIPANTE);
        if (participantes.isEmpty()) {
            System.out.println("  ⚠ No hay participantes registrados.");
            return;
        }
        
        System.out.println("\n  Participantes disponibles:");
        for (Object[] u : participantes) {
            System.out.printf("    ▶ [%s] %s%n", u[0], u[1]);
        }
        
        String idParticipante = leerTexto("ID del participante");
        nSubasta.registrarParticipante(idSubasta, idParticipante);
        System.out.println("  ✅ Participante #" + idParticipante + 
                          " registrado en subasta #" + idSubasta);
    }

    private static void verParticipantes() {
        String id = solicitarIdSubasta();
        if (id == null) return;
        
        List<?> participantes = nSubasta.listarParticipantes(id);
        System.out.println("\n  Participantes en subasta #" + id + ": " + participantes.size());
        
        if (participantes.isEmpty()) {
            System.out.println("    ℹ Sin participantes aún.");
        } else {
            for (Object p : participantes) {
                System.out.println("    ▶ " + p);
            }
        }
    }

    private static void finalizarSubasta() {
        String id = solicitarIdSubasta();
        if (id == null) return;
        
        nSubasta.finalizar(id);
        sincronizarSubastaEnCache(id);
        System.out.println("  ✅ Subasta #" + id + " finalizada.");
        
        Object[] actualizada = nSubasta.buscar(id);
        if (actualizada != null) imprimirSubasta(actualizada);
    }

    private static void listarSubastas() {
        if (cacheSubastas.isEmpty()) {
            System.out.println("\n  ℹ No hay subastas registradas.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  📋 LISTA DE SUBASTAS");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        for (Object[] subasta : cacheSubastas) {
            String id = String.valueOf(subasta[0]);
            Object[] actualizada = nSubasta.buscar(id);
            imprimirSubasta(actualizada != null ? actualizada : subasta);
        }
    }

    // ─────────────────────────────────────────────
    //  MENÚ: PUJAS
    // ─────────────────────────────────────────────
    private static void ejecutarMenuPujas() {
        List<Object[]> subastasActivas = obtenerSubastasActivas();
        
        if (subastasActivas.isEmpty()) {
            System.out.println("\n  ⚠ No hay subastas en estado ACTIVA.");
            return;
        }
        
        System.out.println("\n  " + SEPARADOR_DELGADO);
        System.out.println("  💰 REALIZAR PUJA");
        System.out.println("  " + SEPARADOR_DELGADO);
        
        System.out.println("\n  Subastas activas disponibles:");
        for (Object[] s : subastasActivas) {
            imprimirSubasta(s);
        }
        
        String idSubasta = leerTexto("ID de la subasta");
        String idPujador = seleccionarPujador();
        double monto = leerDoublePositivo("Monto de la puja");
        
        Object[] resultado = nPuja.hacerPuja(idSubasta, idPujador, monto);
        
        if (resultado != null) {
            System.out.printf("%n  ✅ PUJA ACEPTADA — Nuevo precio: $%.2f%n", monto);
            // Actualizar cache si es necesario
            sincronizarSubastaEnCache(idSubasta);
        } else {
            System.out.println("\n  ❌ PUJA RECHAZADA — El monto debe ser mayor al precio actual.");
        }
    }

    private static List<Object[]> obtenerSubastasActivas() {
        List<Object[]> activas = new ArrayList<>();
        for (Object[] s : cacheSubastas) {
            String id = String.valueOf(s[0]);
            Object[] actual = nSubasta.buscar(id);
            if (actual != null && ESTADO_ACTIVA.equals(actual[6])) {
                activas.add(actual);
            }
        }
        return activas;
    }

    private static String seleccionarPujador() {
        List<Object[]> participantes = filtrarUsuariosPorRol(ROL_PARTICIPANTE);
        if (participantes.isEmpty()) {
            throw new IllegalStateException("No hay participantes registrados.");
        }
        
        System.out.println("\n  Participantes disponibles:");
        for (Object[] u : participantes) {
            System.out.printf("    ▶ [%s] %s%n", u[0], u[1]);
        }
        return leerTexto("ID del pujador");
    }

    // ─────────────────────────────────────────────
    //  UTILIDADES DE ENTRADA/SALIDA
    // ─────────────────────────────────────────────
    private static String leerTexto(String etiqueta) {
        System.out.print("  " + etiqueta + ": ");
        return sc.nextLine().trim();
    }

    private static int leerEnteroSeguro() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  ⚠ Ingrese un número entero válido: ");
            }
        }
    }

    private static int leerEnteroPositivo(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            int valor = leerEnteroSeguro();
            if (valor > 0) return valor;
            System.out.println("  ⚠ El valor debe ser positivo.");
        }
    }

    private static double leerDoubleSeguro(String etiqueta) {
        System.out.print("  " + etiqueta + ": ");
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  ⚠ Ingrese un número válido (ej: 150.50): ");
            }
        }
    }

    private static double leerDoublePositivo(String etiqueta) {
        while (true) {
            double valor = leerDoubleSeguro(etiqueta);
            if (valor > 0) return valor;
            System.out.println("  ⚠ El monto debe ser mayor a cero.");
        }
    }

    // ─────────────────────────────────────────────
    //  UTILIDADES DE FORMATO Y UI
    // ─────────────────────────────────────────────
    private static void mostrarEncabezado(String titulo) {
        System.out.println(SEPARADOR);
        System.out.println(centrarTexto(titulo, ANCHO_PANTALLA));
        System.out.println(SEPARADOR);
    }

    private static void mostrarDespedida() {
        System.out.println("\n" + SEPARADOR);
        System.out.println(centrarTexto("👋 ¡Gracias por usar el sistema!", ANCHO_PANTALLA));
        System.out.println(SEPARADOR + "\n");
    }

    private static void mostrarSubMenu(String titulo, String... opciones) {
        System.out.println("\n┌" + repetirCaracter('─', ANCHO_PANTALLA - 2) + "┐");
        System.out.println("│" + centrarTexto(titulo, ANCHO_PANTALLA - 2) + "│");
        System.out.println("├" + repetirCaracter('─', ANCHO_PANTALLA - 2) + "┤");
        for (String opcion : opciones) {
            System.out.println("│  " + formatearOpcion(opcion) + "│");
        }
        System.out.println("└" + repetirCaracter('─', ANCHO_PANTALLA - 2) + "┘");
        System.out.print("  Opción: ");
    }

    /** Reemplazo compatible con Java 8 para String.repeat() */
    private static String repetirCaracter(char c, int veces) {
        if (veces <= 0) return "";
        StringBuilder sb = new StringBuilder(veces);
        for (int i = 0; i < veces; i++) sb.append(c);
        return sb.toString();
    }

    private static String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int padding = (ancho - texto.length()) / 2;
        return repetirCaracter(' ', padding) + texto;
    }

    private static String formatearOpcion(String texto) {
        int espacioRestante = ANCHO_PANTALLA - 4 - texto.length();
        return texto + repetirCaracter(' ', Math.max(0, espacioRestante));
    }

    // ─────────────────────────────────────────────
    //  IMPRESIÓN DE ENTIDADES
    // ─────────────────────────────────────────────
    private static void imprimirUsuario(Object[] u) {
        System.out.printf("  ▶ ID: %-4s | %-20s | %-25s | Rol: %s%n",
            u[0], u[1], u[2], u[4]);
    }

    private static void imprimirProducto(Object[] p) {
        System.out.printf("  ▶ ID: %-4s | %-30s | Base: $%.2f%n",
            p[0], p[2], p[5]);
    }

    private static void imprimirSubasta(Object[] s) {
        System.out.printf("  ▶ ID: %-4s | Estado: %-10s | Precio: $%.2f%n",
            s[0], s[6], s[5]);
    }

    // ─────────────────────────────────────────────
    //  UTILIDADES DE NEGOCIO
    // ─────────────────────────────────────────────
    private static String solicitarIdSubasta() {
        if (cacheSubastas.isEmpty()) {
            System.out.println("  ⚠ No hay subastas registradas.");
            return null;
        }
        
        System.out.println("\n  Subastas registradas:");
        for (Object[] s : cacheSubastas) {
            String id = String.valueOf(s[0]);
            Object[] actual = nSubasta.buscar(id);
            imprimirSubasta(actual != null ? actual : s);
        }
        return leerTexto("  ID de la subasta");
    }

    private static void sincronizarSubastaEnCache(String id) {
        Object[] actualizada = nSubasta.buscar(id);
        if (actualizada == null) return;
        
        for (int i = 0; i < cacheSubastas.size(); i++) {
            if (String.valueOf(cacheSubastas.get(i)[0]).equals(id)) {
                cacheSubastas.set(i, actualizada);
                break;
            }
        }
    }

    private static List<Object[]> filtrarUsuariosPorRol(String rol) {
        List<Object[]> resultado = new ArrayList<>();
        for (Object[] u : cacheUsuarios) {
            if (rol.equals(String.valueOf(u[4]))) {
                resultado.add(u);
            }
        }
        return resultado;
    }
}

    
    
    /**
   
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
