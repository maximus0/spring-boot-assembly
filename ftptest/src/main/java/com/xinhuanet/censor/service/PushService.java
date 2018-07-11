package com.xinhuanet.censor.service;

import java.io.IOException;

/**
 * Created by conanca on 15-6-3.
 */
public interface PushService {

    /**
     * 上传XML文件至FTP
     * @param file
     */
    void push2ftp(String file) throws IOException;

}
