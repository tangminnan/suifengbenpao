package com.runwithwind.project.runmanager.detail.controller;

import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.courseDevice.domain.RunCourseDevice;
import com.runwithwind.project.runmanager.courseDevice.service.IRunCourseDeviceService;
import com.runwithwind.project.runmanager.detail.domain.RunCourseDetail;
import com.runwithwind.project.runmanager.detail.service.IRunCourseDetailService;
import com.runwithwind.project.runmanager.user.domain.RunUser;
import com.runwithwind.project.runmanager.user.service.IRunUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程详情Controller
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/detail")
public class RunCourseDetailController extends BaseController {
  private String prefix = "runmanager/detail";

  @Autowired private IRunCourseDetailService runCourseDetailService;
  @Autowired private IRunUserService userService;
  @Autowired private IRunCourseDeviceService deviceService;

  @RequiresPermissions("runmanager:detail:view")
  @GetMapping("/{courseId}")
  public String detail(@PathVariable("courseId") String courseId, Model model) {
    model.addAttribute("courseId", courseId);
    model.addAttribute("userList", userService.selectRunUserList(new RunUser()));
    return prefix + "/detail";
  }

  /** 查询课程详情列表 */
  @RequiresPermissions("runmanager:detail:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunCourseDetail runCourseDetail) {
    startPage();
    List<RunCourseDetail> list = runCourseDetailService.selectRunCourseDetailList(runCourseDetail);
    for (RunCourseDetail courseDetail : list) {
      if (courseDetail.getUserId() != null) {
        courseDetail.setUsername(userService.selectRunUserById(courseDetail.getUserId()).getName());
      }

      RunCourseDevice runCourseDevice = new RunCourseDevice();
      runCourseDevice.setCourseId(Long.parseLong(courseDetail.getCourseId().toString()));
      runCourseDevice.setUserId(Long.parseLong(courseDetail.getUserId().toString()));
      List<RunCourseDevice> deviceList = deviceService.selectRunCourseDeviceList(runCourseDevice);
      if (deviceList.size() > 0) {
        courseDetail.setDeviceId(deviceList.get(0).getDeviceId());
      }
    }
    return getDataTable(list);
  }

  /** 导出课程详情列表 */
  @RequiresPermissions("runmanager:detail:export")
  @Log(title = "课程详情", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunCourseDetail runCourseDetail) {
    List<RunCourseDetail> list = runCourseDetailService.selectRunCourseDetailList(runCourseDetail);
    ExcelUtil<RunCourseDetail> util = new ExcelUtil<RunCourseDetail>(RunCourseDetail.class);
    return util.exportExcel(list, "detail");
  }

  /** 新增课程详情 */
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }

  /** 新增保存课程详情 */
  @RequiresPermissions("runmanager:detail:add")
  @Log(title = "课程详情", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunCourseDetail runCourseDetail) {
    return toAjax(runCourseDetailService.insertRunCourseDetail(runCourseDetail));
  }

  /** 修改课程详情 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, ModelMap mmap) {
    RunCourseDetail runCourseDetail = runCourseDetailService.selectRunCourseDetailById(id);
    mmap.put("runCourseDetail", runCourseDetail);
    return prefix + "/edit";
  }

  /** 修改保存课程详情 */
  @RequiresPermissions("runmanager:detail:edit")
  @Log(title = "课程详情", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunCourseDetail runCourseDetail) {
    return toAjax(runCourseDetailService.updateRunCourseDetail(runCourseDetail));
  }

  /** 删除课程详情 */
  @RequiresPermissions("runmanager:detail:remove")
  @Log(title = "课程详情", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    return toAjax(runCourseDetailService.deleteRunCourseDetailByIds(ids));
  }
}
