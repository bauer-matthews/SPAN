package attacker;

import org.apfloat.Apfloat;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface Tree {

    Node getRoot();

    Apfloat getAttackProbability();

}
