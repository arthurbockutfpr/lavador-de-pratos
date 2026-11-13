# Lavador de Pratos - Produtor/Consumidor

Projeto da disciplina Fundamentos de Programacao Concorrente (UTFPR Toledo).

## Como compilar e executar

```
javac -d out src/lavadorpratos/*.java
java -cp out lavadorpratos.App [lavadores] [enxugadores] [capacidade] [nivelDeLog]
```

Exemplos:

```
java -cp out lavadorpratos.App              # 2 lavadores, 2 enxugadores, escorredor com 5 pratos
java -cp out lavadorpratos.App 3 2 5 FINE   # 3 lavadores, 2 enxugadores, log detalhado
```

A aplicacao roda por 1 minuto e depois encerra os trabalhos, esvaziando o escorredor.
