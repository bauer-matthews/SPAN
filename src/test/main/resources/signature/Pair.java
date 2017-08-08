package resources.signature;

import rewriting.Signature;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.NameTerm;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/8/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Pair {

    public static final VariableTerm VAR_X = new VariableTerm("x");
    public static final VariableTerm VAR_Y = new VariableTerm("y");

    public static final FunctionSymbol PAIR_SYMBOL = new FunctionSymbol("pair", 2);
    public static final FunctionSymbol FST_SYMBOL = new FunctionSymbol("fst", 1);
    public static final FunctionSymbol SND_SYMBOL = new FunctionSymbol("snd", 1);

    public static final Signature SIGNATURE;

    static {

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();
        functionSymbolList.add(PAIR_SYMBOL);
        functionSymbolList.add(FST_SYMBOL);
        functionSymbolList.add(SND_SYMBOL);


        Collection<VariableTerm> variableList = new ArrayList<>();
        variableList.add(VAR_X);
        variableList.add(VAR_Y);

        SIGNATURE = new Signature(functionSymbolList, Collections.emptyList(), Collections.emptyList(), variableList);
    }
}
