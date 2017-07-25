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
    private final int args;

    public FunctionSymbol(String symbol, int args) {

        Objects.requireNonNull(symbol);

        this.symbol = symbol;
        this.args = args;
    }

    public int getNumArgs() {
        return args;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof FunctionSymbol)) {
            return false;
        }

        if(this.args != ((FunctionSymbol) o).args) {
            return false;
        }

        return symbol.equalsIgnoreCase(((FunctionSymbol) o).symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, args);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("symbol", symbol)
                .add("args", args)
                .toString();
    }
}
