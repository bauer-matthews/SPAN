package rewriting.terms;

import parser.protocol.ProtocolParseException;

import java.util.*;

/**
 * Created by mbauer on 8/17/2017.
 */
public class SortFactory {

    public static final Sort FRAME = new Sort("FRAME");
    public static final Sort SPECIAL = new Sort("SPECIAL");
    public static final Sort KIND = new Sort("KIND");

    static List<Sort> sorts;
    static List<SortOrder> sortOrders;
    static Map<Sort, ArrayList<Sort>> subsortMap;

    static {
        sorts = new ArrayList<>();
        sortOrders = new ArrayList<>();
        subsortMap = new HashMap<>();
    }

    public static List<Sort> getSorts() {
        return sorts;
    }

    public static List<SortOrder> getSortOrders() {
        return sortOrders;
    }

    public static void setSorts(List<Sort> sorts) {
        SortFactory.sorts = sorts;
    }

    public static void setSortOrders(List<SortOrder> sortOrders) {
        SortFactory.sortOrders = sortOrders;
        resetSubsortLists();
        computeSubsortLists();
    }

    private static void resetSubsortLists() {

        subsortMap.clear();
        for (Sort sort : sorts) {
            subsortMap.put(sort, new ArrayList<>());
        }
    }

    private static void computeSubsortLists() {

        for (SortOrder order : sortOrders) {
            subsortMap.get(order.getSuperSort()).add(order.getSubSort());
            addSubSubsorts(order.getSuperSort(), order.getSubSort());
        }
    }

    private static void addSubSubsorts(Sort baseSort, Sort subsort) {
        for (SortOrder order : sortOrders) {

            if (order.getSuperSort().equals(subsort)) {
                subsortMap.get(baseSort).add(order.getSubSort());
                addSubSubsorts(baseSort, order.getSubSort());
            }
        }
    }

    /**
     * @param sort1
     * @param sort2
     * @return true if sort1 has sort2
     */
    public static boolean hasSort(Sort sort1, Sort sort2) {
        return subsortMap.get(sort2).contains(sort1) || sort1.equals(sort2);
    }

    public static Sort fromString(String sortString) throws ProtocolParseException {

        for (int i = 0; i < sorts.size(); i++) {
            if (sorts.get(i).getName().equalsIgnoreCase(sortString)) {
                return sorts.get(i);
            }
        }

        throw new ProtocolParseException("Sort not found:" + sortString);
    }
}
