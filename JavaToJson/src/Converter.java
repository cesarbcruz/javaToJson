import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class Converter {

    String[] wordsRemove = {"private", "public", "protected", "final", ";", "=", "\""};
    private static Map<String, String> simpleTypeAndExample = new HashMap<String, String>();

    static {
        simpleTypeAndExample.put(String.class.getSimpleName(), "\"string\"");
        simpleTypeAndExample.put(Integer.class.getSimpleName(), "0");
        simpleTypeAndExample.put(Double.class.getSimpleName(), "0.0");
        simpleTypeAndExample.put(BigDecimal.class.getSimpleName(), "0.0");
        simpleTypeAndExample.put(Float.class.getSimpleName(), "0.0");
        simpleTypeAndExample.put(Boolean.class.getSimpleName(), "false");
        simpleTypeAndExample.put(Date.class.getSimpleName(), "\"2018-09-03\"");
        simpleTypeAndExample.put(Timestamp.class.getSimpleName(), "\"2018-09-03 08:25:00\"");
        simpleTypeAndExample.put(Time.class.getSimpleName(), "\"08:25:00\"");
        simpleTypeAndExample.put("int", "0");
        simpleTypeAndExample.put("double", "0.0");
        simpleTypeAndExample.put("char", "char");
        simpleTypeAndExample.put("boolean", "false");
    }

    String[] collectionType = {
            "List", "Map", "Vector"
    };

    public String javaToJson(String javaSource) {
        StringBuilder json = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new StringReader(javaSource));
        String line = null;
        json.append("{").append("\n");
        try {
            while ((line = bufReader.readLine()) != null) {
                String newLine = line;
                for (String word : wordsRemove) {
                    newLine = newLine.replace(word, "");
                }
                String[] words = newLine.trim().split(" ");
                json.append("\"").append(words[1]).append("\"").append(" : ").append(formatValueType(words[0])).append(",").append("\n");
            }
        } catch (Exception e) {
            json.append("ERROR CONVERT LINE: " + line).append(",").append("\n");
        }
        json.setLength(json.length() - 2);
        json.append("\n").append("}");
        return json.toString();
    }

    private String formatValueType(String type) {
        StringBuilder value = new StringBuilder();
        if (simpleTypeAndExample.containsKey(type)) {
            value.append(simpleTypeAndExample.get(type));
        } else if (isCollection(type)) {
            value.append("[ ]");
        } else {
            value.append("{ }");
        }
        return value.toString();
    }

    private boolean isCollection(String type) {
        for (String collection : collectionType) {
            if (type.trim().startsWith(collection)) {
                return true;
            }
        }
        return false;
    }
}
