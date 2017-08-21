package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by mbauer on 8/17/2017.
 */
public class SortOrder {

    private final Sort subSort;
    private final Sort superSort;

    public SortOrder(Sort subSort, Sort superSort) {

        Objects.requireNonNull(subSort);
        Objects.requireNonNull(superSort);

        this.subSort = subSort;
        this.superSort = superSort;
    }

    public Sort getSubSort() {
        return subSort;
    }

    public Sort getSuperSort() {
        return superSort;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SortOrder)) {
            return false;
        }

        if (!(subSort.equals(((SortOrder) o).subSort))) return false;
        if (!(superSort.equals(((SortOrder) o).superSort))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subSort, superSort);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sub-sort", subSort)
                .add("super-sort", superSort)
                .toString();
    }
}
