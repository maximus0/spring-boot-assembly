package com.xinhuanet.censor.mq;

import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
public interface CensorListener {
    void receive(@SuppressWarnings("rawtypes") Map message) throws Exception;
}
