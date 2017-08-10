package kiss;

import org.junit.Test;
import process.State;
import protocol.SafetyProperty;
import resources.rewritting.RewriteBuilder;
import resources.signature.SignatureBuilder;
import resources.signature.Simple;
import resources.signature.SymmetricKey;
import rewriting.Equality;
import rewriting.Rewrite;
import rewriting.Signature;
import rewriting.terms.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbauer on 8/3/2017.
 */
public class TestEncodeKiss {

    private static final String defaultEncoding =
            "signature one/1, two/2, three/3, enc/2, dec/2, hash/1, pair/2, fst/1, snd/1;\n" +
                    "variables sx, sy, sz, m, k, x, y;\n" +
                    "names a, b, c, d, e, f, W0, W1;\n" +
                    "rewrite\n" +
                    "\t\tdec(enc(m, k), k) -> m;\n" +
                    "\t\tfst(pair(x, y)) -> x;\n" +
                    "\t\tsnd(pair(x, y)) -> y;\n" +
                    "frames\n" +
                    "\t\tphi1 = new d, e, d.{W0 = enc(d, e), W1 = d},\n" +
                    "\t\tphi2 = new f, e.{W0 = enc(f, e), W1 = a};\n" +
                    "questions\n" +
                    "\t\tequiv phi1 phi2;";

    private static final String deductionEncoding =
            "signature one/1, two/2, three/3, enc/2, dec/2, hash/1, pair/2, fst/1, snd/1;\n" +
                    "variables sx, sy, sz, m, k, x, y;\n" +
                    "names a, b, c, d, e, f, W0, W1;\n" +
                    "rewrite\n" +
                    "\t\tdec(enc(m, k), k) -> m;\n" +
                    "\t\tfst(pair(x, y)) -> x;\n" +
                    "\t\tsnd(pair(x, y)) -> y;\n" +
                    "frames\n" +
                    "\t\tphi1 = new d, e, d.{W0 = enc(d, e), W1 = d},\n" +
                    "\t\tphi2 = new f, e.{W0 = enc(f, e), W1 = a};\n" +
                    "questions\n" +
                    "\t\tequiv phi1 phi2,\n" +
                    "\t\tdeducible NameTerm{name=d, private=true} phi1,\n" +
                    "\t\tdeducible NameTerm{name=d, private=true} phi2;";

    @Test
    public void encodeKiss() throws Exception {

        Signature signature = new SignatureBuilder()
                .addSimple()
                .addSymmetricKey()
                .addPair()
                .build();

        List<Rewrite> rewrites = new RewriteBuilder()
                .addSymmetricKey()
                .addPair()
                .build();

        // DEFINE STATE 1
        List<Equality> frame1 = new ArrayList<>();

        List<Term> cipher1Subterms = new ArrayList<>();
        cipher1Subterms.add(Simple.SEC_NAME_D);
        cipher1Subterms.add(Simple.SEC_NAME_E);
        Term cipher1 = new FunctionTerm(SymmetricKey.ENC_SYMBOL, cipher1Subterms);

        frame1.add(new Equality(new FrameVariableTerm("W", 0), cipher1));
        frame1.add(new Equality(new FrameVariableTerm("W", 1), Simple.SEC_NAME_D));

        State state1 = new State(Collections.emptyList(), frame1, Collections.emptyList());

        // DEFINE STATE 2
        List<Equality> frame2 = new ArrayList<>();

        List<Term> cipher2Subterms = new ArrayList<>();
        cipher2Subterms.add(Simple.SEC_NAME_F);
        cipher2Subterms.add(Simple.SEC_NAME_E);
        Term cipher2 = new FunctionTerm(SymmetricKey.ENC_SYMBOL, cipher2Subterms);

        frame2.add(new Equality(new FrameVariableTerm("W", 0), cipher2));
        frame2.add(new Equality(new FrameVariableTerm("W", 1), Simple.PUB_NAME_A));

        State state2 = new State(Collections.emptyList(), frame2, Collections.emptyList());

        long startTime = System.currentTimeMillis();

        String encodedString =
                KissEncoder.encode(signature, rewrites, state1, state2,
                        Collections.emptyList(), Collections.emptyList());

        long stopTime = System.currentTimeMillis();

        System.out.println("Kiss Encoding Time: " + (stopTime - startTime) + " milliseconds");

        assert (encodedString.equals(defaultEncoding));

        encodedString =
                KissEncoder.encode(signature, rewrites, state1, state2,
                       Collections.singletonList(Simple.SEC_NAME_D), Collections.singletonList(Simple.SEC_NAME_D));

        assert (encodedString.equals(deductionEncoding));


    }
}
