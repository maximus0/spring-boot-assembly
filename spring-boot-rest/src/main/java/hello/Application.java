package hello;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@ImportResource({"demo.xml"})
public class Application {


    public static void main(String[] args) throws IOException {
        //默认运行方式
        SpringApplication.run(Application.class, args);

//       // 设置运行参数
//        SpringApplication app = new SpringApplication(Application.class);
//        app.setShowBanner(true);
//        app.run(args);


//       List list =  POIUtil.readExcel( new File("18.7分.xlsx"));
//        System.out.println(JSON.toJSONString(list));
//        FileWriter fw = null;
//        try {
//            fw = new FileWriter(System.currentTimeMillis()+".vcf");
//            if (null != list && list.size()>0){
//                for (int i = 0; i < list.size(); i++) {
//                    String[] str = (String[])list.get(i);
//                    fw.write("BEGIN:VCARD\n" +
//                            "VERSION:3.0\n" +
//                            "N:"+str[0]+";;;;\n" +
//                            "FN:"+str[0]+"\n" +
//                            "TEL;type=CELL;type=VOICE;type=pref:"+str[1]+"\n" +
//                            "REV:2015-10-08T13:53:28Z\n" +
//                            "END:VCARD\n" );
//                }
//            }
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }





    }
}
