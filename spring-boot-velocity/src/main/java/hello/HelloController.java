package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maximus on 16-3-25.
 */
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public String xhDataList( Model model) {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Map map = new HashMap();
            map.put("a", i);
            map.put("b", i);
            map.put("c", i);
            map.put("d", i);
            map.put("e", i);
            list.add(map);
        }

        model.addAttribute("hello", "world");
        model.addAttribute("list", list);
        return "hello";
    }
}
