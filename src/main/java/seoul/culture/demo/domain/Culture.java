package seoul.culture.demo.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.culture.demo.service.api.Formatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static Culture of(JsonNode json) {
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
