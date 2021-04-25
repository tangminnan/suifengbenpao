package com.runwithwind.project.runmanager.box.controller;

import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.box.domain.RunBox;
import com.runwithwind.project.runmanager.box.service.IRunBoxService;
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
 * 盒子Controller
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/box")
public class RunBoxController extends BaseController {
  private String prefix = "runmanager/box";

  @Autowired private IRunBoxService runBoxService;
  @Autowired private IRunPlaceService placeService;
  @Autowired private IRunPadService padService;

  @RequiresPermissions("runmanager:box:view")
  @GetMapping()
  public String box(Model model) {
    RunPlace runPlace = new RunPlace();
    runPlace.setDelFlag(1);
    List<RunPlace> runPlaceList = placeService.selectRunPlaceList(runPlace);
    for (RunPlace place : runPlaceList) {
      RunBox box = new RunBox();
      box.setBindPlace(place.getId());
      if (runBoxService.selectRunBoxList(box).size() > place.getBoxCount()) {
        runPlaceList.remove(place);
      }
    }

    model.addAttribute("placeList", runPlaceList);
    RunPad pad = new RunPad();
    pad.setIsBind(2);
    model.addAttribute("padList", padService.selectRunPadList(pad));
    return prefix + "/box";
  }

  /** 查询盒子列表 */
  @RequiresPermissions("runmanager:box:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunBox runBox) {
    startPage();
    runBox.setDelFlag(1);
    List<RunBox> list = runBoxService.selectRunBoxList(runBox);
    for (RunBox box : list) {
      if (box.getBindPlace() != null && !box.getBindPlace().equals("")) {
        RunPlace runPlace = placeService.selectRunPlaceById(box.getBindPlace());
        if (runPlace != null) box.setPlaceName(runPlace.getPlaceName());
      }
      if (box.getBindPad() != null && !box.getBindPad().equals("")) {
        RunPad runPad = padService.selectRunPadById(box.getBindPad());
        if (runPad != null) box.setPadName(runPad.getName());
      }
    }
    return getDataTable(list);
  }

  /** 导出盒子列表 */
  @RequiresPermissions("runmanager:box:export")
  @Log(title = "盒子", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunBox runBox) {
    List<RunBox> list = runBoxService.selectRunBoxList(runBox);
    ExcelUtil<RunBox> util = new ExcelUtil<RunBox>(RunBox.class);
    return util.exportExcel(list, "box");
  }

  /** 新增盒子 */
  @GetMapping("/add")
  public String add(Model model) {
    List<RunPlace> runPlaceList = placeService.selectRunPlaceList(new RunPlace());
    for (RunPlace place : runPlaceList) {
      RunBox box = new RunBox();
      box.setBindPlace(place.getId());
      if (runBoxService.selectRunBoxList(box).size() >= place.getBoxCount()) {
        runPlaceList.remove(place);
      }
    }

    model.addAttribute("placeList", runPlaceList);
    RunPad pad = new RunPad();
    pad.setIsBind(2);
    model.addAttribute("padList", padService.selectRunPadList(pad));
    return prefix + "/add";
  }

  /** 新增保存盒子 */
  @RequiresPermissions("runmanager:box:add")
  @Log(title = "盒子", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunBox runBox) {
    runBox.setDelFlag(1);
    return toAjax(runBoxService.insertRunBox(runBox));
  }

  /** 修改盒子 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, ModelMap mmap) {
    RunBox runBox = runBoxService.selectRunBoxById(id);
    mmap.put("runBox", runBox);
    return prefix + "/edit";
  }

  /** 修改保存盒子 */
  @RequiresPermissions("runmanager:box:edit")
  @Log(title = "盒子", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunBox runBox) {
    runBox.setDelFlag(1);
    return toAjax(runBoxService.updateRunBox(runBox));
  }

  /** 删除盒子 */
  @RequiresPermissions("runmanager:box:remove")
  @Log(title = "盒子", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    int result = 0;
    String[] id = Convert.toStrArray(ids);
    for (String s : id) {
      RunBox runBox = runBoxService.selectRunBoxById(Long.parseLong(s));
      runBox.setDelFlag(2);

      result += runBoxService.updateRunBox(runBox);
    }
    return toAjax(result);
  }

  /** 分配心率带 */
  @GetMapping("/sendDevice")
  @RequiresPermissions("runmanager:box:sendDevice")
  public String sendDevice(Model model) {
    // 全部的场地
    List<RunPlace> placeList = placeService.selectRunPlaceList(new RunPlace());

    // 需要返回还可以配置设备的场地
    List<RunPlace> runPlaceList = new ArrayList<>(placeList);
    for (RunPlace runPlace : placeList) {
      RunBox box = new RunBox();
      box.setBindPlace(runPlace.getId());
      if (runBoxService.selectRunBoxList(box).size() >= runPlace.getBoxCount()) {
        runPlaceList.remove(runPlace);
      }
    }

    model.addAttribute("placeList", runPlaceList);
    // 未绑定的心率带
    List<RunBox> runBoxList = new ArrayList();
    // 全部的心率带
    List<RunBox> boxList = runBoxService.selectRunBoxList(new RunBox());
    for (RunBox runBox : boxList) {
      if (runBox.getBindPlace() == null || runBox.getBindPlace() == 0) {
        runBoxList.add(runBox);
      }
    }
    model.addAttribute("runPadList", runBoxList);
    return prefix + "/sendDevice";
  }

  /** 分配心率带 */
  @RequiresPermissions("runmanager:box:sendDevice")
  @PostMapping("/sendDeviceToPlace")
  @ResponseBody
  public R sendDeviceToPlace(Integer placeId, String deviceIds, Model model) {
    System.out.println(
        "===========placeId==========" + placeId + "==========deviceIds==========" + deviceIds);
    int result = 0;
    String[] deviceId = deviceIds.split(",");
    for (String s : deviceId) {
      RunBox runBox = new RunBox();
      if (s != null) runBox.setId(Long.parseLong(s));
      runBox.setBindPlace(placeId);
      result += runBoxService.updateRunBox(runBox);
    }
    if (result >= 0) {
      return R.ok();
    } else {
      return R.error();
    }
  }

  /** 校验用户名 */
  @PostMapping("/checkBoxMacUnique")
  @ResponseBody
  public String checkDeviceIdUnique(RunBox box) {
    return runBoxService.checkBoxMacUnique(box);
  }
}
