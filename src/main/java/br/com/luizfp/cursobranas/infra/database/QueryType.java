package br.com.luizfp.cursobranas.infra.database;

public enum QueryType {
    ONE,
    MAYBE_ONE,
    MANY,
    NONE;

    public boolean onlyOne() {
        return this == ONE || this == MAYBE_ONE;
    }

    public boolean requiresSomeResult() {
        return this == ONE || this == MANY;
    }

    public boolean noResults() {
        return this == NONE;
    }
}
