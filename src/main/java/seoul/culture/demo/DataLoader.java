package seoul.culture.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seoul.culture.demo.datareader.*;
import seoul.culture.demo.confusion.ConfusionService;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.service.CultureService;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final JsonReader seoulEventJsonReader;
    private final JsonReader subwayJsonReader;
    private final ExcelReader spotExcelReader;
    private final CultureService cultureService;
    private final ConfusionService confusionService;

    public DataLoader(@Qualifier("seoulEventJsonReader") JsonReader seoulEventJsonReader,
                      @Qualifier("subwayJsonReader") JsonReader subwayJsonReader,
                      @Qualifier("spotExcelReader") ExcelReader spotExcelReader,
                      CultureService cultureService, ConfusionService confusionService) {
        this.seoulEventJsonReader = seoulEventJsonReader;
        this.subwayJsonReader = subwayJsonReader;
        this.spotExcelReader = spotExcelReader;
        this.cultureService = cultureService;
        this.confusionService = confusionService;
    }

    @Override
    public void run(String... args) throws IOException {
        // [문화장소]
        cultureService.registerCulturePlace();

        // [문화행사]  참고: 키워드 추출 대상
        registerCultureEvent();   // (아래에 스케줄러도 있음)

        // [장소지정검색] 엑셀내용(장소정보) db에 반영 및 할당
        spotExcelReader.getResult();

        // [장소지정검색] 지하철역 db에 반영 및 할당
        subwayJsonReader.getResult();

        // [혼잡도] ConfusionArea 엔티티 생성하기
        confusionService.registerConfusionArea();  // 혼잡도 영역 기본세팅
        confusionService.getRealTimeJson(); // 실시간 데이터 (아래 스케줄러도 있음)
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 문화행사 업데이트
    public void registerCultureEvent() throws IOException {
        List<CultureEvent> cultures = seoulEventJsonReader.getResult();
        cultureService.registerCultureEvent(cultures);
        System.out.println("Data updated at 2:00 AM");
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void registerRealTimeConfusionInMemory() throws IOException {
        confusionService.getRealTimeJson();
    }
}