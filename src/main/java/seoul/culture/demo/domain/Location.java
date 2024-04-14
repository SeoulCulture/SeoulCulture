package seoul.culture.demo.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Location {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
