package seoul.culture.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import seoul.culture.demo.controller.SpotController;
import seoul.culture.demo.datareader.SpotDto;
import seoul.culture.demo.datareader.SpotExcelReader;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.repository.CultureRepository;
import seoul.culture.demo.datareader.JsonReader;
import seoul.culture.demo.service.CultureService;
import seoul.culture.demo.service.MoodService;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MoodService moodService;
    private final JsonReader jsonReader;
    private final CultureRepository cultureRepository;
    private final CultureService cultureService;
    private final SpotExcelReader spotExcelReader;
    private final SpotController spotController;

    @Override
    public void run(String... args) throws IOException {
        cultureService.cultureRegister();
        moodService.moodRegister();

        // TODO 1: 스케쥴링
        // TODO 2: 스케줄링시 db 테이블 초기화
        log.debug("----------> 문화행사 API 불러오기 -> DB 세팅 중...");
        List<JsonNode> jsons = jsonReader.getResult();
        List<Culture> cultures = jsons.stream()
                .map(Culture::forSeoulEvent)
                .toList();
//        cultures.stream().filter(culture -> !cultureRepository.existsByTitleAndLocation(
        // TODO: location이 동일하고, title이 다른 경우는.. 지도상에 겹치는데 그거 클릭 가능하게 처리하려면? (붙어있는 경우도 마찬가지긴 함)
        cultures.stream().filter(culture -> !cultureRepository.existsByLocation(
                culture.getLocation()
        )).forEach(cultureRepository::save);

        // 엑셀 내용 db에 반영 및 할당
        List<SpotDto> result = spotExcelReader.getResult("src/main/resources/KoreaRegionData.xlsx");
        spotController.setSpotDtos(result);
    }
}