package com.runwithwind.project.runmanager.place.controller;

import com.runwithwind.common.constant.UserConstants;
import com.runwithwind.common.utils.poi.ExcelUtil;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.framework.aspectj.lang.annotation.Log;
import com.runwithwind.framework.aspectj.lang.enums.BusinessType;
import com.runwithwind.framework.shiro.service.PasswordService;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.framework.web.domain.AjaxResult;
import com.runwithwind.framework.web.page.TableDataInfo;
import com.runwithwind.project.runmanager.box.domain.RunBox;
import com.runwithwind.project.runmanager.box.service.IRunBoxService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.system.user.domain.User;
import com.runwithwind.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 场地列Controller
 *
 * @author jzf
 * @date 2021-01-18
 */
@Controller
@RequestMapping("/runmanager/place")
public class RunPlaceController extends BaseController {
  private String prefix = "runmanager/place";

  @Autowired private IRunPlaceService runPlaceService;
  @Autowired private IUserService userService;
  @Autowired private PasswordService passwordService;

  @Autowired private IRunBoxService runBoxService;

  @RequiresPermissions("runmanager:place:view")
  @GetMapping()
  public String place() {
    return prefix + "/place";
  }

  /** 查询场地列列表 */
  @RequiresPermissions("runmanager:place:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(RunPlace runPlace) {
    startPage();
    runPlace.setDelFlag(1);
    List<RunPlace> list = runPlaceService.selectRunPlaceList(runPlace);
    return getDataTable(list);
  }

  @PostMapping("/okList")
  @ResponseBody
  public List<RunPlace> okList() {
    List<RunPlace> runPlaceList = runPlaceService.selectRunPlaceList(new RunPlace());
    for (RunPlace place : runPlaceList) {
      RunBox box = new RunBox();
      box.setBindPlace(place.getId());
      if (runBoxService.selectRunBoxList(box).size() >= place.getBoxCount()) {
        runPlaceList.remove(place);
      }
    }
    return runPlaceList;
  }

  /** 导出场地列列表 */
  @RequiresPermissions("runmanager:place:export")
  @Log(title = "场地列", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(RunPlace runPlace) {
    List<RunPlace> list = runPlaceService.selectRunPlaceList(runPlace);
    ExcelUtil<RunPlace> util = new ExcelUtil<RunPlace>(RunPlace.class);
    return util.exportExcel(list, "place");
  }

  /** 新增场地列 */
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }

  /** 新增保存场地列 */
  @RequiresPermissions("runmanager:place:add")
  @Log(title = "场地列", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(RunPlace runPlace) {
    runPlace.setDelFlag(1);

    User user = new User();
    user.setUserName(runPlace.getPlaceName());
    user.setPhonenumber(runPlace.getPhone());
    user.setLoginName(runPlace.getLoginName());
    user.setUserType("02");

    user.randomSalt();

    // user.setPassword(passwordService.encryptPassword(runPlace.getPhone(),runPlace.getPassword(),
    // user.getSalt()));
    user.setPassword(runPlace.getPassword());

    user.setCreateBy(ShiroUtils.getLoginName());

    if (UserConstants.USER_NAME_NOT_UNIQUE.equals(
        userService.checkLoginNameUnique(user.getLoginName()))) {
      return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
    } else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
      return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
    } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
      return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
    } else {
      if (userService.insertUser(user) > 0) {
        return toAjax(runPlaceService.insertRunPlace(runPlace));
      } else {
        return error("新增角色失败");
      }
    }
  }

  /** 修改场地列 */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
    RunPlace runPlace = runPlaceService.selectRunPlaceById(id);
    mmap.put("runPlace", runPlace);
    return prefix + "/edit";
  }

  /** 修改保存场地列 */
  @RequiresPermissions("runmanager:place:edit")
  @Log(title = "场地列", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(RunPlace runPlace) {
    runPlace.setDelFlag(1);
    return toAjax(runPlaceService.updateRunPlace(runPlace));
  }

  /** 删除场地列 */
  @RequiresPermissions("runmanager:place:remove")
  @Log(title = "场地列", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    int result = 0;
    String[] id = Convert.toStrArray(ids);
    for (String s : id) {
      RunPlace runPlace = runPlaceService.selectRunPlaceById(Integer.parseInt(s));
      runPlace.setDelFlag(2);

      result += runPlaceService.updateRunPlace(runPlace);

      User loginUser = userService.selectUserByLoginName(runPlace.getLoginName());
      userService.deleteUserById(loginUser.getUserId());
    }
    return toAjax(result);
  }
}
