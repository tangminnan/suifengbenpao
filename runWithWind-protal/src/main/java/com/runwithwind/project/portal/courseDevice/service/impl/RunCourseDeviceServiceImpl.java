package com.runwithwind.project.portal.courseDevice.service.impl;

import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.courseDevice.domain.RunCourseDevice;
import com.runwithwind.project.portal.courseDevice.mapper.RunCourseDeviceMapper;
import com.runwithwind.project.portal.courseDevice.service.IRunCourseDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 课程中设备记录Service业务层处理
 *
 * @author jzf
 * @date 2021-01-04
 */
@Service
public class RunCourseDeviceServiceImpl implements IRunCourseDeviceService {
  @Autowired private RunCourseDeviceMapper runCourseDeviceMapper;

  /**
   * 查询课程中设备记录
   *
   * @param id 课程中设备记录ID
   * @return 课程中设备记录
   */
  @Override
  public RunCourseDevice selectRunCourseDeviceById(Long id) {
    return runCourseDeviceMapper.selectRunCourseDeviceById(id);
  }

  /**
   * 查询课程中设备记录列表
   *
   * @param runCourseDevice 课程中设备记录
   * @return 课程中设备记录
   */
  @Override
  public List<RunCourseDevice> selectRunCourseDeviceList(RunCourseDevice runCourseDevice) {
    return runCourseDeviceMapper.selectRunCourseDeviceList(runCourseDevice);
  }

  /**
   * 新增课程中设备记录
   *
   * @param runCourseDevice 课程中设备记录
   * @return 结果
   */
  @Override
  public int insertRunCourseDevice(RunCourseDevice runCourseDevice) {
    return runCourseDeviceMapper.insertRunCourseDevice(runCourseDevice);
  }

  /**
   * 修改课程中设备记录
   *
   * @param runCourseDevice 课程中设备记录
   * @return 结果
   */
  @Override
  public int updateRunCourseDevice(RunCourseDevice runCourseDevice) {
    return runCourseDeviceMapper.updateRunCourseDevice(runCourseDevice);
  }

  /**
   * 删除课程中设备记录对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseDeviceByIds(String ids) {
    return runCourseDeviceMapper.deleteRunCourseDeviceByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除课程中设备记录信息
   *
   * @param id 课程中设备记录ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseDeviceById(Long id) {
    return runCourseDeviceMapper.deleteRunCourseDeviceById(id);
  }

  @Override
  public List<Map> selectHistoryByUserId(Long userId) {
    return runCourseDeviceMapper.selectHistoryByUserId(userId);
  }

  /*@Override
  public int insertRunCourseDeviceList(List<RunCourseDevice> deviceList) {
      return runCourseDeviceMapper.insertRunCourseDeviceList(deviceList);
  }*/
}
