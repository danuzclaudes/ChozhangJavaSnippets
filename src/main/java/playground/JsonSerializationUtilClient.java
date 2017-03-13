package playground;

import com.amazon.chongrui.aws.common.util.JsonSerializationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JsonSerializationUtil:
 * - serialize(T)
 * - serializeToBase64(T)
 * - serializeList(Collection<T>)
 * - deserialize(String, Class<T>)
 * - deserialize(String, TypeReference<T>)
 * - deserializeFromBase64(String, TypeReference<T>)
 * - deserializeList(Collection<String>, Class<T>)
 *
 * To read a random JSON file, use TypeReference<Map<String, Object>>
 */
public class JsonSerializationUtilClient {
    public static void main(String[] args) throws ParseException {
        /*
         * {
         *   "a" : [ "a1", "a2" ],
         *   "b" : [ "b2", "b1" ]
         * }
         */
        Map<String, Set<String>> complex = new HashMap<>();
        complex.put("a", Sets.newHashSet("a1", "a2"));
        complex.put("b", Sets.newHashSet("b1", "b2"));
        String jsonMapStr = JsonSerializationUtil.serialize(complex);
        System.out.println(jsonMapStr);

        // {a=[a1, a2], b=[b2, b1]}
        // Todo: compile error for parameterized type class: Map<String, Set<String>>.class
        // Todo: use TypeReference to get rid of warning on Unchecked assignment
        Map<String, Set<String>> jsonOutput = JsonSerializationUtil.deserialize(jsonMapStr, Map.class);
        System.out.println(jsonOutput);
        assert complex.equals(jsonOutput);
        System.out.println(JsonSerializationUtil.deserialize(jsonMapStr,
                                                             new TypeReference<Map<String, Set<String>>>(){}));

        // ewogICJhIiA6IFsgImExIiwgImEyIiBdLAogICJiIiA6IFsgImIyIiwgImIxIiBdCn0=
        String base64Str = JsonSerializationUtil.serializeToBase64(complex);
        System.out.println(base64Str);
        Map<String, Set<String>> base64Map = JsonSerializationUtil
                .deserializeFromBase64(base64Str, new TypeReference<Map<String, Set<String>>>(){});
        System.out.println(base64Map);
        assert complex.equals(base64Map);

        /*[{
         *  "color" : "black",
         *  "model" : "Corolla",
         *  "purchaseDate" : "2017-03-12 10:31 AM"
         *},
         *{
         *  "color" : "black",
         *  "model" : "Camry",
         *  "purchaseDate" : "2017-01-31 00:00 AM"
         *}]
         */
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        List<Car> list = ImmutableList.<Car>builder()
                .add(new Car("black", "Corolla", new Date()))
                .add(new Car("black", "Camry", dateFormat.parse("01.31.2017")))
                .build();
        List<String> serializedList = JsonSerializationUtil.serializeList(list);
        System.out.println(serializedList);
        List<Car> deserializedList = JsonSerializationUtil.deserializeList(serializedList, Car.class);
        // [playground.Car@27808f31, playground.Car@436e852b]
        System.out.println(deserializedList);
        assert list.equals(deserializedList);
    }
}

class Car {
    private String color;
    private String model;
    private Date purchaseDate;

    // Todo: JsonMappingException: Can not construct instance of class: missing default constructor
    public Car() {}
    public Car(String color, String model, Date purchaseDate) {
        this.color = color;
        this.model  = model;
        this.purchaseDate = purchaseDate;
    }
    // Todo: JsonMappingException: No serializer found for class
    // the default configuration of an ObjectMapper instance is to
    // only access properties that are public fields or have public getters/setters.
    public String getColor() { return color; }
    public String getModel() { return model; }
    public Date getPurchaseDate() { return purchaseDate; }
}
