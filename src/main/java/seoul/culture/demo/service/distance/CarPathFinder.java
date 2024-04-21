package seoul.culture.demo.service.distance;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.net.*;

import static seoul.culture.demo.JsonUtil.getResponseJson;

@ConfigurationProperties(prefix = "gmap") // 스프링 컨테이너가 yml 정보를 통해서 bean을 등록할 때 필요
@NoArgsConstructor @Setter // Config 에 의한 Bean 생성을 위해 두가지가 필요
@Getter
public final class CarPathFinder implements PathFinder {
    private String key;
    private int distanceKm;
    private int durationMin;
    private String sourceAddress;
    private String destinationAddress;

    @Override
    public void loadPathInfo(double lat1, double lon1, double lat2, double lon2) throws IOException {
        String api = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&language=ko&mode=transit&origins=" +
                "%f,%f&destinations=" +
                "%f,%f&region=KR&key=" +
                "%s",lat1, lon1, lat2, lon2, key);
        URL url = new URL(api);

        JsonNode json = getResponseJson(url);

        JsonNode originAddressesNode = json.get("origin_addresses");
        JsonNode destinationAddressesNode = json.get("destination_addresses");
        JsonNode rowsNode = json.get("rows").get(0); // 'rows'는 배열이므로 첫 번째 요소 접근
        JsonNode elementsNode = rowsNode.get("elements").get(0); // 'elements'도 배열

        // 각 필드에서 필요한 데이터 추출
        sourceAddress = originAddressesNode.get(0).asText();
        destinationAddress = destinationAddressesNode.get(0).asText();
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
}
