package design_patterns;

/**
 * equals() method recipe
 *
 * 1/ @Override public boolean equals(Object o): override Object.equals(Object) instead of overload
 * 2/ if (this == o) return true: check if argument is the reference to this object
 * 3/ if (!(o instanceof MClass)) return false: check if argument is of same type which contains this equals()
 * 4/ MClass obj = (MClass) o: cast argument to outer class (guaranteed to succeed)
 * 5/ check if each field of argument matches corresponding field of object
 * 5.1/ primitive ==
 * 5.2/ Float.compare, Double.compare for float/double type
 * 5.3/ object reference: recursively invoke equals()
 * 5.4/ null: field == null ? o.field == null : field.equals(o.field)
 * 6/ unit test for symmetric and transitive
 */
public class EqualsMethodRecipe {
}
