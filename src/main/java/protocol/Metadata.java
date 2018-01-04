package protocol;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import mc.indistinguishability.IndistinguishabilityMethod;
import mc.reachability.ReachabilityMethod;
import parser.protocol.ProtocolType;

import java.util.Objects;

import static com.google.common.base.Objects.equal;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Metadata {

    private final String version;
    private final ProtocolType protocolType;
    private final IndistinguishabilityMethod indMethod;
    private final ReachabilityMethod reachabilityMethod;

    private int recipeDepth;
    private boolean enableXOR;

    public Metadata(String version, int recipeDepth, boolean enableXOR, ProtocolType protocolType,
                    ReachabilityMethod reachabilityMethod, IndistinguishabilityMethod indMethod) {

        Objects.requireNonNull(version);
        Objects.requireNonNull(protocolType);
        Objects.requireNonNull(indMethod);
        Objects.requireNonNull(reachabilityMethod);

        this.version = version;
        this.protocolType = protocolType;
        this.recipeDepth = recipeDepth;
        this.enableXOR = enableXOR;
        this.reachabilityMethod = reachabilityMethod;
        this.indMethod = indMethod;
    }

    public String getVersion() {
        return this.version;
    }

    public ProtocolType getProtocolType() {
        return this.protocolType;
    }

    public int getRecipeDepth() {
        return recipeDepth;
    }

    public boolean isXOR() {
        return enableXOR;
    }

    public IndistinguishabilityMethod getIndistinguishabilityMethod() {
        return indMethod;
    }

    public ReachabilityMethod getReachabilityMethod() {
        return reachabilityMethod;
    }

    @VisibleForTesting
    public void setRecipeDepth(int recipeDepth) {
        this.recipeDepth = recipeDepth;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Metadata)) {
            return false;
        }

        if (!(recipeDepth == ((Metadata) o).recipeDepth)) return false;
        if (!protocolType.equals(((Metadata) o).protocolType)) return false;
        if (enableXOR != ((Metadata) o).enableXOR) return false;
        if (!indMethod.equals(((Metadata) o).indMethod)) return false;

        return equal(version, ((Metadata) o).getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, recipeDepth, enableXOR, protocolType, indMethod);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("protocol type ", protocolType)
                .add("enable XOR", enableXOR)
                .add("recipe size", recipeDepth)
                .add("indistinguishability method", indMethod)
                .toString();
    }
}
