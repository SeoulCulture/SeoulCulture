package seoul.culture.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CultureForm {
    private List<String> moods = new ArrayList<>();
    private Integer time;
    private Integer price;
}
