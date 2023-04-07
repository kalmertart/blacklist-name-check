package ee.kalmertart.blacklist.request;

import java.io.File;

public class BlacklistCheckRequest {

    private final String name;
    private final File blacklistFile;
    private final File noiseWordsFile;


    public BlacklistCheckRequest(String name, File blacklistFile, File noiseWordsFile) {
        this.name = name;
        this.blacklistFile = blacklistFile;
        this.noiseWordsFile = noiseWordsFile;
    }

    public String getName() {
        return name;
    }

    public File getBlacklistFile() {
        return blacklistFile;
    }

    public File getNoiseWordsFile() {
        return noiseWordsFile;
    }
}
