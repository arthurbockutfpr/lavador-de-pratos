package lavadorpratos;

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
