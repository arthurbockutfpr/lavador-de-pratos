package lavadorpratos;

import java.util.logging.Logger;

public class Escorredor {

    private static final Logger LOGGER = Logger.getLogger(Escorredor.class.getName());

    private static final long TEMPO_MAXIMO_ESPERA_MS = 500;

    private final Prato[] pratos;
    private int inicio;
    private int fim;
    private int quantidade;

    public Escorredor(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser maior que zero");
        }
        this.pratos = new Prato[capacidade];
    }

    public synchronized boolean estaCheio() {
        return quantidade == pratos.length;
    }

    public synchronized boolean estaVazio() {
        return quantidade == 0;
    }

    public synchronized void aguardarEspaco() throws InterruptedException {
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length
                    + " pratos): lavador aguardando espaco");
        }
        while (estaCheio()) {
            wait(TEMPO_MAXIMO_ESPERA_MS);
        }
    }

    public synchronized void colocar(Prato prato) throws InterruptedException {
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length
                    + " pratos): lavador aguardando espaco");
        }
        while (estaCheio()) {
            wait(TEMPO_MAXIMO_ESPERA_MS);
        }

        pratos[fim] = prato;
        fim = (fim + 1) % pratos.length;
        quantidade++;
        LOGGER.fine(() -> prato + " colocado no escorredor (" + quantidade + "/" + pratos.length + ")");

        if (quantidade == 1) {
            notify();
        }
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length + " pratos)");
        }
    }

    public synchronized Prato retirar() throws InterruptedException {
        if (estaVazio()) {
            LOGGER.info(() -> "Escorredor VAZIO (" + quantidade + "/" + pratos.length
                    + " pratos): enxugador aguardando");
        }
        while (estaVazio()) {
            wait(TEMPO_MAXIMO_ESPERA_MS);
        }

        boolean estavaCheio = estaCheio();
        Prato prato = pratos[inicio];
        pratos[inicio] = null;
        inicio = (inicio + 1) % pratos.length;
        quantidade--;
        LOGGER.fine(() -> prato + " retirado do escorredor (" + quantidade + "/" + pratos.length + ")");

        if (estavaCheio) {
            notify();
        }
        if (estaVazio()) {
            LOGGER.info(() -> "Escorredor VAZIO (" + quantidade + "/" + pratos.length + " pratos)");
        }
        return prato;
    }
}
