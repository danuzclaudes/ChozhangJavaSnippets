package playground.generics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestUnboundedWildCard {

	public static void unboundedArg(Holder<?> holder) {}
	
	public static void unboundedArg(Holder<?> holder, Object arg) {
		// holder.set(? t);
		// holder.set(arg);
		// cannot compile: The method set(capture of ?)
		// is not applicable for the arguments (Object)
		// => Not conversion issue, but the container can only have unknown type

		Object t = holder.get();  // type info is lost
	}

	public static <T> void wildsuper(Holder<? super T> holder) {}

	public static <T> void wildsuper(Holder<? super T> holder, T val) {
		holder.set(val);
		// T t = holder.get();
		// cannot compile: cannot convert from capture of ? super T to T
		// => ? super Fruit = Object => T t = Object
		Object t = holder.get();
		System.out.println(t);
	}
	
	Byte[] byteArray = {1,2,3,4,5,6,7,8,9};
	Set<Byte> byteSet1 = new HashSet<Byte>(Arrays.asList(byteArray));
	Set<Byte> byteSet2 = new HashSet<Byte>(Arrays.<Byte>asList(byteArray));
	Set<Byte> byteSet3 = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
	// Set<Byte> byteSet = new HashSet<Byte>(Arrays.<Byte>asList(1,2,3,4,5,6,7,8,9));
	// The parameterized method <Byte>asList(Byte...) of type Arrays is not applicable for the arguments (Integer, ...)
	// Set<Byte> byteSet = new HashSet<Byte>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	// The constructor HashSet<Byte>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)) is undefined

	public static void main(String[] args) {
		Holder raw = new Holder();
		raw = new Holder<Long>();
		Holder<Long> qualified = new Holder<Long>();
		Holder<?> unbounded = new Holder<Long>();
		Holder<? extends Long> bounded = new Holder<Long>();
		Long lng = 1L;

		// <?> case, accepts any types but the container is now having unknown type only.
		unboundedArg(raw);
		unboundedArg(qualified);
		unboundedArg(unbounded);
		unboundedArg(bounded);

		unboundedArg(raw, lng);
		unboundedArg(qualified, lng);
		unboundedArg(unbounded, lng);
		unboundedArg(bounded, lng);

		// <? super T> case, single type doesn't matter; multiple generic types needs to **conform**
		wildsuper(raw);  // warning: unchecked conversion to <? super Object>
		wildsuper(qualified);

		// wildsuper(unbounded); /* Holder<capture#1 of ?> cannot be converted to Holder<? super T>)) */
		// wildsuper(bounded);  /* Holder<capture#1 of ? extends Long> cannot be converted to Holder<? super T>) */

		wildsuper(raw, lng);  // warning: unchecked conversion to <? super Long>
		wildsuper(qualified, lng);

		// wildsuper(unbounded, lng);
		// cannot compile: The method wildsuper(Holder<? super T>, T)
		// is not applicable for the arguments (Holder<capture of ?>, Long)
		// => unknown cannot be converted to unknown

		// wildsuper(bounded, lng);
		// cannot compile: The method wildsuper(Holder<? super T>, T)
		// is not applicable for the arguments (Holder<capture of ? extends Long>, Long)
		// => ? extends T or T cannot be converted to unknown
	}

}

class Holder<T> {
	private T value;
	public Holder() {};
	public Holder(T val) { value = val; }
	public void set(T t) { value = t; }
	public T get() { return value; }
}
