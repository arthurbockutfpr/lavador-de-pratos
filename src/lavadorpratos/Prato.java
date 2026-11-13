package lavadorpratos;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prato sujo, com numero de serie sequencial e nivel de sujeira sorteado
 * na proporcao 30% BAIXO, 60% MEDIO e 10% ENGORDURADO.
 */
public class Prato {

    private static final int PERCENTUAL_BAIXO = 30;
    private static final int PERCENTUAL_MEDIO = 60;
    private static final int TOTAL_PERCENTUAL = 100;

    // AtomicInteger porque varios lavadores criam pratos ao mesmo tempo
    private static final AtomicInteger ULTIMO_NUMERO_DE_SERIE = new AtomicInteger();

    private final int numeroDeSerie;
    private final NivelSujeira nivelSujeira;

    public Prato() {
        this.numeroDeSerie = ULTIMO_NUMERO_DE_SERIE.incrementAndGet();

        // Sorteio de 0 a 99: [0,30) -> BAIXO, [30,90) -> MEDIO, [90,100) -> ENGORDURADO
        int sorteio = ThreadLocalRandom.current().nextInt(TOTAL_PERCENTUAL);
        if (sorteio < PERCENTUAL_BAIXO) {
            this.nivelSujeira = NivelSujeira.BAIXO;
        } else if (sorteio < PERCENTUAL_BAIXO + PERCENTUAL_MEDIO) {
            this.nivelSujeira = NivelSujeira.MEDIO;
        } else {
            this.nivelSujeira = NivelSujeira.ENGORDURADO;
        }
    }

    public int getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public NivelSujeira getNivelSujeira() {
        return nivelSujeira;
    }

    @Override
    public String toString() {
        return "Prato #" + numeroDeSerie + " [" + nivelSujeira + "]";
    }
}
