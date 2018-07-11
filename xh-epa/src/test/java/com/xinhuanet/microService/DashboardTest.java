package com.xinhuanet.microService;

import com.xinhuanet.microService.service.DashboardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by conanca on 15-6-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class DashboardTest {

    @Autowired
    private DashboardService helloService;
    @Test
    public void test() throws Exception{

        helloService.say();

    }

}
