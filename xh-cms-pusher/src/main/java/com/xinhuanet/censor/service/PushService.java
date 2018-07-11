package com.xinhuanet.censor.service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
public interface PushService {

    /**
     * 上传XML文件至FTP
     * @param file
     */
    void push2CMS(String file) throws IOException;

    /**
     * 上传CNML文件至FTP
     * @param file
     */
    void push2CNML(String file);

    void push2XuanCms(Map message);
}
