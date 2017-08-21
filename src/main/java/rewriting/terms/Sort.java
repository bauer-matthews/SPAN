package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by mbauer on 8/17/2017.
 */
public class Sort {

    private final String name;

    public Sort(String name) {

        Objects.requireNonNull(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Sort)) {
            return false;
        }

        if (!(name.equals(((Sort) o).name))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
