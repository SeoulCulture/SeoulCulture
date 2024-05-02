package seoul.culture.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import seoul.culture.demo.entity.Category;
import seoul.culture.demo.entity.Location;
import seoul.culture.demo.util.Formatter;

import java.math.BigDecimal;

@Getter
public class CultureInfoDto {
    private Category category;
    private Location location;
    private String title;
    private String imgUrl;
    private Integer price;
    private String contents;
    private String address;
    private String detailUrl;
    private String contact;

    @JsonCreator
    public CultureInfoDto(@JsonProperty("category") String category,
                          @JsonProperty("org_name") String orgName,
                          @JsonProperty("latitude") double latitude,
                          @JsonProperty("longitude") double longitude,
                          @JsonProperty("title") String title,
                          @JsonProperty("imgUrl") String imgUrl,
                          @JsonProperty("price") Integer price,
                          @JsonProperty("contents") String contents,
                          @JsonProperty("address") String address,
                          @JsonProperty("detail-url") String detailUrl,
                          @JsonProperty("contact") String contact) {
        this.category = Category.getCategoryByName(category);
        this.location = Location.of(latitude, longitude);
        this.title = title;
        this.imgUrl = imgUrl;
        this.price = price == null ? -1 : price;
        this.contents = contents != null ? Formatter.stringWithNoQuotes(contents) : null;
        this.address = address;
        this.detailUrl = detailUrl;
        this.contact = contact;
    }
}
