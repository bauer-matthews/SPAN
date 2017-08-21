package resources.signature;

import rewriting.Signature;
import rewriting.terms.*;

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
    private final List<NameTerm> publicNames = new ArrayList<>();
    private final List<NameTerm> privateNames = new ArrayList<>();
    private final List<VariableTerm> variables = new ArrayList<>();
    private final List<Sort> sorts = new ArrayList<>();
    private final List<SortOrder> sortOrders = new ArrayList<>();

    public SignatureBuilder() {

    }

    public SignatureBuilder addPair() {

        functionSymbols.addAll(Pair.SIGNATURE.getFunctions());
        publicNames.addAll(Pair.SIGNATURE.getPublicNames());
        privateNames.addAll(Pair.SIGNATURE.getPrivateNames());
        variables.addAll(Pair.SIGNATURE.getVariables());
        sorts.addAll(Pair.SIGNATURE.getSorts());
        sortOrders.addAll(Pair.SIGNATURE.getSortOrders());

        return this;
    }

    public SignatureBuilder addSimple() {

        functionSymbols.addAll(Simple.SIGNATURE.getFunctions());
        publicNames.addAll(Simple.SIGNATURE.getPublicNames());
        privateNames.addAll(Simple.SIGNATURE.getPrivateNames());
        variables.addAll(Simple.SIGNATURE.getVariables());
        sorts.addAll(Simple.SIGNATURE.getSorts());
        sortOrders.addAll(Simple.SIGNATURE.getSortOrders());

        return this;
    }

    public SignatureBuilder addSymmetricKey() {

        functionSymbols.addAll(SymmetricKey.SIGNATURE.getFunctions());
        publicNames.addAll(SymmetricKey.SIGNATURE.getPublicNames());
        privateNames.addAll(SymmetricKey.SIGNATURE.getPrivateNames());
        variables.addAll(SymmetricKey.SIGNATURE.getVariables());
        sorts.addAll(SymmetricKey.SIGNATURE.getSorts());
        sortOrders.addAll(SymmetricKey.SIGNATURE.getSortOrders());

        return this;
    }

    public Signature build() {
        return new Signature(functionSymbols, publicNames, privateNames, variables, sorts, sortOrders);
    }
}
