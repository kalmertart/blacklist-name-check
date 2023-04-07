package ee.kalmertart.blacklist.request;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Objects;

@Component
public class BlacklistCheckRequestBuilder {

    private static final String BLACKLIST_FILE = "names.txt";
    private static final String NOISE_WORDS_FILE = "noise_words.txt";

    public BlacklistCheckRequest build(String name) {
        return new BlacklistCheckRequest(
                name,
                fileFromResources(BLACKLIST_FILE),
                fileFromResources(NOISE_WORDS_FILE)
        );
    }

    private File fileFromResources(@NonNull String filename) {
        ClassLoader loader = this.getClass().getClassLoader();
        URL resource = loader.getResource(filename);
        return new File(Objects.requireNonNull(resource).getFile());
    }
}
