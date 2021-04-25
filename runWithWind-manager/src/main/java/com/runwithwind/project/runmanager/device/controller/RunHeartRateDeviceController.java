package com.runwithwind.project.runmanager.device.controller;

import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.device.domain.RunHeartRateDevice;
import com.runwithwind.project.runmanager.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.runmanager.user.domain.RunUser;
import com.runwithwind.project.runmanager.user.service.IRunUserService;
import com.runwithwind.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 心率带Controller
 *
 * @author jzf
 * @date 2020-12-22
 */
@Controller
@RequestMapping("/runmanager/device")
public class RunHeartRateDeviceController extends BaseController {
  private String prefix = "runmanager/device";

  @Autowired private IRunHeartRateDeviceService runHeartRateDeviceService;
  @Autowired private IRunUserService userService;

  @Autowired private IRunPlaceService placeService;

  @RequiresPermissions("runmanager:device:view")
  @GetMapping()
  public String device(Model model) {

    model.addAttribute("userList", userService.selectRunUserList(new RunUser()));
    return prefix + "/device";
  }

  /** 查询心率带列表 */
  @RequiresPermissions("runmanager:device:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunHeartRateDevice runHeartRateDevice) {
    startPage();
    List<RunHeartRateDevice> list =
        runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
    for (RunHeartRateDevice heartRateDevice : list) {
      if (heartRateDevice.getUserId() != null && heartRateDevice.getUserId() > 0) {
        RunUser user = userService.selectRunUserById(heartRateDevice.getUserId());
        if (user != null) heartRateDevice.setUsername(user.getId() + "--" + user.getName());
        else {
          heartRateDevice.setUsername(heartRateDevice.getUserId() + "");
        }
      }
    }
    return getDataTable(list);
  }

