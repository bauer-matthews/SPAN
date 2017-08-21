package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by matt on 7/26/17.
 */
public class FrameVariableTerm extends VariableTerm {

    private final int index;

    public FrameVariableTerm(String name, int index) {

        super(name, SortFactory.FRAME);
        this.index = index;
    }

    public int getRole() {
        return index;
    }

    @Override
    public Sort getSort() {
        return SortFactory.FRAME;
    }

    @Override
    public boolean hasSort(Sort sort) {
        return sort.equals(SortFactory.FRAME);
    }

    @Override
    public String toMathString() {
        return super.toMathString() + index;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof FrameVariableTerm)) {
            return false;
        }

        if (!(super.getName().equals(((FrameVariableTerm) o).getName()))) return false;
        if (!(index == ((FrameVariableTerm) o).index)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), index);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", super.getName())
                .add("index", index)
                .toString();
    }
}
