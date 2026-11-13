package lavadorpratos;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/** Consumidor: retira pratos do escorredor e enxuga. */
public class Enxugador implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Enxugador.class.getName());

    private static final long PASSO_DE_CICLOS = 10_000_000L;
    private static final int MINIMO_DE_PASSOS = 3;   // 30.000.000
    private static final int MAXIMO_DE_PASSOS = 10;  // 100.000.000

    private final Escorredor escorredor;
    private volatile boolean done;

    public Enxugador(Escorredor escorredor) {
        this.escorredor = escorredor;
    }

    /** Sinaliza o fim do trabalho; o enxugador ainda esvazia o escorredor antes de sair. */
    public void parar() {
        this.done = true;
    }

    @Override
    public void run() {
        LOGGER.fine("Enxugador iniciou o trabalho");
        // So termina quando recebeu done E nao ha mais pratos molhados
        while (!done || !escorredor.estaVazio()) {
            try {
                Prato prato = escorredor.retirar();
                LOGGER.fine(() -> "Enxugando " + prato);
                enxugar(prato);
                LOGGER.fine(() -> prato + " enxuto");
            } catch (InterruptedException e) {
                LOGGER.fine("Enxugador interrompido");
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.fine("Enxugador encerrou o trabalho");
    }

    private void enxugar(Prato prato) {
        // Sorteia de 3 a 10 passos de 10 milhoes: 30M, 40M, ..., 100M
        int passos = ThreadLocalRandom.current().nextInt(MINIMO_DE_PASSOS, MAXIMO_DE_PASSOS + 1);
        long contagem = SimuladorDeTempo.contarAte(passos * PASSO_DE_CICLOS);
        LOGGER.finer(() -> "Contagem do enxugamento concluida: " + contagem);
    }
}
