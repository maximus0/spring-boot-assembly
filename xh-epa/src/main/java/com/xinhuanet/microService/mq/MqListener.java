package com.xinhuanet.microService.mq;

import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
public interface MqListener {
    void receive(@SuppressWarnings("rawtypes") Map message) throws Exception;
}
