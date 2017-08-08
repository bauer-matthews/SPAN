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
public class SignatureBuilder {

    private final List<FunctionSymbol> functionSymbols = new ArrayList<>();
    private final Collection<NameTerm> publicNames = new ArrayList<>();
    private final Collection<NameTerm> privateNames = new ArrayList<>();
    private final Collection<VariableTerm> variables = new ArrayList<>();

    public SignatureBuilder() {

    }

    public SignatureBuilder addPair() {

        functionSymbols.addAll(Pair.SIGNATURE.getFunctions());
        publicNames.addAll(Pair.SIGNATURE.getPublicNames());
        privateNames.addAll(Pair.SIGNATURE.getPrivateNames());
        variables.addAll(Pair.SIGNATURE.getVariables());

        return this;
    }

    public SignatureBuilder addSimple() {

        functionSymbols.addAll(Simple.SIGNATURE.getFunctions());
        publicNames.addAll(Simple.SIGNATURE.getPublicNames());
        privateNames.addAll(Simple.SIGNATURE.getPrivateNames());
        variables.addAll(Simple.SIGNATURE.getVariables());

        return this;
    }

    public SignatureBuilder addSymmetricKey() {

        functionSymbols.addAll(SymmetricKey.SIGNATURE.getFunctions());
        publicNames.addAll(SymmetricKey.SIGNATURE.getPublicNames());
        privateNames.addAll(SymmetricKey.SIGNATURE.getPrivateNames());
        variables.addAll(SymmetricKey.SIGNATURE.getVariables());

        return this;
    }

    public Signature build() {
        return new Signature(functionSymbols, publicNames, privateNames, variables);
    }
}
