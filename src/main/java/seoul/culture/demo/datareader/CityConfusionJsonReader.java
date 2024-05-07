package seoul.culture.demo.datareader;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.culture.demo.config.SeoulApiConfig;
import seoul.culture.demo.util.Formatter;
import seoul.culture.demo.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor // 실시간 갱신됨
public class CityConfusionJsonReader implements JsonReader {

    private final DateTimeFormatter updateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SeoulApiConfig seoulApiConfig;
    private final List<String> poiList = List.of(
            "POI001", "POI002", "POI003", "POI004", "POI005",
            "POI006", "POI007", "POI008", "POI009", "POI010",
            "POI011", "POI012", "POI013", "POI014", "POI015",
            "POI016", "POI017", "POI018", "POI019", "POI020",
            "POI021", "POI022", "POI023", "POI024", "POI025",
            "POI026", "POI027", "POI028", "POI029", "POI030",
            "POI031", "POI032", "POI033", "POI034", "POI035",
            "POI036", "POI037", "POI038", "POI039", "POI040",
            "POI041", "POI042", "POI043", "POI044", "POI045",
            "POI046", "POI047", "POI048", "POI049", "POI050",
            "POI051", "POI052", "POI053", "POI054", "POI055",
            "POI056", "POI057", "POI058", "POI059", "POI060",
            "POI061", "POI062", "POI063", "POI064", "POI065",
            "POI066", "POI067", "POI068", "POI069", "POI070",
            "POI071", "POI072", "POI073", "POI074", "POI075",
            "POI076", "POI077", "POI078", "POI079", "POI080",
            "POI081", "POI082", "POI083", "POI084", "POI085",
            "POI086", "POI087", "POI088", "POI089", "POI090",
            "POI091", "POI092", "POI093", "POI094", "POI095",
            "POI096", "POI097", "POI098", "POI099", "POI100",
            "POI101", "POI102", "POI103", "POI104", "POI105",
            "POI106", "POI107", "POI108", "POI109", "POI110",
            "POI111", "POI112", "POI113", "POI114", "POI115"
    );

    @Override
    public List getResult() throws IOException {

        List<ConfusionDto> confusionDtos = new ArrayList<>();
        for (String poi : poiList) {
            String jsonStr = read(poi);
            JsonNode responseJson = JsonUtil.parse(jsonStr);
            JsonNode contents = responseJson.get("SeoulRtd.citydata_ppltn").get(0);

            // create DTO
            ConfusionDto dto = ConfusionDto.builder()
                    .name(contents.get("AREA_NM").toString())
                    .code(contents.get("AREA_CD").toString())
                    .confusion(contents.get("AREA_CONGEST_LVL").toString())
                    .confusionMsg(contents.get("AREA_CONGEST_MSG").toString())
                    .maleRate(Formatter.toDouble(contents.get("MALE_PPLTN_RATE").toString()))
                    .femaleRate(Formatter.toDouble(contents.get("FEMALE_PPLTN_RATE").toString()))
                    .rateIn0to10(Formatter.toDouble(contents.get("PPLTN_RATE_10").toString()))
                    .rateIn10(Formatter.toDouble(contents.get("PPLTN_RATE_20").toString()))
                    .rateIn20(Formatter.toDouble(contents.get("PPLTN_RATE_30").toString()))
                    .rateIn30(Formatter.toDouble(contents.get("PPLTN_RATE_40").toString()))
                    .rateIn40(Formatter.toDouble(contents.get("PPLTN_RATE_50").toString()))
                    .rateIn50(Formatter.toDouble(contents.get("PPLTN_RATE_50").toString()))
                    .rateIn60(Formatter.toDouble(contents.get("PPLTN_RATE_60").toString()))
                    .rateIn70(Formatter.toDouble(contents.get("PPLTN_RATE_70").toString()))
                    .resentRate(Formatter.toDouble(contents.get("RESNT_PPLTN_RATE").toString()))
                    .nonResentRate(Formatter.toDouble(contents.get("NON_RESNT_PPLTN_RATE").toString()))
                    .updateTime(LocalDateTime.parse(contents.get("PPLTN_TIME").toString(), updateTimeFormatter))
                    .build();

            confusionDtos.add(dto);
        }
        return confusionDtos;
    }

    private String read(String poi) throws IOException {
        // API URL
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(seoulApiConfig.getKey(), "UTF-8"))
                .append("/" + URLEncoder.encode("json", "UTF-8"))
                .append("/" + URLEncoder.encode("citydata_ppltn", "UTF-8"))
                .append("/" + URLEncoder.encode("1", "UTF-8"))
                .append("/" + URLEncoder.encode("5", "UTF-8"))
                .append("/" + URLEncoder.encode(poi, "UTF-8"));
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
}