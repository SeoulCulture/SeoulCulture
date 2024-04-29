package seoul.culture.demo.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.culture.demo.dto.CultureInfoDto;
import seoul.culture.demo.util.Formatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Culture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "culture_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String title;
    private String imgUrl;
    private String detailUrl;

    @Column(columnDefinition = "LONGTEXT")
    private String contents;
    private int price;
    private String contact;
    private LocalDate startDate;
    private LocalDate endDate;

    @Embedded
    private Location location;

    private String address;


    // TODO: 경준 - json파일 읽어서 Culture로 저장하기
    // 아래의 forSeoulEvent와 달리 address정보를 저장해야 한다.
    // 참고: (아래의 forSeoulEvent는 주소정보가 없에서 위경도를 통해 주소를 별도로 찾을 것이며,
    //       현재 구현되어있는 ReverseGeocoding을 통해 가능)
    public Culture(CultureInfoDto dto){
        this.category = dto.getCategory();
        this.title = dto.getTitle();
        this.imgUrl = dto.getImgUrl();
        this.detailUrl = dto.getDetailUrl();
        this.contents = dto.getContents();
        this.price = dto.getPrice();
        this.contact = dto.getContact();
        // startDate랑 endDate는 어카지...??
        this.location = dto.getLocation();
        this.address = dto.getAddress();
    }

    public static Culture forSeoulEvent(JsonNode json) {
        double lat = Formatter.latOrLon(json.get("LOT").toString());  // API문제점: LOT, LAT이 반대로 되어있음.
        double lon = Formatter.latOrLon(json.get("LAT").toString());
        int price = 0;
        try {
            price = Formatter.price(json.get("IS_FREE").toString());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            price = -1;
        }

        LocalDate start = null;
        LocalDate end = null;
        try {
            start = Formatter.date(json.get("STRTDATE").toString());
        } catch (NullPointerException e) {}
        try {
            end = Formatter.date(json.get("END_DATE").toString());
        } catch (NullPointerException e) {}

        List<String> variousInfo = new ArrayList<>();
        try {
            variousInfo.add("장소: " + Formatter.stringWithNoQuotes(json.get("PLACE").toString()));
        } catch (NullPointerException e) {}
        try {
            variousInfo.add("대상: " + Formatter.stringWithNoQuotes(json.get("USE_TRGT").toString()));
        } catch (NullPointerException e) {}
        try {
            String feeInfo = Formatter.stringWithNoQuotes(json.get("USE_FEE").toString());
            if (feeInfo.length() != 0) {
                variousInfo.add("비용: " + feeInfo);
            }
        } catch (NullPointerException e) {}
        String contents = Formatter.contents(variousInfo);

        Category category = null;
        try {
            category = Category.getCategoryByName(Formatter.stringWithNoQuotes(json.get("CODENAME").toString()));
        } catch (NullPointerException e) {
            category = Category.몰라;
        }

        String title = "제목 없음";
        try {
            title = Formatter.stringWithNoQuotes(json.get("TITLE").toString());
        } catch (NullPointerException e) {}

        String imgUrl = "";
        try {
            imgUrl = Formatter.stringWithNoQuotes(json.get("MAIN_IMG").toString());
        } catch (NullPointerException e) {}

        String detailUrl = "";
        try {
            detailUrl = Formatter.stringWithNoQuotes(json.get("HMPG_ADDR").toString());
        } catch (NullPointerException e) {}

        return Culture.builder()
                .category(category)
                .title(title)
                .imgUrl(imgUrl)
                .detailUrl(detailUrl)
                .contents(contents)
                .price(price)
                .startDate(start)
                .endDate(end)
                .location(Location.of(lat, lon))
                .build();
    }


    @Builder
    private Culture(Long id, Category category, String title, String imgUrl, String detailUrl, String contents, int price, String contact, LocalDate startDate, LocalDate endDate, Location location) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.imgUrl = imgUrl;
        this.detailUrl = detailUrl;
        this.contents = contents;
        this.price = price;
        this.contact = contact;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

}
