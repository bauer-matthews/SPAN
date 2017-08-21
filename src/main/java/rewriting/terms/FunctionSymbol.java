package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.List;
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
    private final List<Sort> sortParameters;
    private final Sort sortResult;

    public FunctionSymbol(String symbol, List<Sort> sortParameters, Sort sortResult) {

        Objects.requireNonNull(symbol);
        Objects.requireNonNull(sortParameters);
        Objects.requireNonNull(sortResult);

        this.symbol = symbol;
        this.sortParameters = sortParameters;
        this.sortResult = sortResult;
    }

    public int getArity() {
        return sortParameters.size();
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Sort> getParameterType() {
        return sortParameters;
    }

    public Sort getReturnType() {
        return sortResult;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof FunctionSymbol)) {
            return false;
        }

        if (!(this.symbol.equals(((FunctionSymbol) o).symbol))) return false;
        if (!(this.sortParameters.equals(((FunctionSymbol) o).sortParameters))) return false;
        if (!(this.sortResult.equals(((FunctionSymbol) o).sortResult))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, sortParameters, sortResult);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("symbol", symbol)
                .add("parameter type", sortParameters)
                .add("return type", sortResult)
                .toString();
    }
}
