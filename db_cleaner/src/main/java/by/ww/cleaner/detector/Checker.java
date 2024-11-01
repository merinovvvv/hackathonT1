package by.ww.cleaner.detector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Checker {
    private final Set<String> confidentialFields;

    public Checker(String filePath) throws IOException, ParseException {
        confidentialFields = new HashSet<>();
        loadConfidentialFields(filePath);
    }

    private void loadConfidentialFields(String filePath) throws IOException, ParseException{
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray fieldsArray = (JSONArray) jsonObject.get("fields");

            for (Object field : fieldsArray) {
                confidentialFields.add(field.toString().toLowerCase());
            }
        }
    }

    public boolean isConfidential(String fieldName) {
        return confidentialFields.contains(fieldName.toLowerCase());
    }
}