package com.xinhuanet.censor.service.impl;

import com.xinhuanet.censor.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by conanca on 15-6-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class XuanCmsTest {

    @Autowired
    private ArticleServiceImpl articleServiceImpl;
    @Autowired
    private PushServiceImpl pushServiceImpl;

    @Test
    public void testPush2XuanCms() throws Exception{

        Map m = new HashMap();
        m.put("appId",20);
        m.put("column",1);
        m.put("contentId","219324613");
        m.put("editorName","jnduan");

        Map xuan = articleServiceImpl.genXuanCmsMessage(m);
        pushServiceImpl.push2XuanCms(xuan);

        m.put("contentId","219316669");
        xuan = articleServiceImpl.genXuanCmsMessage(m);
        pushServiceImpl.push2XuanCms(xuan);

        m.put("contentId","219316876");
        xuan = articleServiceImpl.genXuanCmsMessage(m);
        pushServiceImpl.push2XuanCms(xuan);

        m.put("contentId","219309009");
        xuan = articleServiceImpl.genXuanCmsMessage(m);
        pushServiceImpl.push2XuanCms(xuan);

        m.put("contentId","219309013");
        xuan = articleServiceImpl.genXuanCmsMessage(m);
        pushServiceImpl.push2XuanCms(xuan);
    }

}
