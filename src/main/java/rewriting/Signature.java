package rewriting;

import com.google.common.base.MoreObjects;
import rewriting.terms.*;

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
    private final List<NameTerm> publicNames;
    private final List<NameTerm> privateNames;
    private final List<VariableTerm> variables;
    private final List<Sort> sorts;
    private final List<SortOrder> sortOrders;

    public Signature(List<FunctionSymbol> functions, List<NameTerm> publicNames,
                     List<NameTerm> privateNames, List<VariableTerm> variables,
                     List<Sort> sorts, List<SortOrder> sortOrders) {

        Objects.requireNonNull(functions);
        Objects.requireNonNull(publicNames);
        Objects.requireNonNull(privateNames);
        Objects.requireNonNull(variables);
        Objects.requireNonNull(sorts);
        Objects.requireNonNull(sortOrders);

        this.functions = functions;
        this.publicNames = publicNames;
        this.privateNames = privateNames;
        this.variables = variables;
        this.sorts = sorts;
        this.sortOrders = sortOrders;
    }

    public List<FunctionSymbol> getFunctions() {
        return functions;
    }

    public List<NameTerm> getPublicNames() {
        return publicNames;
    }

    public List<NameTerm> getPrivateNames() {
        return privateNames;
    }

    public List<VariableTerm> getVariables() {
        return variables;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public List<SortOrder> getSortOrders() {
        return sortOrders;
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
        if (!sorts.equals(((Signature) o).sorts)) return false;
        if (!sortOrders.equals(((Signature) o).sortOrders)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions, publicNames, privateNames, sorts, sortOrders);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functions", functions.toString())
                .add("public names", publicNames.toString())
                .add("private names", privateNames.toString())
                .add("variables", variables.toString())
                .add("sorts", sorts.toString())
                .add("sort orders", sortOrders.toString())
                .toString();
    }
}
