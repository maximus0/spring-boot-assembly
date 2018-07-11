package com.xinhuanet.microService.web;

import com.xinhuanet.microService.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @Autowired
    private DashboardService helloService;

    @RequestMapping("/")
    public String home() {
        helloService.say();
        return "Hello World!";
    }
}
