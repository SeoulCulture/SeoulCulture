package seoul.culture.demo.service.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Formatter {

    public static String stringWithNoQuotes(String str) {
        return str.trim().replaceAll("^\"|\"$", "");
    }

    public static String string(String str) {
        return str.trim();
    }

    public static double latOrLon(String str){
        str = string(str);
        return Double.parseDouble(str.trim().replaceAll("^\"|\"$", ""));
    }
    public static LocalDate date(String str){
        str = stringWithNoQuotes(str);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
        return localDateTime.toLocalDate();
    }

    public static int price(String str) {
        str = stringWithNoQuotes(str);
        return str.equals("무료") ? 0 : 1;
    }

    public static String contents(List<String> variousInfo) {
        if (variousInfo.size() == 0) {
            return "정보 없음";
        }
        StringBuilder sb = new StringBuilder();
        for (String info : variousInfo) {
            sb.append(info).append("\n");
        }
        return sb.toString();
    }
}
