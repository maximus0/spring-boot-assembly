package com.xinhuanet.microService.service.impl;

import com.xinhuanet.microService.service.DashboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by conanca on 15-5-28.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    /**
     * 生成xml后存放到的本地目录
     */
    @Value("${config.hello}")
    private String testParam;
    public void say() {
        System.out.println("Hello world!");
        System.out.println(testParam);
    }
}
