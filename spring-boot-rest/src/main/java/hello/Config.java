package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweichao on 15-7-23.
 */
@Component
@ConfigurationProperties(prefix = "testdata")
public class Config {

    private List<String> servers = new ArrayList<String>();
    private Map urls = new HashMap();
    @Value("${testdata.hello}")
    private String hello;

    public List<String> getServers() {
        return this.servers;
    }

    public Map getUrls() {
        return this.urls;
    }

    public String getHello(){
        return hello;
    }
}

