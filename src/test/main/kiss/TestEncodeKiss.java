package kiss;

import org.junit.Test;
import rewriting.Signature;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.NameTerm;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mbauer on 8/3/2017.
 */
public class TestEncodeKiss {

    private static final FunctionSymbol PAIR = new FunctionSymbol("pair", 2);
    private static final FunctionSymbol FST = new FunctionSymbol("fst", 1);
    private static final FunctionSymbol SND = new FunctionSymbol("snd", 1);
    private static final FunctionSymbol ENC = new FunctionSymbol("enc", 2);
    private static final FunctionSymbol DEC = new FunctionSymbol("dec", 2);

    private static final VariableTerm X = new VariableTerm("x");
    private static final VariableTerm Y = new VariableTerm("y");
    private static final VariableTerm Z = new VariableTerm("z");

    private static final NameTerm A = new NameTerm("a", true);
    private static final NameTerm B = new NameTerm("b", true);
    private static final NameTerm C = new NameTerm("c", false);
    private static final NameTerm K = new NameTerm("k", true);


    @Test
    public void encodeKiss() throws Exception {

        List<FunctionSymbol> functions = new ArrayList<>();
        functions.add(PAIR);
        functions.add(FST);
        functions.add(SND);
        functions.add(ENC);
        functions.add(DEC);

        Collection<NameTerm> publicNames = new ArrayList<>();
        publicNames.add(C);

        Collection<NameTerm> privateNames = new ArrayList<>();
        privateNames.add(A);
        privateNames.add(B);
        privateNames.add(K);

        Collection<VariableTerm> variables = new ArrayList<>();
        variables.add(X);
        variables.add(Y);
        variables.add(Z);

        Signature signature = new Signature(functions, publicNames, privateNames, variables);

    }
}
