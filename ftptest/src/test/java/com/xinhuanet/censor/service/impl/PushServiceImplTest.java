package com.xinhuanet.censor.service.impl;

import com.xinhuanet.censor.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * PushServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>六月 8, 2015</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class PushServiceImplTest {

    @Autowired
    private PushServiceImpl pushServiceImpl;


    @Test
    public void testPush() throws Exception {
        pushServiceImpl.push2ftp("C:\\Users\\maximus\\Desktop\\include\\20150909\\2538183_c.html");

    }




    @Test
    public void testPush2XuanCms() throws Exception{


    }



}
