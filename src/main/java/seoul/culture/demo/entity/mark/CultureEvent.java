package seoul.culture.demo.entity.mark;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import seoul.culture.demo.entity.Category;
import seoul.culture.demo.entity.Location;
import seoul.culture.demo.util.Formatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class CultureEvent extends Mark{

    public CultureEvent(Category category, String title, String imgUrl, String detailUrl, String contents, int price, LocalDate start, LocalDate end, Location loc) {
        super(category, title, imgUrl, detailUrl, contents, price, start, end, loc);
    }

    public CultureEvent() {
        super();
    }

    public static CultureEvent forSeoulEvent(JsonNode json) {
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

        return new CultureEvent(category, title, imgUrl, detailUrl, contents, price, start, end, Location.of(lat, lon));
    }
}
