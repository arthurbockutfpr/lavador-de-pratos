package lavadorpratos;

import java.util.logging.Logger;

/** Produtor: cria pratos sujos, lava e coloca no escorredor. */
public class Lavador implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Lavador.class.getName());

    private final Escorredor escorredor;
    private volatile boolean done;

    public Lavador(Escorredor escorredor) {
        this.escorredor = escorredor;
    }

    /** Sinaliza o fim do trabalho (condicao de termino). */
    public void parar() {
        this.done = true;
    }

    @Override
    public void run() {
        LOGGER.fine("Lavador iniciou o trabalho");
        while (!done) {
            try {
                escorredor.aguardarEspaco();      // so cria prato novo se houver espaco
                Prato prato = new Prato();        // pilha infinita de pratos sujos
                LOGGER.fine(() -> "Lavando " + prato);
                lavar(prato);
                LOGGER.fine(() -> prato + " lavado");
                escorredor.colocar(prato);
            } catch (InterruptedException e) {
                LOGGER.fine("Lavador interrompido");
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.fine("Lavador encerrou o trabalho");
    }

    private void lavar(Prato prato) {
        long ciclos = prato.getNivelSujeira().getCiclosDeLavagem();
        long contagem = SimuladorDeTempo.contarAte(ciclos);
        LOGGER.finer(() -> "Contagem da lavagem concluida: " + contagem);
    }
}
