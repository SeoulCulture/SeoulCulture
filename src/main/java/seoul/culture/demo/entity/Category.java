package seoul.culture.demo.entity;

public enum Category {
    공연, 전시, 산책, 체험, 전시미술("전시/미술"), 교육체험("교육/체험"), 영화, 클래식, 뮤지컬오페라("뮤지컬/오페라"), 기타, 콘서트, 연극, 축제문화예술("축제-문화/예술"), 독주독창회("독주/독창회"), 무용, 축제전통역사("축제-전통/역사"), 축제시민화합("축제-시민화합"), 국악, 몰라;

    private final String name;
    Category() {
        this.name = name(); // enum 상수의 이름을 사용
    }
    Category(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static Category getCategoryByName(String name) {
        for (Category category : Category.values()) {
            if (category.getName().contains(name) || name.contains(category.getName())) {
                return category;
            }
        }
        return null; // 일치하는 Category가 없을 경우 null 반환
    }
}