package lavadorpratos;

import java.util.logging.Logger;

/**
 * Buffer limitado implementado como fila FIFO circular (sem Collections).
 * Todos os metodos sincronizam no proprio Escorredor (monitor) e usam wait/notify.
 */
public class Escorredor {

    private static final Logger LOGGER = Logger.getLogger(Escorredor.class.getName());

    /**
     * Rede de seguranca: com apenas notify() (sem notifyAll) e um unico monitor,
     * uma notificacao pode acordar uma thread "do tipo errado". O timeout garante
     * que nenhuma thread fique esperando para sempre.
     */
    private static final long TEMPO_MAXIMO_ESPERA_MS = 500;

    private final Prato[] pratos;
    private int inicio;      // posicao do proximo prato a ser retirado
    private int fim;         // posicao onde o proximo prato sera colocado
    private int quantidade;  // quantidade atual de pratos

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

    /**
     * Bloqueia o lavador enquanto nao houver espaco livre. So depois disso
     * ele cria um novo prato sujo.
     */
    public synchronized void aguardarEspaco() throws InterruptedException {
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length
                    + " pratos): lavador aguardando espaco");
        }
        while (estaCheio()) {
            wait(TEMPO_MAXIMO_ESPERA_MS);
        }
    }

    /** Coloca um prato no escorredor; se estiver cheio, espera ate abrir espaco. */
    public synchronized void colocar(Prato prato) throws InterruptedException {
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length
                    + " pratos): lavador aguardando espaco");
        }
        while (estaCheio()) {
            wait(TEMPO_MAXIMO_ESPERA_MS);
        }

        pratos[fim] = prato;
        fim = (fim + 1) % pratos.length;   // volta ao inicio do vetor (fila circular)
        quantidade++;
        LOGGER.fine(() -> prato + " colocado no escorredor (" + quantidade + "/" + pratos.length + ")");

        // Estava vazio e agora tem 1 prato: acorda UM enxugador que esteja descansando
        if (quantidade == 1) {
            notify();
        }
        if (estaCheio()) {
            LOGGER.info(() -> "Escorredor CHEIO (" + quantidade + "/" + pratos.length + " pratos)");
        }
    }

    /** Retira o prato mais antigo (FIFO); se estiver vazio, espera chegar um prato. */
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

        // Estava cheio e abriu uma vaga: acorda UM lavador que esteja esperando
        if (estavaCheio) {
            notify();
        }
        if (estaVazio()) {
            LOGGER.info(() -> "Escorredor VAZIO (" + quantidade + "/" + pratos.length + " pratos)");
        }
        return prato;
    }
}
