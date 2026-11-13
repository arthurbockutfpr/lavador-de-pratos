package lavadorpratos;

/**
 * Nivel de sujeira de um prato. Cada nivel define quantas iteracoes
 * o laco de repeticao deve contar para simular o tempo de lavagem.
 */
public enum NivelSujeira {
    BAIXO(30_000_000L),
    MEDIO(50_000_000L),
    ENGORDURADO(100_000_000L);

    private final long ciclosDeLavagem;

    NivelSujeira(long ciclosDeLavagem) {
        this.ciclosDeLavagem = ciclosDeLavagem;
    }

    public long getCiclosDeLavagem() {
        return ciclosDeLavagem;
    }
}
