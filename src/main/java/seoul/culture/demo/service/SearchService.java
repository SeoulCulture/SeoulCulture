package seoul.culture.demo.service;

import org.springframework.stereotype.Service;
import seoul.culture.demo.domain.Category;
import seoul.culture.demo.domain.CultureSearchForm;
import seoul.culture.demo.service.vo.MarkDto;
import seoul.culture.demo.service.vo.Markable;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    public List<Markable> search(CultureSearchForm cultureSearchForm) {
        // 임시 데이터
        Markable marker1 = MarkDto.builder()
                .category(Category.체험.toString())
                .latitude(37.123422)
                .longitude(127.533678)
                .title("마카롱 카페")
                .price(5000)
                .contents("마카롱을 정말 맛있게 만들 수 있는 곳이요")
                .imgUrl("https://cafe24.poxo.com/ec01/joinandjoin3/YepDBcpQi6F1EGuL9rzRwSOX6iBaegInadJOBqJjBl8mIPOKixffqoe/SEB3Y+EmfOLxV9OtXy9ki4TqHva87Q==/_/web/product/big/202208/efb8871f1a21b518e1e797acaf8d3c21.png")
                .detailUrl("www.naver.com")
                .build();

        Markable marker2 = MarkDto.builder()
                .category(Category.공연.toString())
                .latitude(37.433321)
                .longitude(127.876335)
                .title("피자파티 공연")
                .price(15000)
                .contents("피자파티가열린다야호@!")
                .imgUrl("https://cdn.dominos.co.kr/admin/upload/goods/20200311_x8StB1t3.jpg")
                .detailUrl("www.naver.com")
                .build();

        // MarkerInfo 객체를 담는 List 생성
        List<Markable> testList = new ArrayList<>();
        testList.add(marker1);
        testList.add(marker2);
        return testList;
    }
}