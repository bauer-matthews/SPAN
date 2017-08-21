package resources.signature;

import rewriting.Signature;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.NameTerm;
import rewriting.terms.Sort;
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

    public static final Sort MESSAGE = new Sort("Message");

    public static final VariableTerm VAR_X = new VariableTerm("x", MESSAGE);
    public static final VariableTerm VAR_Y = new VariableTerm("y", MESSAGE);

    public static final FunctionSymbol PAIR_SYMBOL;
    public static final FunctionSymbol FST_SYMBOL;
    public static final FunctionSymbol SND_SYMBOL;

    public static final Signature SIGNATURE;

    static {

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();

        List<Sort> pairParamSorts = new ArrayList<>();
        pairParamSorts.add(MESSAGE);
        pairParamSorts.add(MESSAGE);

        PAIR_SYMBOL = new FunctionSymbol("pair", pairParamSorts, MESSAGE);
        FST_SYMBOL = new FunctionSymbol("fst", Collections.singletonList(MESSAGE), MESSAGE);
        SND_SYMBOL = new FunctionSymbol("snd", Collections.singletonList(MESSAGE), MESSAGE);

        functionSymbolList.add(PAIR_SYMBOL);
        functionSymbolList.add(FST_SYMBOL);
        functionSymbolList.add(SND_SYMBOL);

        List<VariableTerm> variableList = new ArrayList<>();
        variableList.add(VAR_X);
        variableList.add(VAR_Y);

        SIGNATURE = new Signature(functionSymbolList, Collections.emptyList(), Collections.emptyList(),
                variableList, Collections.singletonList(MESSAGE), Collections.emptyList());
    }
}
