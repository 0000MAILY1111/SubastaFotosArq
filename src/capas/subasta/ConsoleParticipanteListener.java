package capas.subasta;

public class ConsoleParticipanteListener implements PujaListener {
    private final String nombreParticipante;

    public ConsoleParticipanteListener(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    @Override
    public void onNuevaPuja(Object[] subasta, Object[] puja) {
        Object idSubasta = subasta[0];
        Object precioActual = subasta[5];
        Object monto = puja[3];
        Object idPujador = puja[2];
        System.out.println("[Notificación a " + nombreParticipante + "] Subasta #" + idSubasta
                + " nueva puja de usuario " + idPujador + " por " + monto
                + ". Precio actual: " + precioActual);
    }
}

