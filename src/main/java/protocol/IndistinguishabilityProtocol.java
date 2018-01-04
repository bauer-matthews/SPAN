package protocol;

import com.google.common.base.MoreObjects;
import org.apfloat.Aprational;
import protocol.role.Role;
import rewriting.Rewrite;
import rewriting.Signature;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class IndistinguishabilityProtocol {

    private final Metadata metadata;
    private final Signature signature;
    private final Collection<Rewrite> rewrites;
    private final Map<String, Aprational> fractionConstants;
    private final List<Role> roles1;
    private final List<Role> roles2;


    IndistinguishabilityProtocol(Metadata metadata, Signature signature, Collection<Rewrite> rewrites,
                                 Map<String, Aprational> fractionConstants, List<Role> roles1, List<Role> roles2) {

        Objects.requireNonNull(metadata);
        Objects.requireNonNull(signature);
        Objects.requireNonNull(rewrites);
        Objects.requireNonNull(fractionConstants);
        Objects.requireNonNull(roles1);
        Objects.requireNonNull(roles2);

        this.metadata = metadata;
        this.signature = signature;
        this.rewrites = rewrites;
        this.fractionConstants = fractionConstants;
        this.roles1 = roles1;
        this.roles2 = roles2;
    }

    public String getProtocolVersion() {
        return this.metadata.getVersion();
    }

    public Signature getSignature() {
        return this.signature;
    }

    public Collection<Rewrite> getRewrites() {
        return this.rewrites;
    }

    public Map<String, Aprational> getFractionConstants() {
        return this.fractionConstants;
    }

    public List<Role> getRoles1() {
        return roles1;
    }

    public List<Role> getRoles2() {
        return roles2;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof IndistinguishabilityProtocol)) {
            return false;
        }

        if (!metadata.equals(((IndistinguishabilityProtocol) o).metadata)) return false;
        if (!signature.equals(((IndistinguishabilityProtocol) o).signature)) return false;
        if (!rewrites.equals(((IndistinguishabilityProtocol) o).rewrites)) return false;
        if (!fractionConstants.equals(((IndistinguishabilityProtocol) o).fractionConstants)) return false;
        if (!roles1.equals(((IndistinguishabilityProtocol) o).roles1)) return false;
        if (!roles2.equals(((IndistinguishabilityProtocol) o).roles2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, signature, rewrites, fractionConstants, roles1, roles2);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("metadata", metadata)
                .add("signature", signature)
                .add("rewrites", rewrites)
                .add("fraction constants", fractionConstants)
                .add("first roles", roles1)
                .add("second roles", roles2)
                .toString();
    }
}
