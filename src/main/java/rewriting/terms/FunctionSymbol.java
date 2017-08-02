package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class FunctionSymbol {

    private final String symbol;
    private final int arity;

    public FunctionSymbol(String symbol, int arity) {

        Objects.requireNonNull(symbol);

        this.symbol = symbol;
        this.arity = arity;
    }

    public int getArity() {
        return arity;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof FunctionSymbol)) {
            return false;
        }

        if (this.arity != ((FunctionSymbol) o).arity) {
            return false;
        }

        return symbol.equalsIgnoreCase(((FunctionSymbol) o).symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, arity);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("symbol", symbol)
                .add("arity", arity)
                .toString();
    }
}
