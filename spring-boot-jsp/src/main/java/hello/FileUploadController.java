package hello;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController {

    /*
     * 获取file.html页面
     */
    @RequestMapping("file")
    public String file() {
        return "/file";
    }

    /**
     * 实现文件上传
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "false";
        }
        List list = POIUtilCopy.readExcel(file);
        System.out.println(JSON.toJSONString(list));
        FileWriter fw = null;
        try {
            fw = new FileWriter(System.currentTimeMillis() + ".vcf");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String[] str = (String[]) list.get(i);
                    fw.write("BEGIN:VCARD\n" +
                            "VERSION:3.0\n" +
                            "N:" + str[0] + ";;;;\n" +
                            "FN:" + str[0] + "\n" +
                            "TEL;type=CELL;type=VOICE;type=pref:" + str[1] + "\n" +
                            "REV:2015-10-08T13:53:28Z\n" +
                            "END:VCARD\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    /*
     * 获取multifile.html页面
     */
    @RequestMapping("multifile")
    public String multifile() {
        return "/multifile";
    }

    /**
     * 实现多文件上传
     */
    @RequestMapping(value = "multifileUpload", method = RequestMethod.POST)

/**public @ResponseBody String multifileUpload(@RequestParam("fileName")List<MultipartFile> files) */
    public @ResponseBody
    String multifileUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");

        if (files.isEmpty()) {
            return "false";
        }

        String path = "F:/test";

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

            if (file.isEmpty()) {
                return "false";
            } else {
                File dest = new File(path + "/" + fileName);
                if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "true";
    }
}