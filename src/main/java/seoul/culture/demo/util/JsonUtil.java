package seoul.culture.demo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class JsonUtil {
    public static JsonNode parse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    public static JsonNode getResponseJson(URL url, Map<String, String> headerInfo) throws IOException {
        // CONNECTION
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (!headerInfo.isEmpty()){
            for (Map.Entry<String, String> entry : headerInfo.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        // RESULT
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // JSON
        return parse(sb.toString());
    }
}