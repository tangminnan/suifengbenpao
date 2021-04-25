package com.runwithwind.project.runmanager.user.service.impl;

import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.runmanager.user.domain.RunUser;
import com.runwithwind.project.runmanager.user.mapper.RunUserMapper;
import com.runwithwind.project.runmanager.user.service.IRunUserService;
import com.runwithwind.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户列Service业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunUserServiceImpl implements IRunUserService {
  @Autowired private RunUserMapper runUserMapper;
  @Autowired private IRunPlaceService runPlaceService;

  /**
   * 查询用户列
   *
   * @param id 用户列ID
   * @return 用户列
   */
  @Override
  public RunUser selectRunUserById(Integer id) {
    return runUserMapper.selectRunUserById(id);
  }

  /**
   * 查询用户列列表
   *
   * @param runUser 用户列
   * @return 用户列
   */
  @Override
  public List<RunUser> selectRunUserList(RunUser runUser) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runUser.setPlaceId(runPlace.getId());
    }
    return runUserMapper.selectRunUserList(runUser);
  }

  /**
   * 新增用户列
   *
   * @param runUser 用户列
   * @return 结果
   */
  @Override
  public int insertRunUser(RunUser runUser) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByPhone(currentUser.getLoginName());
      runUser.setPlaceId(runPlace.getId());
    }
    return runUserMapper.insertRunUser(runUser);
  }

  /**
   * 修改用户列
   *
   * @param runUser 用户列
   * @return 结果
   */
  @Override
  public int updateRunUser(RunUser runUser) {
    return runUserMapper.updateRunUser(runUser);
  }

  /**
   * 删除用户列对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunUserByIds(String ids) {
    return runUserMapper.deleteRunUserByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除用户列信息
   *
   * @param id 用户列ID
   * @return 结果
   */
  @Override
  public int deleteRunUserById(Integer id) {
    return runUserMapper.deleteRunUserById(id);
  }

  @Override
  public List<RunUser> selectRunUserPartList(RunUser runUser) {
    return runUserMapper.selectRunUserPartList(runUser);
  }
}
