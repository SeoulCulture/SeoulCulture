package seoul.culture.demo.entity.mark;

import jakarta.persistence.*;
import lombok.Getter;
import seoul.culture.demo.dto.CultureInfoDto;
@Getter
@Entity
public class CulturePlace extends Mark {

    public CulturePlace(CultureInfoDto dto) {
        super(dto);
    }

    public CulturePlace() {
        super();
    }
}