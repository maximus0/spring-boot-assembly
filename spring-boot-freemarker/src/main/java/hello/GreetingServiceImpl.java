package hello;

/**
 * Created by chenweichao on 15-7-23.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService{

    @Value("${testdata.hello}")
    private  String hello;


    @Value("${testdata.servers[0]}")
    private   String serverName0;

    @Value("${testdata.servers[1]}")
    private   String serverName1;




    @Override
    public void sayHello() {
        System.out.println("service say : "+ hello);
        System.out.println("my.servers[0] : "+ serverName0);
        System.out.println("my.servers[1] : "+ serverName1);

    }
}
