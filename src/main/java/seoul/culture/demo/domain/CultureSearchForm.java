package seoul.culture.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CultureSearchForm {
    private List<String> moods = new ArrayList<>();
    private Integer time;
    private Integer price;
    private String latitude;
    private String longitude;
}