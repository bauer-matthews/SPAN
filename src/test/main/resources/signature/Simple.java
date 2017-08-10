package resources.signature;

import rewriting.Signature;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.NameTerm;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/8/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Simple {


    public static final NameTerm PUB_NAME_A = new NameTerm("a", false);
    public static final NameTerm PUB_NAME_B = new NameTerm("b", false);
    public static final NameTerm PUB_NAME_C = new NameTerm("c", false);

    public static final NameTerm SEC_NAME_D = new NameTerm("d", true);
    public static final NameTerm SEC_NAME_E = new NameTerm("e", true);
    public static final NameTerm SEC_NAME_F = new NameTerm("f", true);

    public static final FunctionSymbol FUNC_ONE = new FunctionSymbol("one", 1);
    public static final FunctionSymbol FUNC_TWO = new FunctionSymbol("two", 2);
    public static final FunctionSymbol FUNC_THREE = new FunctionSymbol("three", 3);

    public static final VariableTerm VAR_SX = new VariableTerm("sx");
    public static final VariableTerm VAR_SY = new VariableTerm("sy");
    public static final VariableTerm VAR_SZ = new VariableTerm("sz");

    public static final Signature SIGNATURE;

    static {

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();
        functionSymbolList.add(FUNC_ONE);
        functionSymbolList.add(FUNC_TWO);
        functionSymbolList.add(FUNC_THREE);

        List<NameTerm> publicNames = new ArrayList<>();
        publicNames.add(PUB_NAME_A);
        publicNames.add(PUB_NAME_B);
        publicNames.add(PUB_NAME_C);

        List<NameTerm> privateNames = new ArrayList<>();
        privateNames.add(SEC_NAME_D);
        privateNames.add(SEC_NAME_E);
        privateNames.add(SEC_NAME_F);

        List<VariableTerm> variables = new ArrayList<>();
        variables.add(VAR_SX);
        variables.add(VAR_SY);
        variables.add(VAR_SZ);

        SIGNATURE = new Signature(functionSymbolList, publicNames, privateNames, variables);
    }
}
