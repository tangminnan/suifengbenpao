package com.runwithwind.project.runmanager.pad.controller;

import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.pad.domain.RunPad;
import com.runwithwind.project.runmanager.pad.service.IRunPadService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * padController
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/pad")
public class RunPadController extends BaseController {
  private String prefix = "runmanager/pad";

  @Autowired private IRunPadService runPadService;
  @Autowired private IRunPlaceService placeService;

  @RequiresPermissions("runmanager:pad:view")
  @GetMapping()
  public String pad() {
    return prefix + "/pad";
  }

  /** 查询pad列表 */
  @RequiresPermissions("runmanager:pad:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunPad runPad) {
    startPage();
    runPad.setDelFlag(1);
    List<RunPad> list = runPadService.selectRunPadList(runPad);
    return getDataTable(list);
  }

  /** 导出pad列表 */
  @RequiresPermissions("runmanager:pad:export")
  @Log(title = "pad", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunPad runPad) {
    List<RunPad> list = runPadService.selectRunPadList(runPad);
    ExcelUtil<RunPad> util = new ExcelUtil<RunPad>(RunPad.class);
    return util.exportExcel(list, "pad");
  }

  /** 新增pad */
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }

  /** 新增保存pad */
  @RequiresPermissions("runmanager:pad:add")
  @Log(title = "pad", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunPad runPad) {
    runPad.setDelFlag(1);
    runPad.setIsBind(2);
    return toAjax(runPadService.insertRunPad(runPad));
  }

  /** 修改pad */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, ModelMap mmap) {
    RunPad runPad = runPadService.selectRunPadById(id);
    mmap.put("runPad", runPad);
    return prefix + "/edit";
  }

  /** 修改保存pad */
  @RequiresPermissions("runmanager:pad:edit")
  @Log(title = "pad", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunPad runPad) {
    runPad.setDelFlag(1);
    return toAjax(runPadService.updateRunPad(runPad));
  }

  /** 删除pad */
  @RequiresPermissions("runmanager:pad:remove")
  @Log(title = "pad", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    int result = 0;
    String[] id = Convert.toStrArray(ids);
    for (String s : id) {
      RunPad runPad = runPadService.selectRunPadById(Long.parseLong(s));
      runPad.setDelFlag(2);

      result += runPadService.updateRunPad(runPad);
    }
    return toAjax(result);
  }

  /** 分配心率带 */
  @GetMapping("/sendDevice")
  @RequiresPermissions("runmanager:pad:sendDevice")
  public String sendDevice(Model model) {
    // 全部的场地
    List<RunPlace> placeList = placeService.selectRunPlaceList(new RunPlace());

    // 需要返回还可以配置设备的场地
    List<RunPlace> runPlaceList = new ArrayList<>(placeList);
    for (RunPlace runPlace : placeList) {
      RunPad pad = new RunPad();
      pad.setPlaceId(runPlace.getId());
      if (runPadService.selectRunPadList(pad).size() >= runPlace.getPadCount()) {
        runPlaceList.remove(runPlace);
      }
    }

    model.addAttribute("placeList", runPlaceList);
    // 未绑定的心率带
    List<RunPad> runPadList = new ArrayList();
    // 全部的心率带
    List<RunPad> padList = runPadService.selectRunPadList(new RunPad());
    for (RunPad runPad : padList) {
      if (runPad.getPlaceId() == null || runPad.getPlaceId() == 0) {
        runPadList.add(runPad);
      }
    }
    model.addAttribute("runPadList", runPadList);
    return prefix + "/sendDevice";
  }

  /** 分配心率带 */
  @RequiresPermissions("runmanager:pad:sendDevice")
  @PostMapping("/sendDeviceToPlace")
  @ResponseBody
  public R sendDeviceToPlace(Integer placeId, String deviceIds, Model model) {
    int result = 0;
    String[] deviceId = deviceIds.split(",");
    for (String s : deviceId) {
      RunPad runPad = new RunPad();
      if (s != null) runPad.setId(Long.parseLong(s));
      runPad.setPlaceId(placeId);
      runPad.setDelFlag(1);
      result += runPadService.updateRunPad(runPad);
    }
    if (result >= 0) {
      return R.ok();
    } else {
      return R.error();
    }
  }

  /** 校验用户名 */
  @PostMapping("/checkPadMacUnique")
  @ResponseBody
  public String checkPadMacUnique(RunPad runPad) {
    return runPadService.checkPadMacUnique(runPad);
  }
}
