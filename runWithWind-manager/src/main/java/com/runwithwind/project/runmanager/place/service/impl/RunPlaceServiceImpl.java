package com.runwithwind.project.runmanager.place.service.impl;

import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.box.service.IRunBoxService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.mapper.RunPlaceMapper;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.runmanager.user.domain.RunUser;
import com.runwithwind.project.runmanager.user.service.IRunUserService;
import com.runwithwind.project.system.user.domain.User;
import com.runwithwind.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场地列Service业务层处理
 *
 * @author jzf
 * @date 2021-01-18
 */
@Service
public class RunPlaceServiceImpl implements IRunPlaceService {
  @Autowired private RunPlaceMapper runPlaceMapper;
  @Autowired private IRunPlaceService runPlaceService;
  @Autowired private IUserService userService;
  @Autowired private IRunUserService runUserService;
  @Autowired private IRunBoxService runBoxService;

  /**
   * 查询场地列
   *
   * @param id 场地列ID
   * @return 场地列
   */
  @Override
  public RunPlace selectRunPlaceById(Integer id) {
    return runPlaceMapper.selectRunPlaceById(id);
  }

  @Override
  public RunPlace selectRunPlaceByPhone(String phone) {
    return runPlaceMapper.selectRunPlaceByPhone(phone);
  }

  /**
   * 查询场地列列表
   *
   * @param runPlace 场地列
   * @return 场地列
   */
  @Override
  public List<RunPlace> selectRunPlaceList(RunPlace runPlace) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace11 = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runPlace.setId(runPlace11.getId());
    }
    return runPlaceMapper.selectRunPlaceList(runPlace);
  }

  /**
   * 新增场地列
   *
   * @param runPlace 场地列
   * @return 结果
   */
  @Override
  public int insertRunPlace(RunPlace runPlace) {
    runPlace.setCreateTime(DateUtils.getNowDate());
    return runPlaceMapper.insertRunPlace(runPlace);
  }

  /**
   * 修改场地列
   *
   * @param runPlace 场地列
   * @return 结果
   */
  @Override
  public int updateRunPlace(RunPlace runPlace) {
    return runPlaceMapper.updateRunPlace(runPlace);
  }

  /**
   * 删除场地列对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunPlaceByIds(String ids) {
    System.out.println("=============ids============" + ids);
    String[] id = ids.split(",");
    for (String s : id) {
      RunPlace runPlace = selectRunPlaceById(Integer.parseInt(s));
      if (runPlace.getPhone() != null) {
        User user = userService.selectUserByLoginName(runPlace.getPhone());
        userService.deleteUserById(user.getUserId());
      }

      RunUser runUser = new RunUser();
      runUser.setPlaceId(runPlace.getId());
      List<RunUser> userList = runUserService.selectRunUserList(runUser);
      for (RunUser user : userList) {
        user.setPlaceId(0);
        runUserService.updateRunUser(user);
      }
    }

    return runPlaceMapper.deleteRunPlaceByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除场地列信息
   *
   * @param id 场地列ID
   * @return 结果
   */
  @Override
  public int deleteRunPlaceById(Integer id) {
    return runPlaceMapper.deleteRunPlaceById(id);
  }

  @Override
  public RunPlace selectRunPlaceByLoginName(String loginName) {
    return runPlaceMapper.selectRunPlaceByLoginName(loginName);
  }
}
