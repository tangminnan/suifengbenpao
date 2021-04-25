package com.runwithwind.project.portal.device.service.impl;

import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.device.domain.RunHeartRateDevice;
import com.runwithwind.project.portal.device.mapper.RunHeartRateDeviceMapper;
import com.runwithwind.project.portal.device.service.IRunHeartRateDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 心率带Service业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunHeartRateDeviceServiceImpl implements IRunHeartRateDeviceService {
  @Autowired private RunHeartRateDeviceMapper runHeartRateDeviceMapper;

  /**
   * 查询心率带
   *
   * @param id 心率带ID
   * @return 心率带
   */
  @Override
  public RunHeartRateDevice selectRunHeartRateDeviceById(Integer id) {
    return runHeartRateDeviceMapper.selectRunHeartRateDeviceById(id);
  }

  /**
   * 查询心率带列表
   *
   * @param runHeartRateDevice 心率带
   * @return 心率带
   */
  @Override
  public List<RunHeartRateDevice> selectRunHeartRateDeviceList(
      RunHeartRateDevice runHeartRateDevice) {
    return runHeartRateDeviceMapper.selectRunHeartRateDeviceList(runHeartRateDevice);
  }

  /**
   * 新增心率带
   *
   * @param runHeartRateDevice 心率带
   * @return 结果
   */
  @Override
  public int insertRunHeartRateDevice(RunHeartRateDevice runHeartRateDevice) {
    return runHeartRateDeviceMapper.insertRunHeartRateDevice(runHeartRateDevice);
  }

  /**
   * 修改心率带
   *
   * @param runHeartRateDevice 心率带
   * @return 结果
   */
  @Override
  public int updateRunHeartRateDevice(RunHeartRateDevice runHeartRateDevice) {
    return runHeartRateDeviceMapper.updateRunHeartRateDevice(runHeartRateDevice);
  }

  /**
   * 删除心率带对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunHeartRateDeviceByIds(String ids) {
    return runHeartRateDeviceMapper.deleteRunHeartRateDeviceByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除心率带信息
   *
   * @param id 心率带ID
   * @return 结果
   */
  @Override
  public int deleteRunHeartRateDeviceById(Integer id) {
    return runHeartRateDeviceMapper.deleteRunHeartRateDeviceById(id);
  }

  @Override
  public RunHeartRateDevice selectRunHeartRateDeviceByDeviceId(String deviceId) {
    return runHeartRateDeviceMapper.selectRunHeartRateDeviceByDeviceId(deviceId);
  }

  @Override
  public RunHeartRateDevice selectRunHeartRateDeviceByUserId(Long userId) {
    return runHeartRateDeviceMapper.selectRunHeartRateDeviceByUserId(userId);
  }
}
