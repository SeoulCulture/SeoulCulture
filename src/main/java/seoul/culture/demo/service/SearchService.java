package seoul.culture.demo.service;

import org.springframework.stereotype.Service;
import seoul.culture.demo.controller.dto.MarkerInfo;
import seoul.culture.demo.domain.CultureForm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    public List<MarkerInfo> search(CultureForm cultureForm) throws InterruptedException {
        // 임시 데이터
        MarkerInfo marker1 = MarkerInfo.builder()
                .category("카페")
                .latitude(new BigDecimal("37.123422"))
                .longitude(new BigDecimal("127.533678"))
                .name("마카롱 카페")
                .price(5000)
                .build();

        MarkerInfo marker2 = MarkerInfo.builder()
                .category("식당")
                .latitude(new BigDecimal("37.433321"))
                .longitude(new BigDecimal("127.876335"))
                .name("피자먹쟈")
                .price(15000)
                .build();

        // MarkerInfo 객체를 담는 List 생성
        List<MarkerInfo> testList = new ArrayList<>();
        testList.add(marker1);
        testList.add(marker2);
        Thread.sleep(2000);
        return testList;
    }

}
