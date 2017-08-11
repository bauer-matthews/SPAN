package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.util.Pair;
import process.EquivalenceCheckResult;
import process.EquivalenceChecker;
import process.State;
import rewriting.Equality;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;
import util.rewrite.RewriteUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SubstitutionCache {

    private static final LoadingCache<Pair<Term, Collection<Equality>>, Term> solvedTerms;

    static {
        solvedTerms = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<Pair<Term, Collection<Equality>>, Term>() {
                            // TODO: tighten exception
                            public Term load(Pair<Term, Collection<Equality>> sub) throws Exception {
                                return RewriteUtils.applySubstitution(sub.getKey(), sub.getValue());
                            }
                        });
    }

    public static Term applySubstitution(Term term, Collection<Equality> equalities) throws ExecutionException {

        return solvedTerms.get(new Pair<>(term, equalities));
    }

    public static Equality applySubstitution(Equality equality, Collection<Equality> equalities)
            throws ExecutionException {

        Term subLhs = applySubstitution(equality.getLhs(), equalities);
        Term subRhs = applySubstitution(equality.getRhs(), equalities);

        return new Equality(subLhs, subRhs);
    }

    public static List<Term> applySubstitution(List<Term> terms, Collection<Equality> equalities)
            throws ExecutionException {

        List<Term> newTerms = new ArrayList<>();

        for (Term term : terms) {
            newTerms.add(applySubstitution(term, equalities));
        }

        return newTerms;
    }
}
