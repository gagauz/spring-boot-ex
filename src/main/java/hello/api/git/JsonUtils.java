package hello.api.git;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonUtils {
    public static Optional<?> parseMap(Map<String, ?> map, String path) {
        String[] paths = path.split("/");
        Object value = map;
        for (String p : paths) {
            value = ((Map<String, ?>) value).get(p);
            if (null == value) {
                break;
            }
        }
        return Optional.ofNullable(value);
    }

    public static Optional<Map<String, ?>> parseObject(Map<String, ?> map, String path) {
        return (Optional<Map<String, ?>>) parseMap(map, path);
    }

    public static Optional<Integer> parseInt(Map<String, ?> map, String path) {
        return (Optional<Integer>) parseMap(map, path);
    }

    public static Optional<List<Map<String, ?>>> parseArray(Map<String, ?> map, String path) {
        return (Optional<List<Map<String, ?>>>) parseMap(map, path);
    }

}
