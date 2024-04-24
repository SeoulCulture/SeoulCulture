package seoul.culture.demo.service.distance;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static seoul.culture.demo.JsonUtil.getResponseJson;

@Component
@Slf4j
public final class GooglePathFinder implements PathFinder {
    private final GoogleConfig googleConfig;
    private final ReverseGeocoding reverseGeocoding;
    private int distanceKm;
    private int durationMin;
    private double srcLat;
    private double dstLat;
    private double srcLon;
    private double dstLon;

    public GooglePathFinder(GoogleConfig googleConfig, ReverseGeocoding reverseGeocoding) {
        this.googleConfig = googleConfig;
        this.reverseGeocoding = reverseGeocoding;
    }

    @Override
    public void setPathInfo(double lat1, double lon1, double lat2, double lon2, HowToGo howToGo) throws IOException {
        String mode = "";
        if (howToGo == HowToGo.TRANSIT)  // 한국에서는 transit밖에 안된다는 것 같다. waling이나 driving, bicycling 지원하지 않는 듯
            mode = "transit";

        this.srcLat = lat1;
        this.srcLon = lon1;
        this.dstLat = lat2;
        this.dstLon = lon2;

        String api = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&language=ko&mode=%s&origins=" +
                "%f,%f&destinations=" +
                "%f,%f&region=KR&key=" +
                "%s", mode, lat1, lon1, lat2, lon2, googleConfig.getKey());
        URL url = new URL(api);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");

        JsonNode json = getResponseJson(url, headers);
        System.out.println(json.toString());

        JsonNode rowsNode = json.get("rows").get(0); // 'rows'는 배열이므로 첫 번째 요소 접근
        JsonNode elementsNode = rowsNode.get("elements").get(0); // 'elements'도 배열
        if (elementsNode.has("status")) {
            if (elementsNode.get("status").asText().equals("ZERO_RESULTS"))
                log.debug("ZERO_RESULTS!");
            return;
        }

        // 각 필드에서 필요한 데이터 추출
        distanceKm = elementsNode.get("distance").get("value").asInt() / 1000;
        durationMin = elementsNode.get("duration").get("value").asInt() / 60;
    }

    @Override
    public int getDuration() {
        return this.durationMin;
    }

    @Override
    public int getDistance() {
        return this.distanceKm;
    }

    @Override
    public Map<String, String> getSrcAddress() throws IOException {
        return reverseGeocoding.getAddress(srcLat, srcLon);
    }

    @Override
    public Map<String, String> getDstAddress() throws IOException {
        return reverseGeocoding.getAddress(dstLat, dstLon);
    }
}