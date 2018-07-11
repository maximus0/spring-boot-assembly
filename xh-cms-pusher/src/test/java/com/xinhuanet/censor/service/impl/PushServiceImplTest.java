package com.xinhuanet.censor.service.impl;

import com.alibaba.fastjson.JSON;
import com.xinhuanet.censor.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private ArticleServiceImpl articleServiceImpl;
    @Test
    public void testPush2CMS() throws Exception {
        pushServiceImpl.push2CMS("/tmp/xh-cms-pusher/article/0110000000201506091218467738.xml");
        pushServiceImpl.push2CMS("/tmp/xh-cms-pusher/article/0110000000201506091218467738.xml.ok");

    }


    @Test
    public void testPush2CNML() throws Exception {
        pushServiceImpl.push2CNML("/tmp/xh-cms-pusher/article/0110000000201506091218467738_new.xml");
    }

    @Test
    public void testPush2XuanCms() throws Exception{

        String origin = "新华博客";

        Map xuan = new HashMap();
        xuan.put("appId",20);
        xuan.put("column",1);
        xuan.put("contentId","219324613");

        xuan.put("title","【金筱阮】:黄金继续震荡，空头独占鳌头");
        xuan.put("contentTxt","<p>　　【金筱阮-消息面】：</p><p>　　</p><p>　　黄金上周五的行情波动主要受到几个方面因素的影响，首先是市场预期美联储不加息，动能先行。其次是美国的密歇根消费者信心指数远高于前值和预期值。两者相结合，组成了短期震荡的行情波动。</p><p>　　</p><p>　　【金筱阮-行情回顾】：</p><p>　　</p><p>　　黄金上周开盘1171.20，最高运行至1192.20，最低运行至1169.30，收盘为1183.70，周线收盘成一根小阳线，从周线图上看黄金还处于震荡趋势的空头区域，所以我们采用逢高做空的思路，行去本周预计还会下行，但是震荡模式下的下行会加剧，本周预计可以见到1161附近的价格，这也是我们近期一直在期待的价格，而操作方面上周的空单持有，今日1183以上进场空单即可。消息面上注意希腊问题和评级事件，一定要注意市场的避险情节，一旦引发避险情节就要随时关注美元的走势，以防黄金避险出现暴涨。</p><p>　　</p><p>　　上周五伦敦股市富时100指数报收于6784.92点,比前一交易日下跌61.82点,跌幅为0.90%。法国巴黎股市CAC40指数以4901.19点报收,比前一交易日下跌70.18点,跌幅为1.41%。德国法兰克福股市DAX指数收于11196.49点,比前一交易日下跌136.29点,跌幅为1.20%。上周五美国股市收跌，全周道指上涨0.3%、标普上涨0.1%、纳指下跌0.3%。希腊债务违约的可能性令欧美股市承压。道琼斯工业平均指数下跌140.53点，报17,898.84点，跌幅为0.78%;标准普尔500指数下跌14.75点，报2,094.11点，跌幅为0.70%;纳斯达克综合指数下跌31.41点，报5,051.10点，跌幅为0.62%。</p><p>　　</p><p>　　【金筱阮-技术面分析】：</p><p>　　</p><p>　　黄金上周开盘1171.20，最高运行至1192.20，最低运行至1169.30，收盘为1183.70，周线收盘成一根小阳线，从周线图上看黄金还处于震荡趋势的空头区域，所以我们采用逢高做空的思路，行去本周预计还会下行，但是震荡模式下的下行会加剧，本周预计可以见到1161附近的价格，这也是我们近期一直在期待的价格，而操作方面上周的空单持有，今日1183以上进场空单即可。消息面上注意希腊问题和评级事件，一定要注意市场的避险情节，一旦引发避险情节就要随时关注美元的走势，以防黄金避险出现暴涨。</p><p>　　</p><p>　　在技术面上，黄金在筑底之后，就呈现出小幅的反弹，其主要原因是价格下跌过快，之前非常接近之前价格的前低，使得反弹调整有利于趋势的重塑。而之后价格就形成了矩形整理形态，并逐步往三角形整理形态进行调整。在技术指标方面，MACD和RSI都出现了动能适度放缓的迹象，线段皆出现走平，动能消退明显。</p><p>　　</p><p>　　【金筱阮-操作建议】：</p><p>　　</p><p>　　黄金，月中主力调仓，通常有较大行情，早间金价冲高回落，暴漏了多头的实力不佳，重要时间关口，注重后发制人，多头早间太急，现在已经变的被动。周末我们认为黄金周一白盘横盘整理或主动向下找支撑，不破1175可以继续看反弹，美盘突破1185则反弹有望延续。而早间的多头提前侧死1185阻力无果，预计白盘已经不再有破新高的能力，1184-1178运动，有跌破1175的可能，建议短线以1187为损短线高空。</p><p>　　</p><p>　　白银，早盘16.10迅速打压银价反弹，白银日线上恰好测试ma10阻力下跌，调仓日多空对决，后出手者胜，早盘多头匆匆出手没有跟风盘，不破16.15空头继续掌控全局。日线上，白银似乎在底部，而却没有企稳信号，底部不是画根趋势线就能支撑起来的。早盘的高点可能成为日内的高点。</p><p><br /></p>");
        xuan.put("author","金筱阮");
        xuan.put("origin",origin);
        xuan.put("originUrl","http://103570110.home.news.cn/blog/a/0101000000000D12A0C50265.html");
        xuan.put("tag","操作建议,现货黄金白银,金筱阮");

       pushServiceImpl.push2XuanCms(xuan);
    }
    @Test
    public void testPush301XuanCms() throws Exception{


        // {"sendFlag":2,"result":115,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"1477016","sender":2852954,"editorName":"wb001","time":1435650213127,"editorId":2852954,"column":"1","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}

        Map m = new HashMap();
        m.put("appId",30);
        m.put("column","1");
        m.put("contentId","1477016");
        m.put("editorName", "wb001");
        Map xuan = articleServiceImpl.genXuanCmsMessage(m);
        System.out.println("--------"+JSON.toJSON(xuan));

        pushServiceImpl.push2XuanCms(xuan);
    }
    @Test
    public void testPush302XuanCms() throws Exception{
        // {"sendFlag":1,"result":110,"appId":30,"receiver":[1369],"edi     torIp":"127.0.0.1","msgTypeId":102,"contentId":"2648217","sender":2852954,"editorName":"wb001","time":1435651508870,"editorId":2852954,"column":"2","queueName":"xhGeek.push-CMS","editor     NickName":"大死胖子","companyId":100}


        Map m = new HashMap();
        m.put("appId",30);
        m.put("column","2");
        m.put("contentId","2648217");
        m.put("editorName", "wb001");
        Map xuan = articleServiceImpl.genXuanCmsMessage(m);
        System.out.println("-----"+JSON.toJSON(xuan));

        pushServiceImpl.push2XuanCms(xuan);
    }

}
