package hello;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * GreetingServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>七月 23, 2015</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GreetingServiceImplTest {
    @Autowired
    private GreetingService greetingService;
    @Autowired
    private Config config;

    @Before
    public void before() throws Exception {
        System.out.println("**********************************************");
    }

    @After
    public void after() throws Exception {
        System.out.println("**********************************************");
    }

    /**
     * Method: sayHello()
     */
    @Test
    public void testSayHello() throws Exception {
        greetingService.sayHello();
    }

    /**
     * Method: sayHello()
     */
    @Test
    public void testConfig() throws Exception {
        System.out.println(JSON.toJSONString(config));

    }


}
