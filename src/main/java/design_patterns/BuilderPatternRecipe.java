package design_patterns;

/**
 * Builder Pattern recipe
 *
 * http://javarevisited.blogspot.com/2012/06/builder-design-pattern-in-java-example.html
 *
 * 1/ static nested Builder class
 * 2/ Builder class fields same as outer class
 * 3/ Builder constructor for required attributes
 * 4/ setter-like for optional attributes and return same Builder object
 * 5/ build() method to copy field values from `this` to outer class constructor
 * 6/ private constructor of outer class to assign each field
 *
 * Disadvantage:
 * verbose and duplication as Builder needs to copy all fields from outer class
 *
 * Optional:
 * use setter + declare outer class object in Builder + builder() return the object
 *
 * Usage:
 * new BuilderPatternRecipe.Builder(1, 2).calories(3).build();
 */
public class BuilderPatternRecipe {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values
        private int calories      = 0;
        private int fat           = 0;
        private int carbohydrate  = 0;
        private int sodium        = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val) { calories = val; return this; }
        public Builder fat(int val) { fat = val; return this; }
        public Builder carbohydrate(int val) { carbohydrate = val; return this; }
        public Builder sodium(int val) { sodium = val; return this; }

        public BuilderPatternRecipe build() {
            return new BuilderPatternRecipe(this);
        }
    }

    private BuilderPatternRecipe(Builder builder) {
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
