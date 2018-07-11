package com.xinhuanet.censor.mq;

import com.alibaba.fastjson.JSON;
import com.xinhuanet.censor.App;
import com.xinhuanet.censor.service.impl.ArticleServiceImpl;
import com.xinhuanet.censor.service.impl.PushServiceImpl;
import com.xinhuanet.censor.util.Lang;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * CensorListenerImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>六月 29, 2015</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class CensorListenerImplTest {
    @Autowired
    private ArticleServiceImpl articleServiceImpl;
    @Autowired
    private PushServiceImpl pushServiceImpl;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: receive(@SuppressWarnings("rawtypes") Map message)
     */
    @Test
    public void testReceive() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: pushXuanCms(@SuppressWarnings("rawtypes") Map message)
     */
    @Test
    public void testPushXuanCms() throws Exception {
        Map message = new HashMap();
        message.put("appId", 30);
        message.put("appId", 2);
        message.put("contentId", "2648168");
        message.put("editorName", "jnduan");


        Map xuanCmsMessage = null;
        xuanCmsMessage = articleServiceImpl.genXuanCmsMessage(message);
        if (Lang.isEmpty(xuanCmsMessage)) {
            System.out.println("生成推送炫知的message数据异常 , 由contentId查询不到原审核对象 ");
        }
        System.out.println(" xuanCmsMessage:" + JSON.toJSONString(xuanCmsMessage));
        System.out.println("推送至炫知...");

        pushServiceImpl.push2XuanCms(xuanCmsMessage);

    }

    /**
     * Method: pushCmsXML(@SuppressWarnings("rawtypes") Map message)
     */
    @Test
    public void testPushCmsXML() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = CensorListenerImpl.getClass().getMethod("pushCmsXML", @SuppressWarnings("rawtypes").class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: pushCmsCNML(@SuppressWarnings("rawtypes") Map message)
     */
    @Test
    public void testPushCmsCNML() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = CensorListenerImpl.getClass().getMethod("pushCmsCNML", @SuppressWarnings("rawtypes").class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
