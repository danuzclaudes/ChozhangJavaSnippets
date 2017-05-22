package playground.generics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenericTypesManager {
    public static void main(String[] args) {
        /* Generic<T>[] g = (Generic<T>[]) new Generic[n]; */
        ArrayList<Integer>[] lists1 = (ArrayList<Integer>[]) new ArrayList[6];
        List<Integer>[] lists2 = (ArrayList<Integer>[]) new ArrayList[6];
        List<Integer>[] lists3 = (List<Integer>[]) new ArrayList[6];
        List<Integer>[] lists4 = (List<Integer>[]) new List[6];
        System.out.println(lists1.getClass() + " " + lists2.getClass());
        System.out.println(lists3.getClass() + " " + lists4.getClass());
        /* compiler error: cannot create array with ‘<>’ */
        // List<Integer>[] lists = new List<>[1];

        ArrayList<Integer> list5 = new ArrayList<>();
        /* ClassCastException: java.lang.Object cannot be cast to java.util.ArrayList */
        // ArrayList<Integer> list = (ArrayList<Integer>) new Object();
    }
}

class Generic<T> {
    private T field;
    // private static T staticField;  // T cannot be referenced from a static context
    T getField() { return field; }  // T is fine for instance method
    // static T getStatic() { return staticField; }

    // nested interfaces and enum types are considered static
    private interface Copyable {
        // List<T> value;
        // T copy();  // error
    }
    private enum State {
        VALID, INVALID;
        // private T desc;  // error
        // public T getInfo() { return info; }    // error
    }

    // Enum may not have type parameters
    // private static enum genericEnum<T> {}

    /**
     * @param <S> generify the nested interface and static class
     */
    public interface Immutable<S> {
        S getImmutableItem();
    }
    private final static class Mutable<S> {}
    /**
     * <T> inner class can still access the enclosing class's type parameter
     */
    private final class ImmutableImpl implements Immutable<T> {
        @Override
        public T getImmutableItem() { return null; }
    }
}

interface GenericInterface<T> {
    // Generic<T> interfaceField = ...;
    T getValue();
}

/**
 * @param <T> the type parameter T of the class GenericOutterClass<T>
 *            is used as part of the bound of the type parameter W of inner class WrapperComparator
 */
class GenericOutterClass<T> {
    protected final T theObject;
    public GenericOutterClass(T t) { theObject = t; }
    public <U extends T> GenericOutterClass(GenericOutterClass<U> w) { theObject = w.theObject;}
    public T getWrapper() { return theObject; }
    // type parameter T as part of the bound of a wildcard
    private final class WrapperComparator <W extends GenericOutterClass<? extends Comparable<T>>>
            implements Comparator<W> {
        public int compare(W lhs, W rhs) {
            return lhs.theObject.compareTo((T)(rhs.theObject));
        }
    }
    public <V extends GenericOutterClass<? extends Comparable< T >>> Comparator<V> comparator() {
        return this.new WrapperComparator<V>();
    }
}

