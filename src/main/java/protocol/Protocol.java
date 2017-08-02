package protocol;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
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
public class Protocol {

    private final Metadata metadata;
    private final Signature signature;
    private final Collection<Rewrite> rewrites;
    private final Map<String, Apfloat> fractionConstants;
    private final List<Role> roles;
    private final SafetyProperty safetyProperty;

    Protocol(Metadata metadata, Signature signature, Collection<Rewrite> rewrites,
             Map<String, Apfloat> fractionConstants, List<Role> roles, SafetyProperty safetyProperty) {

        Objects.requireNonNull(metadata);
        Objects.requireNonNull(signature);
        Objects.requireNonNull(rewrites);
        Objects.requireNonNull(fractionConstants);
        Objects.requireNonNull(roles);
        Objects.requireNonNull(safetyProperty);

        this.metadata = metadata;
        this.signature = signature;
        this.rewrites = rewrites;
        this.fractionConstants = fractionConstants;
        this.roles = roles;
        this.safetyProperty = safetyProperty;
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

    public Map<String, Apfloat> getFractionConstants() {
        return this.fractionConstants;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Protocol)) {
            return false;
        }

        if (!metadata.equals(((Protocol) o).metadata)) return false;
        if (!signature.equals(((Protocol) o).signature)) return false;
        if (!rewrites.equals(((Protocol) o).rewrites)) return false;
        if (!fractionConstants.equals(((Protocol) o).fractionConstants)) return false;
        if (!roles.equals(((Protocol) o).roles)) return false;
        if (!safetyProperty.equals(((Protocol) o).safetyProperty)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, signature, rewrites, fractionConstants, roles, safetyProperty);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("metadata", metadata)
                .add("signature", signature)
                .add("rewrites", rewrites)
                .add("fraction constants", fractionConstants)
                .add("roles", roles)
                .add("safety property", safetyProperty)
                .toString();
    }
}
