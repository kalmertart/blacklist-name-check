package ee.kalmertart.blacklist.response;

public class BlacklistCheckResponse {

    private final boolean blacklisted;
    private final String comment;

    public BlacklistCheckResponse(boolean blacklisted, String comment) {
        this.blacklisted = blacklisted;
        this.comment = comment;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public String getComment() {
        return comment;
    }
}
