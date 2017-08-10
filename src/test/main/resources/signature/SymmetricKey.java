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
public class SymmetricKey {

    public static final VariableTerm VAR_M = new VariableTerm("m");
    public static final VariableTerm VAR_K = new VariableTerm("k");

    public static final FunctionSymbol ENC_SYMBOL = new FunctionSymbol("enc", 2);
    public static final FunctionSymbol DEC_SYMBOL = new FunctionSymbol("dec", 2);
    public static final FunctionSymbol HASH_SYMBOL = new FunctionSymbol("hash", 1);

    public static final Signature SIGNATURE;

    static {

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();
        functionSymbolList.add(ENC_SYMBOL);
        functionSymbolList.add(DEC_SYMBOL);
        functionSymbolList.add(HASH_SYMBOL);

        List<VariableTerm> variableList = new ArrayList<>();
        variableList.add(VAR_M);
        variableList.add(VAR_K);

        SIGNATURE = new Signature(functionSymbolList, Collections.emptyList(), Collections.emptyList(), variableList);
    }
}
