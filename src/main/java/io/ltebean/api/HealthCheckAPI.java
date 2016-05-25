package io.ltebean.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leo on 16/5/25.
 */

@RestController
public class HealthCheckAPI {

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    public String ping() {
        return "I am good";
    }

}
