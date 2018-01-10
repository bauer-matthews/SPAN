package lp;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Constraint {

    private static long CONSTRAINT_INDEX;

    private final VarList leftList;
    private final VarList rightList;
    private final Operation operation;
    private final String constraintId;

    static {
        CONSTRAINT_INDEX = 0;
    }

    Constraint(VarList leftList, VarList rightList, Operation operation) {

        Objects.requireNonNull(leftList);
        Objects.requireNonNull(rightList);
        Objects.requireNonNull(operation);

        this.leftList = leftList;
        this.rightList = rightList;
        this.operation = operation;

        this.constraintId = "c" + CONSTRAINT_INDEX++;
    }

    VarList getLeftList() {
        return leftList;
    }

    Operation getOperation() {
        return operation;
    }

    VarList getRightList() {
        return rightList;
    }

    String getConstraintId() {
        return constraintId;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Constraint)) {
            return false;
        }

        if (!leftList.equals(((Constraint) o).leftList)) return false;
        if (!rightList.equals(((Constraint) o).rightList)) return false;
        if (!operation.equals(((Constraint) o).operation)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftList, rightList, operation);
    }

    @Override
    public String toString() {
        return constraintId + ": " + leftList.toString() + " - " +rightList.toString() + operation.toString() + " 0 ";
    }
}
