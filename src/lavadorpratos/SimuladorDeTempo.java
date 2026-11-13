package lavadorpratos;

public final class SimuladorDeTempo {

    private SimuladorDeTempo() {
    }

    public static long contarAte(long limite) {
        long contador = 0;
        while (contador < limite) {
            contador++;
        }
        return contador;
    }
}
