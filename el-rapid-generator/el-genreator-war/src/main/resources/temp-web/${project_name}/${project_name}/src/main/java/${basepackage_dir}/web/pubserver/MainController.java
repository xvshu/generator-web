<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web.pubserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
<#include "/java_imports.include">

/**
 * 首页控制器
 *
 * @author xvshu
 */
@Controller("pubMainController")
@RequestMapping("/pubserver/main")
public class MainController {

    @RequestMapping(value = "/index")
    public String index() {
        return "/main/index";
    }

    @RequestMapping(value = "/hello")
    public String welcome() {
        return "/main/hello";
    }

}
