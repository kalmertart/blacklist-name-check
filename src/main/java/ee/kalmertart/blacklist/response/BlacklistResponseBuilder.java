package ee.kalmertart.blacklist.response;

public class BlacklistResponseBuilder {

    private BlacklistResponseBuilder() {}

    public static BlacklistCheckResponse blacklisted(String comment) {
        return new BlacklistCheckResponse(true, comment);
    }

    public static BlacklistCheckResponse notBlacklisted(String comment) {
        return new BlacklistCheckResponse(false, comment);
    }

    public static BlacklistCheckResponse notBlacklisted() {
        return notBlacklisted("Not found from blacklist");
    }
}
