package seoul.culture.demo.entity.mark;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import seoul.culture.demo.dto.CultureInfoDto;
import seoul.culture.demo.entity.Category;
import seoul.culture.demo.entity.Location;

import java.time.LocalDate;

@Entity @Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn // 하위 테이블의 구분 컬럼 생성(default = DTYPE)
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id")
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

    private String address;

    public Mark(CultureInfoDto dto){
        this.category = dto.getCategory();
        this.title = dto.getTitle();
        this.imgUrl = dto.getImgUrl();
        this.detailUrl = dto.getDetailUrl();
        this.contents = dto.getContents();
        this.price = dto.getPrice();
        this.contact = dto.getContact();
        // startDate랑 endDate는 어카지...??
        this.location = dto.getLocation();
        this.address = dto.getAddress();
    }

    @Builder
    Mark(Category category, String title, String imgUrl, String detailUrl, String contents, int price, LocalDate startDate, LocalDate endDate, Location location) {
        this.category = category;
        this.title = title;
        this.imgUrl = imgUrl;
        this.detailUrl = detailUrl;
        this.contents = contents;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

    public Mark() {
    }
}