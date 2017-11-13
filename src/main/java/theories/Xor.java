package theories;

import rewriting.terms.FunctionSymbol;
import rewriting.terms.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 11/13/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Xor {

    private static final FunctionSymbol ZERO;
    private static final FunctionSymbol ONE;
    private static final FunctionSymbol PLUS;

    static {
        List<Sort> paramList = new ArrayList<>();
        paramList.add(new Sort("Bit"));
        paramList.add(new Sort("Bit"));
        PLUS = new FunctionSymbol("plus", paramList, new Sort("Bit"));
        ZERO = new FunctionSymbol("zero", Collections.emptyList(), new Sort("Bit"));
        ONE = new FunctionSymbol("one", Collections.emptyList(), new Sort("Bit"));

    }

    public static void addXorFunctions(Collection<FunctionSymbol> functionSymbols) {

        functionSymbols.add(ZERO);
        functionSymbols.add(ONE);
        functionSymbols.add(PLUS);
    }

    public static FunctionSymbol getZERO() {
        return ZERO;
    }

    public static FunctionSymbol getONE() {
        return ONE;
    }

    public static FunctionSymbol getPLUS() {
        return PLUS;
    }
}
