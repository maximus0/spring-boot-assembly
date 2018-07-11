package hello;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by chenweichao on 15-7-23.
 */
@Component
public class ShutdownaBean implements ApplicationListener {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ShutdownaBean.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LOGGER.info("do something ******************");
    }

}
