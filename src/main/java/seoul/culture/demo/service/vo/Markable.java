package seoul.culture.demo.service.vo;

import java.util.Map;

public interface Markable {
    String getMarkCategory();
    String getTitle();
    Map getLocation();
    String getContents();
    String getAddress();
    String getDetailUrl();
    String getContact();
    int getPrice();
}