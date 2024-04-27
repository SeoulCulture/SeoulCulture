package seoul.culture.demo.pathfinder;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import seoul.culture.demo.config.NaverConfig;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static seoul.culture.demo.util.JsonUtil.getResponseJson;

@Component
public class ReverseGeocoding { // 좌표정보를 주소로 바꾸는 네이버의 api 이름입니다
    private final NaverConfig naverConfig;
    public ReverseGeocoding(NaverConfig naverConfig) {
        this.naverConfig = naverConfig;
    }

    public Map<String, String> getAddress(double latitude, double longitude) throws IOException {

        String api = String.format("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?" +
                        "coords=%f,%f&" +
                        "orders=%s&" +
                        "output=%s", longitude, latitude, "roadaddr", "json");

        URL url = new URL(api);
        Map<String, String> headers = new HashMap<>();

        headers.put("X-NCP-APIGW-API-KEY-ID", naverConfig.getId());
        headers.put("X-NCP-APIGW-API-KEY", naverConfig.getSecret());

        JsonNode json = getResponseJson(url, headers);
        JsonNode regionInfo = json.get("results").get(0).get("region");

        String sido = regionInfo.get("area1").get("name").asText(); // 경기도
        String sigungu = regionInfo.get("area2").get("name").asText(); // OO시 OO구
        String eupmyeandong = regionInfo.get("area3").get("name").asText(); // OO동
        String ri = regionInfo.get("area4").get("name").asText(); // OO리
        String doro = json.get("results").get(0).get("land").get("name").asText();

        return new HashMap<>() {{
            put("sido", sido);
            put("sigungu", sigungu);
            put("eupmyeandong", eupmyeandong);
            put("ri", ri);
            put("doro", doro);
        }};
    }
}