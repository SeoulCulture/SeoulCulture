package seoul.culture.demo.개발중;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import seoul.culture.demo.config.PathConfig;
import seoul.culture.demo.datareader.ConfusionDto;
import seoul.culture.demo.util.Formatter;

@Service
public class ConfusionService {

    private List<ConfusionResponseDto> realTimeConfusions;
    private final CityConfusionJsonReader cityConfusionJsonReader;
    private final ConfusionAreaRepository confusionAreaRepository;

    public List<ConfusionResponseDto> getRealTimeConfusions() throws IOException {
        if (realTimeConfusions == null || realTimeConfusions.size() == 0) {
            getRealTimeJson();
        }
        return realTimeConfusions;
    }

    public ConfusionService(CityConfusionJsonReader cityConfusionJsonReader, ConfusionAreaRepository confusionAreaRepository) {
        this.cityConfusionJsonReader = cityConfusionJsonReader;
        this.confusionAreaRepository = confusionAreaRepository;
    }

    public void setRealTimeConfusions(List<ConfusionResponseDto> realTimeConfusions) {
        this.realTimeConfusions = realTimeConfusions;
    }

    @Transactional
    public void getRealTimeJson() throws IOException {
        List<ConfusionDto> results = cityConfusionJsonReader.getResult();

        //실시간으로 받아온 ConfusionDto List를, Area정보와 매핑하여 응답데이터 만들기
        List<ConfusionResponseDto> response = new ArrayList<>();
        for (ConfusionDto result : results) {
            ConfusionArea areaData = confusionAreaRepository.getByPoi(result.getPoi());
            if (areaData.getAreaData().size() == 0) continue;
            ConfusionResponseDto res = ConfusionResponseDto.builder()
                    .poi(areaData.getPoi())
                    .name(areaData.getName())
                    .eng(areaData.getEng())
                    .areas(areaData.getAreaData())
                    .confusionData(result).build();
            response.add(res);
        }
        setRealTimeConfusions(response);
    }


    // 여기서부터는 초기에만 필요한 ConfusionArea 테이블 세팅.
    @Transactional
    public void registerConfusionArea() {
        // 임시 삭제: 지우기 꼮!
        confusionAreaRepository.deleteAll();

        List<ConfusionArea> confusionAreas = readPoiData(PathConfig.CONFUSION_PATH);
        confusionAreas.stream().filter(area -> !confusionAreaRepository.existsByPoi(area.getPoi()))
                .forEach(confusionAreaRepository::save);
        System.out.println("혼잡도 표시대상 Area 개수:" + confusionAreas.size());
    }

    private static List<ConfusionArea> readPoiData(String filename) {
        List<ConfusionArea> confusionAreas = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(filename));

            // 각 POI 데이터를 ConfusionArea 엔터티로 변환하여 리스트에 추가
            for (JsonNode node : rootNode) {
                ConfusionArea area = new ConfusionArea();
                area.setPoi(Formatter.stringWithNoQuotes(node.get("poi").asText()));
                area.setName(Formatter.stringWithNoQuotes(node.get("name").asText()));
                area.setEng(Formatter.stringWithNoQuotes(node.get("eng").asText()));

                // "area" 필드를 "areaData"로 변환하여 설정
                List<String> areaData = new ArrayList<>();
                for (JsonNode areaNode : node.get("area")) {
                    areaData.add(Formatter.stringWithNoQuotes(areaNode.toString()));
                }
                area.setAreaData(areaData);
                confusionAreas.add(area);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return confusionAreas;
    }
}
