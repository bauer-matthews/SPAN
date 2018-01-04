package protocol;

import org.apfloat.Aprational;
import protocol.role.Role;
import rewriting.Rewrite;
import rewriting.Signature;

import java.util.Collection;
import java.util.List;
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
    private Collection<Rewrite> rewrites;
    private Map<String, Aprational> fractionConstants;
    private List<Role> roles1;
    private List<Role> roles2;


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

    public ProtocolBuilder rewrites(Collection<Rewrite> rewrites) {
        this.rewrites = rewrites;
        return this;
    }

    public ProtocolBuilder fractionConstants(Map<String, Aprational> fractionConstants) {
        this.fractionConstants = fractionConstants;
        return this;
    }

    public ProtocolBuilder roles(List<Role> roles1) {
        this.roles1 = roles1;
        return this;
    }

    public ProtocolBuilder roles2(List<Role> roles2) {
        this.roles2 = roles2;
        return this;
    }

    public ProtocolBuilder safetyProperty(SafetyProperty safetyProperty) {
        this.safetyProperty = safetyProperty;
        return this;
    }

    public ReachabilityProtocol buildReachabilityProtocol() throws IllegalStateException {

        if (metadata == null || signature == null || rewrites == null || fractionConstants == null ||
                roles1 == null || safetyProperty == null) {
            throw new IllegalStateException("Builder is missing required fields");
        }

        return new ReachabilityProtocol(metadata, signature, rewrites, fractionConstants, roles1, safetyProperty);
    }

    public IndistinguishabilityProtocol buildIndistinguishabilityProtocol() throws IllegalStateException {

        if (metadata == null || signature == null || rewrites == null || fractionConstants == null ||
                roles1 == null || roles2 == null) {
            throw new IllegalStateException("Builder is missing required fields");
        }

        return new IndistinguishabilityProtocol(metadata, signature, rewrites, fractionConstants, roles1, roles2);
    }
}
