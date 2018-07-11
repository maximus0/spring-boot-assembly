package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ImportResource({"demo.xml"})
public class Application {


    public static void main(String[] args) {
        //默认运行方式
//        SpringApplication.run(Application.class, args);

        //设置运行参数
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(true);
        app.run(args);
    }
}
