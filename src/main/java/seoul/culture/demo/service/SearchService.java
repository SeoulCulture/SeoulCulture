package seoul.culture.demo.service;

import org.springframework.stereotype.Service;
import seoul.culture.demo.domain.Category;
import seoul.culture.demo.domain.CultureSearchForm;
import seoul.culture.demo.service.vo.Mark;
import seoul.culture.demo.service.vo.Markable;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    public List<Markable> search(CultureSearchForm cultureSearchForm) {
        // 임시 데이터
        Markable marker1 = Mark.builder()
                .category(Category.체험.toString())
                .latitude("37.123422")
                .longitude("127.533678")
                .title("마카롱 카페")
                .price(5000)
                .build();

        Markable marker2 = Mark.builder()
                .category(Category.공연.toString())
                .latitude("37.433321")
                .longitude("127.876335")
                .title("피자파티 공연")
                .price(15000)
                .build();

        // MarkerInfo 객체를 담는 List 생성
        List<Markable> testList = new ArrayList<>();
        testList.add(marker1);
        testList.add(marker2);
        return testList;
    }
}