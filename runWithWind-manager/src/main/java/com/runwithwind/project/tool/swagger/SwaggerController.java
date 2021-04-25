package com.runwithwind.project.tool.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.runwithwind.framework.web.controller.BaseController;

/**
 * swagger 接口
 * 
 * @author runwithwind
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController
{
    //@RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect("/swagger-ui.html");
    }
}
