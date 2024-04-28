package seoul.culture.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import seoul.culture.demo.entity.Category;
import seoul.culture.demo.entity.Location;

import java.math.BigDecimal;

public class CultureInfoDto {
    private Category category;
    private Location location;
    private String title;
    private String imgUrl;
    private String price;
    private String contents;
    private String address;
    private String detailUrl;
    private String contact;

    public CultureInfoDto(String category, double latitude, double longitude, String title, String imgUrl,
                          String price, String contents, String address, String detailUrl, String contact) {
        this.category = Category.valueOf(category);
        this.location = Location.of(latitude, longitude);
        this.title = title;
        this.imgUrl = imgUrl;
        this.price = price;
        this.contents = contents;
        this.address = address;
        this.detailUrl = detailUrl;
        this.contact = contact;
    }
}
