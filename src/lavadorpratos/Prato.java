package lavadorpratos;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Prato {

    private static final int PERCENTUAL_BAIXO = 30;
    private static final int PERCENTUAL_MEDIO = 60;
    private static final int TOTAL_PERCENTUAL = 100;

    private static final AtomicInteger ULTIMO_NUMERO_DE_SERIE = new AtomicInteger();

    private final int numeroDeSerie;
    private final NivelSujeira nivelSujeira;

    public Prato() {
        this.numeroDeSerie = ULTIMO_NUMERO_DE_SERIE.incrementAndGet();

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
