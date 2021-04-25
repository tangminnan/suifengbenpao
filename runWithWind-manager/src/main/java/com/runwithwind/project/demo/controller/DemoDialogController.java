package com.runwithwind.project.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模态窗口
 * 
 * @author runwithwind
 */
@Controller
@RequestMapping("/demo/modal")
public class DemoDialogController
{
    private String prefix = "demo/modal";

    /**
     * 模态窗口
     */
    @RequiresPermissions("demo:modal:dialog")
    @GetMapping("/dialog")
    public String dialog()
    {
        return prefix + "/dialog";
    }

    /**
     * 弹层组件
     */
    @RequiresPermissions("demo:modal:layer")
    @GetMapping("/layer")
    public String layer()
    {
        return prefix + "/layer";
    }

    /**
     * 表单
     */
    @RequiresPermissions("demo:modal:form")
    @GetMapping("/form")
    public String form()
    {
        return prefix + "/form";
    }

    /**
     * 表格
     */
    @RequiresPermissions("demo:modal:table")
    @GetMapping("/table")
    public String table()
    {
        return prefix + "/table";
    }

    /**
     * 表格check
     */
    @RequiresPermissions("demo:modal:check")
    @GetMapping("/check")
    public String check()
    {
        return prefix + "/table/check";
    }

    /**
     * 表格radio
     */
    @RequiresPermissions("demo:modal:radio")
    @GetMapping("/radio")
    public String radio()
    {
        return prefix + "/table/radio";
    }

    /**
     * 表格回传父窗体
     */
    @RequiresPermissions("demo:modal:parent")
    @GetMapping("/parent")
    public String parent()
    {
        return prefix + "/table/parent";
    }
}
