package com.runwithwind.project.portal.courseDevice.service;

import com.runwithwind.project.portal.courseDevice.domain.RunCourseDevice;

import java.util.List;
import java.util.Map;

/**
 * 课程中设备记录Service接口
 *
 * @author jzf
 * @date 2021-01-04
 */
public interface IRunCourseDeviceService {
  /**
   * 查询课程中设备记录
   *
   * @param id 课程中设备记录ID
   * @return 课程中设备记录
   */
  public RunCourseDevice selectRunCourseDeviceById(Long id);

  /**
   * 查询课程中设备记录列表
   *
   * @param runCourseDevice 课程中设备记录
   * @return 课程中设备记录集合
   */
  public List<RunCourseDevice> selectRunCourseDeviceList(RunCourseDevice runCourseDevice);

  /**
   * 新增课程中设备记录
   *
   * @param runCourseDevice 课程中设备记录
   * @return 结果
   */
  public int insertRunCourseDevice(RunCourseDevice runCourseDevice);

  /**
   * 修改课程中设备记录
   *
   * @param runCourseDevice 课程中设备记录
   * @return 结果
   */
  public int updateRunCourseDevice(RunCourseDevice runCourseDevice);

  /**
   * 批量删除课程中设备记录
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRunCourseDeviceByIds(String ids);

  /**
   * 删除课程中设备记录信息
   *
   * @param id 课程中设备记录ID
   * @return 结果
   */
  public int deleteRunCourseDeviceById(Long id);

  List<Map> selectHistoryByUserId(Long userId);

  // int insertRunCourseDeviceList(List<RunCourseDevice> );
}