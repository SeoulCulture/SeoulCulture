package seoul.culture.demo.confusion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfusionArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poi;
    private String name;
    private String eng;
    @Convert(converter = AreaListConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private List<String> areaData;
}
