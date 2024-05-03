package seoul.culture.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import seoul.culture.demo.controller.SpotController;
import seoul.culture.demo.datareader.*;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.repository.CultureRepository;
import seoul.culture.demo.service.CultureService;
import seoul.culture.demo.service.MoodService;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final MoodService moodService;
    private final JsonReader seoulEventJsonReader;
    private final JsonReader subwayJsonReader;
    private final ExcelReader spotExcelReader;
    private final CultureRepository cultureRepository;
    private final CultureService cultureService;
    private final SpotController spotController;

    public DataLoader(MoodService moodService,
                      @Qualifier("seoulEventJsonReader") JsonReader seoulEventJsonReader,
                      @Qualifier("subwayJsonReader") JsonReader subwayJsonReader,
                      @Qualifier("spotExcelReader") ExcelReader spotExcelReader,
                      CultureRepository cultureRepository, CultureService cultureService, SpotController spotController) {
        this.moodService = moodService;
        this.seoulEventJsonReader = seoulEventJsonReader;
        this.subwayJsonReader = subwayJsonReader;
        this.spotExcelReader = spotExcelReader;
        this.cultureRepository = cultureRepository;
        this.cultureService = cultureService;
        this.spotController = spotController;
    }

    @Override
    public void run(String... args) throws IOException {
        // [문화장소]
        cultureService.cultureRegister();
        moodService.moodRegister();

        // [문화행사]
        // TODO 1: 스케쥴링
        // TODO 2: 스케줄링시 db 테이블 초기화
        List<Culture> cultures = seoulEventJsonReader.getResult();
//        cultures.stream().filter(culture -> !cultureRepository.existsByTitleAndLocation(
        // TODO: location이 동일하고, title이 다른 경우는.. 지도상에 겹치는데 그거 클릭 가능하게 처리하려면? (붙어있는 경우도 마찬가지긴 함)
        cultures.stream().filter(culture -> !cultureRepository.existsByLocation(
                culture.getLocation()
        )).forEach(cultureRepository::save);

        // [장소지정검색] 엑셀내용(장소정보) db에 반영 및 할당
        spotExcelReader.getResult();

        // [장소지정검색] 지하철역 db에 반영 및 할당
        subwayJsonReader.getResult();
    }
}