package log;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public enum Severity {

    ERROR, WARNING, INFO;

    @Override
    public String toString() {
        switch(this) {
            case ERROR: return "Error";
            case WARNING: return "Warning";
            case INFO: return "Info";
            default: throw new IllegalArgumentException();
        }
    }
}
