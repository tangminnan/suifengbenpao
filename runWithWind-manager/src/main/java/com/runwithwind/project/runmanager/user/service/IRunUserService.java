package com.runwithwind.project.runmanager.user.service;

import com.runwithwind.project.runmanager.user.domain.RunUser;

import java.util.List;

/**
 * 用户列Service接口
 *
 * @author jzf
 * @date 2020-12-22
 */
public interface IRunUserService {
  /**
   * 查询用户列
   *
   * @param id 用户列ID
   * @return 用户列
   */
  public RunUser selectRunUserById(Integer id);

  /**
   * 查询用户列列表
   *
   * @param runUser 用户列
   * @return 用户列集合
   */
  public List<RunUser> selectRunUserList(RunUser runUser);

  /**
   * 新增用户列
   *
   * @param runUser 用户列
   * @return 结果
   */
  public int insertRunUser(RunUser runUser);

  /**
   * 修改用户列
   *
   * @param runUser 用户列
   * @return 结果
   */
  public int updateRunUser(RunUser runUser);

  /**
   * 批量删除用户列
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRunUserByIds(String ids);

  /**
   * 删除用户列信息
   *
   * @param id 用户列ID
   * @return 结果
   */
  public int deleteRunUserById(Integer id);

  List<RunUser> selectRunUserPartList(RunUser runUser);
}
