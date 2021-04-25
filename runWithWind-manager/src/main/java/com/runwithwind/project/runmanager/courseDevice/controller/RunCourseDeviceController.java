package com.runwithwind.project.runmanager.courseDevice.controller;

import java.util.List;

import com.runwithwind.project.runmanager.courseDevice.domain.RunCourseDevice;
import com.runwithwind.project.runmanager.courseDevice.service.IRunCourseDeviceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.framework.web.page.TableDataInfo;

/**
 * 课程中设备记录Controller
 * 
 * @author jzf
 * @date 2021-01-19
 */
@Controller
@RequestMapping("/runmanager/courseDevice")
public class RunCourseDeviceController extends BaseController
{
    private String prefix = "runmanager/courseDevice";

    @Autowired
    private IRunCourseDeviceService runCourseDeviceService;

    @RequiresPermissions("runmanager:courseDevice:view")
    @GetMapping("/{courseId}")
    public String courseDevice(@PathVariable("courseId")Long courseId, Model model)
    {
        model.addAttribute("courseId",courseId);
        return prefix + "/device";
    }

    /**
     * 查询课程中设备记录列表
     */
    @RequiresPermissions("runmanager:courseDevice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RunCourseDevice runCourseDevice)
    {
        startPage();
        List<RunCourseDevice> list = runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
        return getDataTable(list);
    }

    /**
     * 导出课程中设备记录列表
     */
    @RequiresPermissions("runmanager:courseDevice:export")
    @Log(title = "课程中设备记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RunCourseDevice runCourseDevice)
    {
        List<RunCourseDevice> list = runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
        ExcelUtil<RunCourseDevice> util = new ExcelUtil<RunCourseDevice>(RunCourseDevice.class);
        return util.exportExcel(list, "courseDevice");
    }

    /**
     * 新增课程中设备记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存课程中设备记录
     */
    @RequiresPermissions("runmanager:courseDevice:add")
    @Log(title = "课程中设备记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RunCourseDevice runCourseDevice)
    {
        return toAjax(runCourseDeviceService.insertRunCourseDevice(runCourseDevice));
    }

    /**
     * 修改课程中设备记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        RunCourseDevice runCourseDevice = runCourseDeviceService.selectRunCourseDeviceById(id);
        mmap.put("runCourseDevice", runCourseDevice);
        return prefix + "/edit";
    }

    /**
     * 修改保存课程中设备记录
     */
    @RequiresPermissions("runmanager:courseDevice:edit")
    @Log(title = "课程中设备记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RunCourseDevice runCourseDevice)
    {
        return toAjax(runCourseDeviceService.updateRunCourseDevice(runCourseDevice));
    }

    /**
     * 删除课程中设备记录
     */
    @RequiresPermissions("runmanager:courseDevice:remove")
    @Log(title = "课程中设备记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(runCourseDeviceService.deleteRunCourseDeviceByIds(ids));
    }
}
