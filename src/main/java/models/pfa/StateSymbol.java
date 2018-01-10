package models.pfa;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class StateSymbol {

    private final int stateIndex;
    private final int symbolIndex;

    public StateSymbol(int stateIndex, int symbolIndex) {

        this.stateIndex = stateIndex;
        this.symbolIndex = symbolIndex;
    }

    public int getSymbolIndex() {
        return symbolIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof StateSymbol)) {
            return false;
        }

        if (stateIndex != ((StateSymbol) o).stateIndex) return false;
        if (symbolIndex != ((StateSymbol) o).symbolIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateIndex, symbolIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state index", stateIndex)
                .add("symbol index", symbolIndex)
                .toString();
    }
}
