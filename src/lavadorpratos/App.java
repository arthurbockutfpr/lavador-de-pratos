package lavadorpratos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static final int PADRAO_LAVADORES = 2;
    private static final int PADRAO_ENXUGADORES = 2;
    private static final int PADRAO_CAPACIDADE = 5;
    private static final long DURACAO_EXECUCAO_MS = 60_000;
    private static final long INTERVALO_INTERRUPCAO_MS = 100;

    private final Escorredor escorredor;
    private final int quantidadeLavadores;
    private final int quantidadeEnxugadores;
    private final List<Lavador> lavadores = new ArrayList<>();
    private final List<Enxugador> enxugadores = new ArrayList<>();
    private final List<Thread> threadsLavadores = new ArrayList<>();
    private final List<Thread> threadsEnxugadores = new ArrayList<>();

    public App(int quantidadeLavadores, int quantidadeEnxugadores, int capacidadeEscorredor) {
        this.quantidadeLavadores = quantidadeLavadores;
        this.quantidadeEnxugadores = quantidadeEnxugadores;
        this.escorredor = new Escorredor(capacidadeEscorredor);
    }

    public void iniciar() {
        LOGGER.info("Iniciando: " + quantidadeLavadores + " lavador(es) e "
                + quantidadeEnxugadores + " enxugador(es)");
        for (int i = 1; i <= quantidadeLavadores; i++) {
            Lavador lavador = new Lavador(escorredor);
            Thread thread = new Thread(lavador, "Lavador-" + i);
            lavadores.add(lavador);
            threadsLavadores.add(thread);
            thread.start();
        }
        for (int i = 1; i <= quantidadeEnxugadores; i++) {
            Enxugador enxugador = new Enxugador(escorredor);
            Thread thread = new Thread(enxugador, "Enxugador-" + i);
            enxugadores.add(enxugador);
            threadsEnxugadores.add(thread);
            thread.start();
        }
    }

    public void parar() throws InterruptedException {
        LOGGER.info("Encerrando os trabalhos...");
        lavadores.forEach(Lavador::parar);
        aguardarTermino(threadsLavadores);

        enxugadores.forEach(Enxugador::parar);
        aguardarTermino(threadsEnxugadores);
        LOGGER.info("Trabalhos encerrados. Pratos no escorredor: "
                + (escorredor.estaVazio() ? "nenhum" : "AINDA HA PRATOS"));
    }

    private void aguardarTermino(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            while (thread.isAlive()) {
                thread.interrupt();
                thread.join(INTERVALO_INTERRUPCAO_MS);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int lavadores = lerInteiro(args, 0, PADRAO_LAVADORES);
        int enxugadores = lerInteiro(args, 1, PADRAO_ENXUGADORES);
        int capacidade = lerInteiro(args, 2, PADRAO_CAPACIDADE);
        Level nivelDeLog = args.length > 3 ? Level.parse(args[3].toUpperCase()) : Level.INFO;
        configurarLog(nivelDeLog);

        App app = new App(lavadores, enxugadores, capacidade);
        app.iniciar();
        Thread.sleep(DURACAO_EXECUCAO_MS);
        app.parar();
    }

    private static int lerInteiro(String[] args, int posicao, int padrao) {
        if (args.length <= posicao) {
            return padrao;
        }
        int valor = Integer.parseInt(args[posicao]);
        if (valor <= 0) {
            throw new IllegalArgumentException("Os valores devem ser maiores que zero");
        }
        return valor;
    }

    private static void configurarLog(Level nivel) {
        Logger raiz = Logger.getLogger("");
        for (Handler handler : raiz.getHandlers()) {
            raiz.removeHandler(handler);
        }
        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(nivel);
        console.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord registro) {
                return String.format("%1$tT [%2$s] %3$s%n", new Date(registro.getMillis()),
                        Thread.currentThread().getName(), formatMessage(registro));
            }
        });
        raiz.addHandler(console);
        raiz.setLevel(nivel);
    }
}
