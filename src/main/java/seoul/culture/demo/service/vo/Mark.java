package seoul.culture.demo.service.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public final class Mark implements Markable {
    String category;
    String title;
    String latitude;
    String longitude;
    String contents;
    String address;
    String imgUrl;
    String detailUrl;
    String contact;
    int price;

    @Override
    public String getMarkCategory() {
        return this.category;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Map<String, String> getLocation() {
        Map<String, String> map = new HashMap<>();
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
}