package seoul.culture.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import seoul.culture.demo.datareader.*;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.repository.CultureEventRepository;
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
    private final CultureService cultureService;

    public DataLoader(MoodService moodService,
                      @Qualifier("seoulEventJsonReader") JsonReader seoulEventJsonReader,
                      @Qualifier("subwayJsonReader") JsonReader subwayJsonReader,
                      @Qualifier("spotExcelReader") ExcelReader spotExcelReader,
                      CultureService cultureService) {
        this.moodService = moodService;
        this.seoulEventJsonReader = seoulEventJsonReader;
        this.subwayJsonReader = subwayJsonReader;
        this.spotExcelReader = spotExcelReader;
        this.cultureService = cultureService;
    }

    @Override
    public void run(String... args) throws IOException {
        // [문화장소]
        cultureService.registerCulturePlace();
        moodService.moodRegister();

        // [문화행사]
        // TODO 1: 스케쥴링
        // TODO 2: 스케줄링시 db 테이블 초기화
        List<CultureEvent> cultures = seoulEventJsonReader.getResult();
        cultureService.registerCultureEvent(cultures);

        // [장소지정검색] 엑셀내용(장소정보) db에 반영 및 할당
        spotExcelReader.getResult();

        // [장소지정검색] 지하철역 db에 반영 및 할당
        subwayJsonReader.getResult();
    }
}