package com.runwithwind.project.runmanager.user.controller;

import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.interceptor.annotation.RepeatSubmit;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.device.domain.RunHeartRateDevice;
import com.runwithwind.project.runmanager.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.runmanager.user.domain.RunUser;
import com.runwithwind.project.runmanager.user.service.IRunUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户列Controller
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/user")
public class RunUserController extends BaseController {
  private String prefix = "runmanager/user";

  @Autowired private IRunUserService runUserService;

  @Autowired private IRunHeartRateDeviceService heartRateDeviceService;

  @RequiresPermissions("runmanager:user:view")
  @GetMapping()
  public String user() {
    return prefix + "/user";
  }

  /** 查询用户列列表 */
  @RequiresPermissions("runmanager:user:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunUser runUser) {

    startPage();
    runUser.setType(1);
    List<RunUser> list = runUserService.selectRunUserList(runUser);
    for (RunUser user : list) {
      RunHeartRateDevice runHeartRateDevice =
          heartRateDeviceService.selectRunHeartRateDeviceByUserId(user.getId());
      if (runHeartRateDevice != null && runHeartRateDevice.getDeviceId() != null)
        user.setDevice(runHeartRateDevice.getDeviceId());
    }
    return getDataTable(list);
  }

  /** 导出用户列列表 */
  @RequiresPermissions("runmanager:user:export")
  @Log(title = "用户列", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunUser runUser) {
    List<RunUser> list = runUserService.selectRunUserPartList(runUser);
    ExcelUtil<RunUser> util = new ExcelUtil<RunUser>(RunUser.class);
    return util.exportExcel(list, "用户信息表");
  }

  /** 新增用户列 */
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }

  /** 新增保存用户列 */
  @RepeatSubmit
  @RequiresPermissions("runmanager:user:add")
  @Log(title = "用户列", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunUser runUser) {

    return toAjax(runUserService.insertRunUser(runUser));
  }

  /** 修改用户列 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
    RunUser runUser = runUserService.selectRunUserById(id);
    mmap.put("runUser", runUser);
    return prefix + "/edit";
  }

  /** 修改保存用户列 */
  @RequiresPermissions("runmanager:user:edit")
  @Log(title = "用户列", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunUser runUser) {
    return toAjax(runUserService.updateRunUser(runUser));
  }

  /** 删除用户列 */
  @RequiresPermissions("runmanager:user:remove")
  @Log(title = "用户列", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    if (!ids.contains(",")) {
      RunHeartRateDevice heartRateDevice = new RunHeartRateDevice();
      heartRateDevice.setUserId(Integer.parseInt(ids));
      if (heartRateDeviceService.selectRunHeartRateDeviceList(heartRateDevice).size() > 0) {
        return error("用户有未解除绑定的设备，请先解除绑定");
      } else {
        return toAjax(runUserService.deleteRunUserByIds(ids));
      }
    } else {
      String[] arr = ids.split(",");
      boolean flag = true;
      for (String s : arr) {
        RunHeartRateDevice heartRateDevice = new RunHeartRateDevice();
        heartRateDevice.setUserId(Integer.parseInt(s));
        if (heartRateDeviceService.selectRunHeartRateDeviceList(heartRateDevice).size() > 0) {
          flag = false;
        }
      }
      if (flag) {
        return toAjax(runUserService.deleteRunUserByIds(ids));
      } else {
        return error("已选择的用户有未解除绑定的设备，请先解除绑定");
      }
    }
  }
}
