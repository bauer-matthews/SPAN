package lp;

import com.google.common.base.Joiner;
import org.apfloat.Aprational;

import java.util.Arrays;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class VarList {

    private static final Joiner PLUS_JOINER = Joiner.on(" + ");
    private static final Joiner MINUS_JOINER = Joiner.on(" - ");

    private final Aprational[] constList;
    private final String varName;
    private final boolean positive;

    private final String symbol;

    VarList(Aprational[] constList, String varName, boolean positive) {

        Objects.requireNonNull(constList);
        Objects.requireNonNull(varName);

        this.constList = constList;
        this.varName = varName;
        this.positive = positive;

        if (positive) {
            symbol = " + ";
        } else {
            symbol = " - ";
        }
    }

    public Aprational[] getConstList() {
        return constList;
    }

    public Aprational getFirstProb() {
        return constList[0];
    }

    boolean isPositive() {
        return positive;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof VarList)) {
            return false;
        }

        if (!Arrays.equals(constList, ((VarList) o).constList)) return false;
        if (!varName.equals(((VarList) o).varName)) return false;
        if (positive != ((VarList) o).positive) return false;

        return true;
    }

    @Override
    public int hashCode() {

        return Objects.hash(varName, positive) + Arrays.hashCode(constList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(constList[0].doubleValue());
        sb.append(" ");
        sb.append(varName);
        sb.append(0);

        for (int i = 1; i < constList.length; i++) {

            if (!constList[i].equals(Aprational.ZERO)) {
                sb.append(symbol);
                sb.append(constList[i].doubleValue());
                sb.append(" ");
                sb.append(varName);
                sb.append(i);
            }
        }

        return sb.toString();
    }
}