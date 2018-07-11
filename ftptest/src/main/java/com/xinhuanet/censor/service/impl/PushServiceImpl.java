package com.xinhuanet.censor.service.impl;

import com.xinhuanet.censor.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by conanca on 15-6-3.
 */
@Service
public class PushServiceImpl implements PushService {
    private static Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

    @Autowired
    private MessageChannel ftpChannelXmlContent;
    @Autowired
    private MessageChannel ftpChannelXmlInfo;
    @Autowired
    private MessageChannel ftpChannelCnmlContent;
    @Autowired
    private MessageChannel ftpChannelCnmlInfo;



    @Override
    public void push2ftp(String filePath) throws IOException {
        File file = new File(filePath);
        final Message<File> messageFile = MessageBuilder.withPayload(file).build();
        ftpChannelXmlContent.send(messageFile);

//        // 上传 ok 文件
//        String fileOKPath = filePath + ".ok";
//        File fileOK = new File(fileOKPath);
//        try {
//            fileOK.createNewFile();
//        } catch (IOException e) {
//            logger.error("ok文件生成失败！", e);
//        }
//        final Message<File> messageFileOk = MessageBuilder.withPayload(fileOK).build();
//        ftpChannelXmlInfo.send(messageFileOk);

    }


}
