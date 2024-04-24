package seoul.culture.demo.service.distance;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static seoul.culture.demo.JsonUtil.getResponseJson;

@Component
@Slf4j
public class NaverPathFinder implements PathFinder {
    private final NaverConfig naverConfig;
    private final ReverseGeocoding reverseGeocoding;

    private int distanceKm;
    private int durationMin;
    private double srcLat;
    private double dstLat;
    private double srcLon;
    private double dstLon;


    public NaverPathFinder(NaverConfig naverConfig, ReverseGeocoding reverseGeocoding) {
        this.naverConfig = naverConfig;
        this.reverseGeocoding = reverseGeocoding;
    }

    @Override
    public void setPathInfo(double lat1, double lon1, double lat2, double lon2, HowToGo howToGo) throws IOException {
        if (howToGo != HowToGo.DRIVING) return;
        this.srcLat = lat1;
        this.srcLon = lon1;
        this.dstLat = lat2;
        this.dstLon = lon2;

        String api = String.format("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?" +
                "start=%f,%f&goal=%f,%f", lon1, lat1, lon2, lat2);

        URL url = new URL(api);
        Map<String, String> headers = new HashMap<>();

        headers.put("X-NCP-APIGW-API-KEY-ID", naverConfig.getId());
        headers.put("X-NCP-APIGW-API-KEY", naverConfig.getSecret());

        JsonNode json = getResponseJson(url, headers);
        json = json.get("route").get("traoptimal").get(0).get("summary");

        this.distanceKm = json.get("distance").asInt() / 1000;
        this.durationMin = json.get("duration").asInt() / 1000 / 60;
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