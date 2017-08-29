package util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/28/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class CollectionUtils {

    public static Collection intersection(Collection collection1, Collection collection2) {

        if(collection1.size() < collection2.size()) {
            intersection(collection2, collection1);
        }

        Collection<Object> collection = new ArrayList<>();

        for(Object object : collection1) {
            if(collection2.contains(object)) {
                collection.add(object);
            }
        }

        return collection;
    }

}
