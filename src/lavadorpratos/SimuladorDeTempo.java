package lavadorpratos;

/**
 * Simula a passagem de tempo com um laco de repeticao (espera ocupada),
 * conforme exigido na especificacao.
 */
public final class SimuladorDeTempo {

    private SimuladorDeTempo() {
    }

    /**
     * Conta ate o limite informado. Retorna o contador para que o resultado seja
     * usado e o compilador JIT nao elimine o laco como "codigo morto".
     */
    public static long contarAte(long limite) {
        long contador = 0;
        while (contador < limite) {
            contador++;
        }
        return contador;
    }
}
