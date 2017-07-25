package protocol;

import org.apfloat.Apfloat;
import protocol.role.Role;
import rewriting.Rewrites;
import rewriting.Signature;

import java.util.Collection;
import java.util.Map;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ProtocolBuilder {

    // Required Parameters
    private Metadata metadata;
    private Signature signature;
    private Rewrites rewrites;
    private Map<String, Apfloat> fractionConstants;
    private Collection<Role> roles;
    private SafetyProperty safetyProperty;

    public ProtocolBuilder() {

    }

    public ProtocolBuilder metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public ProtocolBuilder signature(Signature signature) {
        this.signature = signature;
        return this;
    }

    public ProtocolBuilder rewrites(Rewrites rewrites) {
        this.rewrites = rewrites;
        return this;
    }

    public ProtocolBuilder fractionConstants(Map<String, Apfloat> fractionConstants) {
        this.fractionConstants = fractionConstants;
        return this;
    }

    public ProtocolBuilder roles(Collection<Role> roles) {
        this.roles = roles;
        return this;
    }

    public ProtocolBuilder safetyProperty(SafetyProperty safetyProperty) {
        this.safetyProperty = safetyProperty;
        return this;
    }

    public Protocol build() throws IllegalStateException {

        if(metadata == null || signature == null || rewrites == null || fractionConstants == null ||
                roles == null || safetyProperty == null) {
            throw new IllegalStateException("Builder is missing required fields");
        }

        return new Protocol(metadata, signature, rewrites, fractionConstants, roles, safetyProperty);
    }
}
