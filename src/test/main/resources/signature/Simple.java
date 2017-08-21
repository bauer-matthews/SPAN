package resources.signature;

import rewriting.Signature;
import rewriting.terms.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static rewriting.terms.SortFactory.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/8/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Simple {


    public static final NameTerm PUB_NAME_A = new NameTerm("a", false, KIND);
    public static final NameTerm PUB_NAME_B = new NameTerm("b", false, KIND);
    public static final NameTerm PUB_NAME_C = new NameTerm("c", false, KIND);

    public static final NameTerm SEC_NAME_D = new NameTerm("d", true, KIND);
    public static final NameTerm SEC_NAME_E = new NameTerm("e", true, KIND);
    public static final NameTerm SEC_NAME_F = new NameTerm("f", true, KIND);

    public static final FunctionSymbol FUNC_ONE;
    public static final FunctionSymbol FUNC_TWO;
    public static final FunctionSymbol FUNC_THREE;

    public static final VariableTerm VAR_SX = new VariableTerm("sx", KIND);
    public static final VariableTerm VAR_SY = new VariableTerm("sy", KIND);
    public static final VariableTerm VAR_SZ = new VariableTerm("sz", KIND);

    public static final Signature SIGNATURE;

    static {

        FUNC_ONE = new FunctionSymbol("one", Collections.singletonList(KIND), KIND);

        List<Sort> twoParamSorts = new ArrayList<>();
        twoParamSorts.add(KIND);
        twoParamSorts.add(KIND);

        FUNC_TWO = new FunctionSymbol("two", twoParamSorts, KIND);

        List<Sort> threeParamSorts = new ArrayList<>();
        threeParamSorts.add(KIND);
        threeParamSorts.add(KIND);
        threeParamSorts.add(KIND);

        FUNC_THREE = new FunctionSymbol("three", threeParamSorts, KIND);

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

        SIGNATURE = new Signature(functionSymbolList, publicNames, privateNames,
                variables, Collections.singletonList(KIND), Collections.emptyList());
    }
}
