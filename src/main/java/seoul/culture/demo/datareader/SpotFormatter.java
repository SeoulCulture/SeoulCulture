package seoul.culture.demo.datareader;

import java.util.List;
public class SpotFormatter {
    public static double parseLocationPoint(String str){
        return Double.parseDouble(str);
    }

    public static String parseSido(String str){
        return str.trim().replace("특별시", "")
                .replace("광역시", "")
                .replace("도", "")
                .replace("특별자치시", "");
    }

    public static String parseSpotName(List<String> spotInfo){
        return String.join(" ", spotInfo);
    }
}