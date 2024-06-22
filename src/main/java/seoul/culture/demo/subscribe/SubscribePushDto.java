package seoul.culture.demo.subscribe;

public record SubscribePushDto(String phoneNumber, String eventTitle, String originMessage) {
    @Override
    public String toString() {
        return "[서울지금여기]\n" +
                "와! 행사가 오픈되었어요!\n" +
                eventTitle + "\n" +
                "바로가기: 미구현\n" +
                "(신청 메세지: " + originMessage + ")";
    }
}