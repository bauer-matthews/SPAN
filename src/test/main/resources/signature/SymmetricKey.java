package resources.signature;

import rewriting.Signature;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.Sort;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
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

    public static final Sort MESSAGE = new Sort("Message");
    public static final Sort NONCE = new Sort("Nonce");

    public static final VariableTerm VAR_M = new VariableTerm("m", MESSAGE);
    public static final VariableTerm VAR_K = new VariableTerm("k", NONCE);

    public static final FunctionSymbol ENC_SYMBOL;
    public static final FunctionSymbol DEC_SYMBOL;
    public static final FunctionSymbol HASH_SYMBOL;

    public static final Signature SIGNATURE;

    static {

        List<Sort> params = new ArrayList<>();
        params.add(MESSAGE);
        params.add(NONCE);

        ENC_SYMBOL = new FunctionSymbol("senc", params, MESSAGE);
        DEC_SYMBOL = new FunctionSymbol("sdec", params, MESSAGE);
        HASH_SYMBOL = new FunctionSymbol("hash", Collections.singletonList(MESSAGE), MESSAGE);

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();
        functionSymbolList.add(ENC_SYMBOL);
        functionSymbolList.add(DEC_SYMBOL);
        functionSymbolList.add(HASH_SYMBOL);

        List<VariableTerm> variableList = new ArrayList<>();
        variableList.add(VAR_M);
        variableList.add(VAR_K);

        SIGNATURE = new Signature(functionSymbolList, Collections.emptyList(), Collections.emptyList(),
                variableList, params, Collections.emptyList());
    }
}
