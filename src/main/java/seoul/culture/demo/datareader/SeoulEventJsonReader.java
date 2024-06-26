package seoul.culture.demo.datareader;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.culture.demo.config.SeoulApiConfig;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.util.JsonUtil;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Component
@Getter
@RequiredArgsConstructor
public final class SeoulEventJsonReader implements JsonReader {
    private final SeoulApiConfig apiConfig;
    private final LocalDate FIRST_DAY = LocalDate.now().with(firstDayOfYear());
    private final LocalDate LAST_DAY = LocalDate.now().with(lastDayOfYear());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter endTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    @Override
    public List<CultureEvent> getResult() throws IOException {
        return createResult(FIRST_DAY, LAST_DAY);
    }

    private List<CultureEvent> createResult(LocalDate start, LocalDate end) throws IOException {
        Set<String> set = new HashSet<>();
        List<JsonNode> jsons = new ArrayList<>();
        LocalDate date = start;
        while (date.isBefore(end) || date.isEqual(end)) {
            String response = read(date);
            // 응답 결과 확인하기
            JsonNode responseJson = JsonUtil.parse(response);
            // 각 요소 얻기
            JsonNode contents = null;
            try {
                contents = responseJson.get("culturalEventInfo").get("row");
            } catch (NullPointerException e) {
                date = date.plusDays(1);
                continue;
            }
            for (JsonNode json : contents) {
                String endTimeStr = json.get("END_DATE").toString().replace("\"", "");
                LocalDate endDate = LocalDateTime.parse(endTimeStr, endTimeFormatter).toLocalDate();
                if (endDate.isBefore(LocalDate.now())) {
                    continue;
                }
                set.add(json.toString());
                jsons.add(json);
            }
            date = date.plusDays(1);
        }
        System.out.println("2024년 모든 데이터 개수: " + set.size());

        List<CultureEvent> cultureEvents = jsons.stream()
                .map(CultureEvent::forSeoulEvent)
                .toList();
        return cultureEvents;
    }

    private String read(LocalDate date) throws IOException {
        String dateFormat = date.format(formatter);

        // API URL
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(apiConfig.getKey(), "UTF-8"))
                .append("/" + URLEncoder.encode("json", "UTF-8"))
                .append("/" + URLEncoder.encode("culturalEventInfo", "UTF-8"))
                .append("/" + URLEncoder.encode("1", "UTF-8"))
                .append("/" + URLEncoder.encode("1000", "UTF-8"))
                .append("/" + URLEncoder.encode(" ", "UTF-8"))
                .append("/" + URLEncoder.encode(" ", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(dateFormat,"UTF-8"));
        URL url = new URL(urlBuilder.toString());

        // CONNECTION
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
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

        return sb.toString();
    }

    // 조건: date검색을 현재날짜로 하면, 결과가 안 나올수도 있다.
    // 예를 들어 3월12일부터 5월20일까지인 행사가 있을 때,
    // 4월 20일로 검색하면, 안 나오더라..
    // 어쩌지.. 다 받아와서 하루마다 파싱해두기? 이것도 괜찮을 것 같다.
    // 즉 2024-01-01부터 12-31까지 빠짐없이, 중복없이 받아와 처리하기.
}
