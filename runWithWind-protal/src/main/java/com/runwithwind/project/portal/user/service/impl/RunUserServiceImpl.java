package com.runwithwind.project.portal.user.service.impl;

import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.mapper.RunUserMapper;
import com.runwithwind.project.portal.user.service.IRunUserService;
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
  public RunUser selectRunUserByPhone(String username) {
    return runUserMapper.selectRunUserByPhone(username);
  }

  @Override
  public boolean exit(RunUser user) {
    boolean exit = false;
    if (runUserMapper.selectRunUserList(user).size() > 0) exit = true;
    return exit;
  }

  @Override
  public RunUser selectRunUserByOpenId(String openId) {
    return runUserMapper.selectRunUserByOpenId(openId);
  }
}
