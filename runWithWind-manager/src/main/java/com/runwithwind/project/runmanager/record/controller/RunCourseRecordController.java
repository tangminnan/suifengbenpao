package com.runwithwind.project.runmanager.record.controller;

import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.pad.domain.RunPad;
import com.runwithwind.project.runmanager.pad.service.IRunPadService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.runmanager.record.domain.RunCourseRecord;
import com.runwithwind.project.runmanager.record.service.IRunCourseRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程记录Controller
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/record")
public class RunCourseRecordController extends BaseController {
  private String prefix = "runmanager/record";

  @Autowired private IRunCourseRecordService runCourseRecordService;
  @Autowired private IRunPadService padService;

  @Autowired private IRunPlaceService placeService;

  @Autowired private IRunPadService runPadService;

  @RequiresPermissions("runmanager:record:view")
  @GetMapping()
  public String record(Model model) {
    model.addAttribute("padList", runPadService.selectRunPadList(new RunPad()));
    model.addAttribute("placeList", placeService.selectRunPlaceList(new RunPlace()));
    return prefix + "/record";
  }

  /** 查询课程记录列表 */
  @RequiresPermissions("runmanager:record:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunCourseRecord runCourseRecord) {
    startPage();
    List<RunCourseRecord> list = runCourseRecordService.selectRunCourseRecordList(runCourseRecord);
    for (RunCourseRecord courseRecord : list) {
      if (courseRecord.getPadMac() != null) {
        courseRecord.setPadName(padService.selectRunPadByMac(courseRecord.getPadMac()).getName());
      }
      if (courseRecord.getPlaceId() != null) {
        courseRecord.setPlaceName(
            placeService.selectRunPlaceById(courseRecord.getPlaceId()).getPlaceName());
      }
    }
    return getDataTable(list);
  }

  /** 导出课程记录列表 */
  @RequiresPermissions("runmanager:record:export")
  @Log(title = "课程记录", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunCourseRecord runCourseRecord) {
    List<RunCourseRecord> list = runCourseRecordService.selectRunCourseRecordList(runCourseRecord);
    ExcelUtil<RunCourseRecord> util = new ExcelUtil<RunCourseRecord>(RunCourseRecord.class);
    return util.exportExcel(list, "record");
  }

  /** 新增课程记录 */
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }

  /** 新增保存课程记录 */
  @RequiresPermissions("runmanager:record:add")
  @Log(title = "课程记录", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunCourseRecord runCourseRecord) {
    return toAjax(runCourseRecordService.insertRunCourseRecord(runCourseRecord));
  }

  /** 修改课程记录 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
    RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(id);
    mmap.put("runCourseRecord", runCourseRecord);
    return prefix + "/edit";
  }

  /** 修改保存课程记录 */
  @RequiresPermissions("runmanager:record:edit")
  @Log(title = "课程记录", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunCourseRecord runCourseRecord) {
    return toAjax(runCourseRecordService.updateRunCourseRecord(runCourseRecord));
  }

  /** 删除课程记录 */
  @RequiresPermissions("runmanager:record:remove")
  @Log(title = "课程记录", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    return toAjax(runCourseRecordService.deleteRunCourseRecordByIds(ids));
  }
}
