package com.runwithwind.project.runmanager.box.service;

import com.runwithwind.project.runmanager.box.domain.RunBox;

import java.util.List;

/**
 * 盒子Service接口
 *
 * @author jzf
 * @date 2020-12-22
 */
public interface IRunBoxService {
  /**
   * 查询盒子
   *
   * @param id 盒子ID
   * @return 盒子
   */
  public RunBox selectRunBoxById(Long id);

  /**
   * 查询盒子列表
   *
   * @param runBox 盒子
   * @return 盒子集合
   */
  public List<RunBox> selectRunBoxList(RunBox runBox);

  /**
   * 新增盒子
   *
   * @param runBox 盒子
   * @return 结果
   */
  public int insertRunBox(RunBox runBox);

  /**
   * 修改盒子
   *
   * @param runBox 盒子
   * @return 结果
   */
  public int updateRunBox(RunBox runBox);

  /**
   * 批量删除盒子
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRunBoxByIds(String ids);

  /**
   * 删除盒子信息
   *
   * @param id 盒子ID
   * @return 结果
   */
  public int deleteRunBoxById(Long id);

  String checkBoxMacUnique(RunBox runBox);
}
