package lijie.test.myTomcat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjie
 * @version 2018/11/9 
 */
public class ServletMappingConfig {
    public static List<ServletMapping> servletMappingList =new ArrayList<>();
    //制定哪个URL交给哪个servlet来处理
    static{
        servletMappingList.add(new ServletMapping("findGirl","/girl","lijie.test.myTomcat.FindGirlServlet"));
        servletMappingList.add(new ServletMapping("helloWorld","/world","lijie.test.myTomcat.HelloWorldServlet"));
        servletMappingList.add(new ServletMapping("helloWorld","/favicon.ico","lijie.test.myTomcat.HelloWorldServlet"));
    }
}
