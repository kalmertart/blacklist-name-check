package ee.kalmertart.blacklist.controller;

import ee.kalmertart.blacklist.request.BlacklistCheckRequest;
import ee.kalmertart.blacklist.request.BlacklistCheckRequestBuilder;
import ee.kalmertart.blacklist.response.BlacklistCheckResponse;
import ee.kalmertart.blacklist.service.BlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    private static final Logger log = LoggerFactory.getLogger(BlacklistController.class);

    private final BlacklistService blacklistService;
    private final BlacklistCheckRequestBuilder requestBuilder;

    public BlacklistController(BlacklistService blacklistService,
                               BlacklistCheckRequestBuilder requestBuilder) {
        this.blacklistService = blacklistService;
        this.requestBuilder = requestBuilder;
    }

    @PostMapping
    public BlacklistCheckResponse isBlackListed(@RequestBody String name) {
        log.info("Check if '{}' is blacklisted", name);
        BlacklistCheckRequest blacklistCheckRequest = requestBuilder.build(name);
        return blacklistService.check(blacklistCheckRequest);
    }
}
