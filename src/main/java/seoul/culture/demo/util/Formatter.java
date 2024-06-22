package seoul.culture.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Formatter {

    public static double toDouble(String str) {
        return Double.parseDouble(stringWithNoQuotesForJson(str));
    }

    public static String stringWithNoQuotes(String str) {
        return str.trim().replaceAll("^\"|\"$", "").replace("'", "＇").replace("\"", "\\\"");
    }
    public static String stringWithNoQuotesForJson(String str) {
        return str.trim().replaceAll("^\"|\"$", "").replace("^'|'$","");
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
            sb.append(info).append("<br>");
        }
        return sb.toString();
    }

    public static String toAddress(Map<String, String> mapAddress){
        return  mapAddress.get("sido") + " " +
                mapAddress.get("sigungu") + " " +
                mapAddress.get("doro");
    }
}