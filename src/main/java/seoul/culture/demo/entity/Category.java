package seoul.culture.demo.entity;

import java.util.Arrays;
import java.util.List;

public enum Category {
    공연(List.of("공연", "공연장")),
    전시(List.of("전시")),
    산책(List.of("산책")),
    체험(List.of("체험")),
    도서관(List.of("도서관")),
    전시미술(List.of("전시/미술", "미술관", "박물관/기념관")),
    교육체험(List.of("교육/체험")),
    영화(List.of("영화")),
    클래식(List.of("클래식")),
    뮤지컬오페라(List.of("뮤지컬/오페라")),
    기타(List.of("기타")),
    콘서트(List.of("콘서트")),
    공연장(List.of("공연장")),
    연극(List.of("연극")),
    축제문화예술(List.of("축제-문화/예술", "문화예술회관", "문화원")),
    독주독창회(List.of("독주/독창회")),
    무용(List.of("무용")),
    축제전통역사(List.of("축제-전통/역사")),
    축제시민화합(List.of("축제-시민화합")),
    국악(List.of("국악")),
    몰라(List.of("몰라"));

    private final List<String> names;
    Category(List<String> names) {
        this.names = names;
    }
    public List<String> getNames() {
        return names;
    }

    public static Category getCategoryByName(String name) {
        for (Category category : Category.values()) {
            if (category.getNames().contains(name)) {
                return category;
            }
        }
        return Category.몰라; // 일치하는 Category가 없을 경우 null 반환
    }
}