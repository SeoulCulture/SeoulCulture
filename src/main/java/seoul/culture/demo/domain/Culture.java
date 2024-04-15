package seoul.culture.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Culture {
    @Id @GeneratedValue
    @Column(name = "culture_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imgUrl;
    private String detailUrl;
    private String detailInfo;

    @Embedded
    private Location location;
}
