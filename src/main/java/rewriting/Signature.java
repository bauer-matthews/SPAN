package rewriting;

import com.google.common.base.MoreObjects;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.NameTerm;
import rewriting.terms.VariableTerm;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/23/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Signature {

    private final List<FunctionSymbol> functions;
    private final Collection<NameTerm> publicNames;
    private final Collection<NameTerm> privateNames;
    private final Collection<VariableTerm> variables;

    public Signature(List<FunctionSymbol> functions, Collection<NameTerm> publicNames,
                     Collection<NameTerm> privateNames, Collection<VariableTerm> variables) {

        Objects.requireNonNull(functions);
        Objects.requireNonNull(publicNames);
        Objects.requireNonNull(privateNames);
        Objects.requireNonNull(variables);

        this.functions = functions;
        this.publicNames = publicNames;
        this.privateNames = privateNames;
        this.variables = variables;
    }

    public List<FunctionSymbol> getFunctions() {
        return functions;
    }

    public Collection<NameTerm> getPublicNames() {
        return publicNames;
    }

    public Collection<NameTerm> getPrivateNames() {
        return privateNames;
    }

    public Collection<VariableTerm> getVariables() {
        return variables;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Signature)) {
            return false;
        }

        if (!functions.equals(((Signature) o).functions)) return false;
        if (!publicNames.equals(((Signature) o).publicNames)) return false;
        if (!privateNames.equals(((Signature) o).privateNames)) return false;
        if (!variables.equals(((Signature) o).variables)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions, publicNames, privateNames);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functions", functions.toString())
                .add("public names", publicNames.toString())
                .add("private names", privateNames.toString())
                .add("variables", variables.toString())
                .toString();
    }
}
