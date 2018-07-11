package com.xinhuanet.censor.service.impl;

import com.alibaba.fastjson.JSON;
import com.trs.blog.domain.Article;
import com.trs.blog.logic.ArticleManager;
import com.xinhuanet.censor.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * ArticleServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>六月 4, 2015</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class ArticleServiceImplTest {



    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @Autowired
    private ArticleManager articleManager;

    /**
     * Method: genXML(CensorNeed censorNeed)
     */
    @Test
    public void testGenXML() throws Exception {
        Map m = new HashMap();
        m.put("appId",20);
        m.put("contentId","219127778");
//        m.put("contentId","219154676");
        m.put("editorName","jnduan");
        String[] arr = articleServiceImpl.genXML(m);
        System.out.println(JSON.toJSON(arr));
    }

    /**
     * Method: genCNML(CensorNeed censorNeed)
     */
    @Test
    public void testGenCNML() throws Exception {
        Map m = new HashMap();
        m.put("appId",20);
        m.put("contentId","219154676");
        m.put("editorName","jnduan");
       String[] arr =  articleServiceImpl.genCNML(m);
        System.out.println(JSON.toJSON(arr));
    }


    @Test
    public void testArticle() throws Exception{
        Article a = articleManager.getArticle(219324613);
        System.out.println(a.getTagWords());
    }

    @Test
    public void testGenXuanCmsMessage() throws Exception{
        Map m = new HashMap();
        m.put("appId",20);
        m.put("column","1");
        m.put("contentId","217175274");
        m.put("editorName","jnduan");

        Map xuan = articleServiceImpl.genXuanCmsMessage(m);

System.out.println("~~~~~~~~~~~~~~~~~~"+JSON.toJSON(xuan));
    }

    @Test
    public void testGen30Message() throws Exception {
        Map m = new HashMap();
        m.put("appId",30);
        m.put("column","2");
        m.put("contentId","2648168");
        m.put("editorName","jnduan");
        Map xuan = articleServiceImpl.genXuanCmsMessage(m);
        System.out.println(JSON.toJSON(xuan));
    }
    @Test
    public void testGenPhotoXML() throws Exception {
        Map m = new HashMap();
        m.put("appId",30);
        m.put("column","2");
        m.put("contentId","2648168");
        m.put("editorName","jnduan");
        String[] arr = articleServiceImpl.genXML(m);
        System.out.println(JSON.toJSON(arr));
    }
    @Test
    public void testGenPhotoCNML() throws Exception {
        // {"sendFlag":2,"result":115,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"1477016","sender":2852954,"editorName":"wb001","time":1435650213127,"editorId":2852954,"column":"1","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}
        // {"sendFlag":1,"result":110,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"2648217","sender":2852954,"editorName":"wb001","time":1435651508870,"editorId":2852954,"column":"2","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}

        Map m = new HashMap();
        m.put("appId",30);
        m.put("column","2");
        m.put("contentId","2648168");
        m.put("editorName","jnduan");
        String[] arr =  articleServiceImpl.genCNML(m);
        System.out.println(JSON.toJSON(arr));
    }

    @Test
    public void testGen40Message() throws Exception{
        Map m = new HashMap();
        m.put("appId",40);
        m.put("column","1");
        m.put("contentId","153182");
        m.put("editorName","jnduan");

        Map xuan = articleServiceImpl.genXuanCmsMessage(m);

        System.out.println("~~~~~~~~~~~~~~~~~~"+JSON.toJSON(xuan));
    }

    @Test
    public void testGenVideoXml() throws Exception {
        // {"sendFlag":2,"result":115,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"1477016","sender":2852954,"editorName":"wb001","time":1435650213127,"editorId":2852954,"column":"1","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}
        // {"sendFlag":1,"result":110,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"2648217","sender":2852954,"editorName":"wb001","time":1435651508870,"editorId":2852954,"column":"2","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}

        Map m = new HashMap();
        m.put("appId",40);
        m.put("column","1");
        m.put("contentId","153182");
        m.put("editorName","jnduan");
        String[] arr =  articleServiceImpl.genXML(m);
        System.out.println(JSON.toJSON(arr));
    }
    @Test
    public void testGenVideoCNML() throws Exception {
        // {"sendFlag":2,"result":115,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"1477016","sender":2852954,"editorName":"wb001","time":1435650213127,"editorId":2852954,"column":"1","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}
        // {"sendFlag":1,"result":110,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"2648217","sender":2852954,"editorName":"wb001","time":1435651508870,"editorId":2852954,"column":"2","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}

        Map m = new HashMap();
        m.put("appId",40);
        m.put("column","1");
        m.put("contentId","153182");
        m.put("editorName","jnduan");
        String[] arr =  articleServiceImpl.genCNML(m);
        System.out.println(JSON.toJSON(arr));
    }

} 