  /** 导出心率带列表 */
  @RequiresPermissions("runmanager:device:export")
  @Log(title = "心率带", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunHeartRateDevice runHeartRateDevice) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = placeService.selectRunPlaceByPhone(currentUser.getLoginName());
      runHeartRateDevice.setPlaceId(runPlace.getId());
    }
    List<RunHeartRateDevice> list =
        runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
    ExcelUtil<RunHeartRateDevice> util =
        new ExcelUtil<RunHeartRateDevice>(RunHeartRateDevice.class);
    return util.exportExcel(list, "device");
  }

  /** 新增心率带 */
  @GetMapping("/add")
  public String add(Model model) {
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();

    // 查询已绑定设备的人
    runHeartRateDevice.setisbind(1);
    List<RunHeartRateDevice> runHeartRateDeviceList =
        runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
    List<RunUser> userList = new ArrayList<>();
    for (RunHeartRateDevice heartRateDevice : runHeartRateDeviceList) {
      if (heartRateDevice.getUserId() != null && heartRateDevice.getUserId() > 0) {
        RunUser user = userService.selectRunUserById(heartRateDevice.getUserId());
        userList.add(user);
      }
    }

    List<RunUser> runUserList = new ArrayList<>(userService.selectRunUserList(new RunUser()));
    for (RunUser user : userList) {
      Iterator<RunUser> iterator = runUserList.iterator();
      while (iterator.hasNext()) {
        if (user.getId() == iterator.next().getId()) {
          iterator.remove();
        }
      }
    }
    model.addAttribute("list", runUserList);
    return prefix + "/add";
  }

  @ResponseBody
  @GetMapping("/selectWBD")
  public R selectWBD() {
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();

    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = placeService.selectRunPlaceByPhone(currentUser.getLoginName());
      runHeartRateDevice.setPlaceId(runPlace.getId());
    }
    runHeartRateDevice.setisbind(1);
    List<RunHeartRateDevice> runHeartRateDeviceList =
        runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
    List<RunUser> userList = new LinkedList<>();
    for (RunHeartRateDevice heartRateDevice : runHeartRateDeviceList) {
      if (heartRateDevice.getUserId() != null && heartRateDevice.getUserId() > 0) {
        RunUser user = userService.selectRunUserById(heartRateDevice.getUserId());
        userList.add(user);
      }
    }
    List<RunUser> runUserList = new ArrayList<>(userService.selectRunUserList(new RunUser()));

    for (RunUser user : userList) {
      Iterator<RunUser> iterator = runUserList.iterator();
      while (iterator.hasNext()) {
        if (user.getId() == iterator.next().getId()) {
          iterator.remove();
        }
      }
    }

    return R.data(runUserList);
  }

  /** 分配心率带 */
  @GetMapping("/sendDevice")
  @RequiresPermissions("runmanager:device:sendDevice")
  public String sendDevice(Model model) {
    // 全部的场地
    List<RunPlace> placeList = placeService.selectRunPlaceList(new RunPlace());

    // 需要返回还可以配置设备的场地
    List<RunPlace> runPlaceList = new ArrayList<>(placeList);
    for (RunPlace runPlace : placeList) {
      RunHeartRateDevice heartRateDevice = new RunHeartRateDevice();
      heartRateDevice.setPlaceId(runPlace.getId());
      if (runHeartRateDeviceService.selectRunHeartRateDeviceList(heartRateDevice).size()
          >= runPlace.getDeviceCount()) {
        runPlaceList.remove(runPlace);
      }
    }

    model.addAttribute("placeList", runPlaceList);
    // 未绑定的心率带
    List<RunHeartRateDevice> heartRateDevices = new ArrayList();
    // 全部的心率带
    List<RunHeartRateDevice> runHeartRateDeviceList =
        runHeartRateDeviceService.selectRunHeartRateDeviceList(new RunHeartRateDevice());
    for (RunHeartRateDevice runHeartRateDevice : runHeartRateDeviceList) {
      if (runHeartRateDevice.getPlaceId() == null || runHeartRateDevice.getPlaceId() == 0) {
        heartRateDevices.add(runHeartRateDevice);
      }
    }
    model.addAttribute("heartRateDevices", heartRateDevices);
    return prefix + "/sendDevice";
  }

  /** 分配心率带 */
  @RequiresPermissions("runmanager:device:sendDevice")
  @PostMapping("/sendDeviceToPlace")
  @ResponseBody
  public AjaxResult sendDeviceToPlace(Integer placeId, String deviceIds, Model model) {
    int result = 0;
    String[] deviceId = deviceIds.split(",");
    for (String s : deviceId) {
      RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
      if (s != null) runHeartRateDevice.setId(Integer.parseInt(s));
      runHeartRateDevice.setPlaceId(placeId);
      result += runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice);
    }
    if (result >= 0) {
      return AjaxResult.success();
    } else {
      return AjaxResult.error();
    }
  }

  /** 新增保存心率带 */
  @RequiresPermissions("runmanager:device:add")
  @Log(title = "心率带", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunHeartRateDevice runHeartRateDevice) {
    if (runHeartRateDevice.getUserId() != null) {
      runHeartRateDevice.setisbind(1);
    } else {
      runHeartRateDevice.setisbind(2);
    }
    return toAjax(runHeartRateDeviceService.insertRunHeartRateDevice(runHeartRateDevice));
  }

  /** 修改心率带 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap, Model model) {
    RunHeartRateDevice runHeartRateDevice =
        runHeartRateDeviceService.selectRunHeartRateDeviceById(id);
    mmap.put("runHeartRateDevice", runHeartRateDevice);
    return prefix + "/edit";
  }

  /** 修改保存心率带 */
  @RequiresPermissions("runmanager:device:edit")
  @Log(title = "心率带", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunHeartRateDevice runHeartRateDevice) {
    return toAjax(runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice));
  }

  /** 删除心率带 */
  @RequiresPermissions("runmanager:device:remove")
  @Log(title = "心率带", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    return toAjax(runHeartRateDeviceService.deleteRunHeartRateDeviceByIds(ids));
  }

  @RequiresPermissions("runmanager:device:edit")
  @PostMapping("/isBindChange")
  @ResponseBody
  public AjaxResult isBindChange(RunHeartRateDevice runHeartRateDevice) {
    if (runHeartRateDevice.getisbind() == 2) {
      runHeartRateDevice.setUserId(0);
    } else if (runHeartRateDevice.getUserId() != null) {
      runHeartRateDevice.setisbind(1);
    }
    return toAjax(runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice));
  }

  /** 校验用户名 */
  @PostMapping("/checkDeviceIdUnique")
  @ResponseBody
  public String checkDeviceIdUnique(RunHeartRateDevice runHeartRateDevice) {
    return runHeartRateDeviceService.checkDeviceIdUnique(runHeartRateDevice);
  }
}
