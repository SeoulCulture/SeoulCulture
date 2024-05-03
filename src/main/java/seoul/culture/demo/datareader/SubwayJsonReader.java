package seoul.culture.demo.datareader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.culture.demo.entity.Spot;
import seoul.culture.demo.repository.SpotRepository;
import seoul.culture.demo.util.Formatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubwayJsonReader implements JsonReader {

    private final String SUBWAY_DATA_PATH = "src/main/resources/seoul_subway.json";
    private final SpotRepository spotRepository;

    public List<Spot> getResult() throws IOException {
        List<Spot> spots = createResult();
        spots.stream().filter(x -> !spotRepository.existsBySpotName(x.getSpotName()))
                .forEach(spotRepository::save);
        return spots;
    }

    private List<Spot> createResult() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(SUBWAY_DATA_PATH));

        JsonNode dataNode = rootNode.get("DATA");
        List<Spot> spots = new ArrayList<>();
        for (JsonNode node : dataNode) {
            String spotName = node.get("statn_nm").asText();
            spotName = spotName.endsWith("역") ? spotName : spotName + "역";
            String sido = node.get("route").asText();
            String latitude = node.get("crdnt_y").asText();
            String longitude = node.get("crdnt_x").asText();
            spots.add(Spot.of(spotName, sido, Formatter.latOrLon(latitude), Formatter.latOrLon(longitude)));
        }
        return spots;
    }
}