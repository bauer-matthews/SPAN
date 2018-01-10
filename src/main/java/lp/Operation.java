package lp;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public enum Operation {

    EQ {
        @Override
        public String toString() {
            return " = ";
        }
    }, LTE {
        @Override
        public String toString() {
            return " <= ";
        }
    }, LT {
        @Override
        public String toString() {
            return " < ";
        }
    };
}