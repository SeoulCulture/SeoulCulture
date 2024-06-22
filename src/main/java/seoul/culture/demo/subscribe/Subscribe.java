package seoul.culture.demo.subscribe;

class Subscribe {
    private String phoneNumber;
    private String message;

    public Subscribe(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }
}