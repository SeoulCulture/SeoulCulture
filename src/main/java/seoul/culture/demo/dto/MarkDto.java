package seoul.culture.demo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public final class MarkDto implements Markable {
    String id;
    String category;
    String title;
    double latitude;
    double longitude;
    String contents;
    String address;
    String imgUrl;
    String detailUrl;
    String contact;
    int price;
    LocalDate startDate;
    LocalDate endDate;

    @Override
    public String getMarkCategory() {
        return this.category;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Map<String, Double> getLocation() {
        Map<String, Double> map = new HashMap<>();
        map.put("latitude", this.latitude);
        map.put("longitude", this.longitude);
        return map;
    }

    @Override
    public String getContents() {
        return this.contents;
    }

    @Override
    public String getAddress() {
        return this.address;
    }
    @Override
    public String getImgUrl() {
        return this.imgUrl;
    }

    @Override
    public String getDetailUrl() {
        return this.detailUrl;
    }

    @Override
    public String getContact() {
        return this.contact;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}