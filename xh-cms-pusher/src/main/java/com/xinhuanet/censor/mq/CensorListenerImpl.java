package com.xinhuanet.censor.mq;

import com.alibaba.fastjson.JSON;
import com.xinhuanet.censor.service.impl.ArticleServiceImpl;
import com.xinhuanet.censor.service.impl.PushServiceImpl;
import com.xinhuanet.censor.util.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
public class CensorListenerImpl implements CensorListener {
    private static Logger logger = LoggerFactory.getLogger(CensorListenerImpl.class);

    @Autowired
    ArticleServiceImpl articleServiceImpl;
    @Autowired
    PushServiceImpl pushServiceImpl;

    @Override
    public void receive(@SuppressWarnings("rawtypes") Map message) throws Exception {
        logger.info("===从队列中获取到消息：" + JSON.toJSONString(message));

        if (Lang.isEmpty(message)) {
            logger.error("message 为空");
            return;
        }

        Integer sendFlag = (Integer) message.get("sendFlag");
        if (sendFlag.equals(1)) {
            //推送炫知
            pushXuanCms(message);
            //推送CMS
            pushCmsXML(message);
            pushCmsCNML(message);
        } else if (sendFlag.equals(2)) {
            //推送炫知
            pushXuanCms(message);
        } else if (sendFlag.equals(3)) {
            //推送CMS
            pushCmsXML(message);
            pushCmsCNML(message);
        }


        //回调原送审系统，通知数据已推送
        articleServiceImpl.callbackUGC(message);
        logger.info("===消息处理完成");
    }

    private void pushXuanCms(@SuppressWarnings("rawtypes") Map message) {
        logger.info("生成推送至炫知所需的队列消息...");
        Map xuanCmsMessage = null;
        try {
            xuanCmsMessage = articleServiceImpl.genXuanCmsMessage(message);
            if(Lang.isEmpty(xuanCmsMessage)){
                logger.error("生成推送炫知的message数据异常 , 由contentId查询不到原审核对象 ");
                return;
            }
            logger.info(" xuanCmsMessage:" + JSON.toJSONString(xuanCmsMessage));
        } catch (Exception e) {
            logger.error("生成推送至炫知所需的队列消息失败", e);
        }
        logger.info("推送至炫知...");
        try {
            pushServiceImpl.push2XuanCms(xuanCmsMessage);
        } catch (Exception e) {
            logger.error("推送至炫知失败", e);
        }
    }

    private void pushCmsXML(@SuppressWarnings("rawtypes") Map message) {
        logger.info("正在生成XML文件及相关文件...");
        try {
            String[] filePathArr1 = articleServiceImpl.genXML(message);
            logger.info("已生成：" + JSON.toJSONString(filePathArr1));
            logger.info("正在上传XML文件及相关文件...");
            if (filePathArr1 != null) {
                for (String filePath : filePathArr1) {
                    if (!StringUtils.isEmpty(filePath)) {
                        pushServiceImpl.push2CMS(filePath);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("XML文件及相关文件上传或生成失败", e);
        }
        logger.info("上传完成!");
    }

    private void pushCmsCNML(@SuppressWarnings("rawtypes") Map message) {
        logger.info("正在生成CNML文件及相关文件...");
        try {
            String[] filePathArr2 = articleServiceImpl.genCNML(message);
            logger.info("已生成：" + JSON.toJSONString(filePathArr2));
            if (filePathArr2 != null) {
                for (String filePath : filePathArr2) {
                    if (!StringUtils.isEmpty(filePath)) {
                        pushServiceImpl.push2CNML(filePath);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("CNML文件及相关文件上传或生成失败", e);
        }
        logger.info("上传完成!");
    }
}
