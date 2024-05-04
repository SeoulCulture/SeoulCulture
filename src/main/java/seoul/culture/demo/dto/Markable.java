package seoul.culture.demo.dto;

import java.util.Map;

public interface Markable {
    String getMarkCategory();
    String getTitle();
    Map getLocation();
    String getContents();
    String getAddress();
    String getImgUrl();
    String getDetailUrl();
    String getContact();
    int getPrice();

    void setId(String id);
}