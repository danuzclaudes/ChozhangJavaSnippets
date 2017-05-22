package playground.enums;

import java.util.Random;

/**
 * A utility class to randomly choose a enum constant using generics.
 */
public class EnumsUtil {
    private static Random rand = new Random(47);
    public static <T extends Enum<T>> T random(Class<T> enumConstant) {
        return random(enumConstant.getEnumConstants());
    }
    public static <T> T random(T[] values) {
        return values[rand.nextInt(values.length)];
    }
}

// Error: type argument java.lang.String is not within bounds of type-variable E
// class MyClass<E extends Enum<String>> {}
