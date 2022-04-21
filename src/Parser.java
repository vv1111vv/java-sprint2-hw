

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Parser {

    private final String delimeter;

    public Parser(String delimeter) {
        this.delimeter = delimeter;
    }

    public <T> List<T> load(InputStream is, Class<T> modelClass) throws Exception {
        final List<String> rows = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))
                .lines().collect(Collectors.toList());
        if (rows.size() < 2) {
            return new ArrayList<>();
        }
        final String[] columnsNames = rows.remove(0).split(delimeter);
        final List<T> valuesList = new ArrayList<>(rows.size());
        for (final String v : rows) {
            final String[] value = v.split(delimeter);
            final Map<String, String> values = new HashMap(value.length);
            for (int i = 0; i < value.length; i++) values.put(columnsNames[i], value[i]);
            final T t = modelClass.getDeclaredConstructor(Map.class).newInstance(values);
            valuesList.add(t);
        }
        return valuesList;
    }
}
