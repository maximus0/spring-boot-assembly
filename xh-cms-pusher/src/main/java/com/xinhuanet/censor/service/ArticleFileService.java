package com.xinhuanet.censor.service;


import java.io.IOException;
import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
public interface ArticleFileService {

    /**
     * 生成推炫知所需要的Map
     * @param message
     * @return
     */
    Map genXuanCmsMessage(Map message);

    /**
     * 生成CMS所需的XML文件
     * @param message
     * @return 文件路径
     */
    String[] genXML(Map message) ;

    /**
     * 生成CNML文件
     * @param message
     * @return 文件路径
     */
    String[] genCNML( Map message);

    /**
     * 推CMS后回调原系统
     * @param message
     * @return
     */
    void callbackUGC(Map message);
}
