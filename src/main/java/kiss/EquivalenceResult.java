package kiss;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class EquivalenceResult {

    private final String frame1;
    private final String frame2;
    private final boolean equivalent;

    public EquivalenceResult(String frame1, String frame2, boolean equivalent) {
        Objects.requireNonNull(frame1);
        Objects.requireNonNull(frame2);

        this.frame1 = frame1;
        this.frame2 = frame2;
        this.equivalent = equivalent;
    }

    public String getFrame1() {
        return frame1;
    }

    public String getFrame2() {
        return frame2;
    }

    public boolean isEquivalent() {
        return equivalent;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof EquivalenceResult)) {
            return false;
        }

        if (!this.frame1.equals(((EquivalenceResult) o).frame1)) return false;
        if (!this.frame2.equals(((EquivalenceResult) o).frame2)) return false;
        if (!this.equivalent == ((EquivalenceResult) o).equivalent) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frame1, frame2, equivalent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("frame1", frame1)
                .add("frame2", frame2)
                .add("equivalent", isEquivalent())
                .toString();
    }
}
