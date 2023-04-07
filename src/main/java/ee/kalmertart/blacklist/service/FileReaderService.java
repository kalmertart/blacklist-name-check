package ee.kalmertart.blacklist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class FileReaderService {

    private static final Logger log = LoggerFactory.getLogger(FileReaderService.class);

    public List<String> readWords(File file) {
        try (Scanner scanner = new Scanner(file)) {
            List<String> words = new ArrayList<>();
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
            return words;
        } catch (Exception e) {
            log.error("Failed to read file {}", file);
            return Collections.emptyList();
        }
    }
}
