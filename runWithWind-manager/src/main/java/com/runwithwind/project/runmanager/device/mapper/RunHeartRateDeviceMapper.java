package com.runwithwind.project.runmanager.device.mapper;

import com.runwithwind.project.runmanager.device.domain.RunHeartRateDevice;

import java.util.List;

/**
 * 心率带Mapper接口
 *
 * @author jzf
 * @date 2020-12-22
 */
public interface RunHeartRateDeviceMapper {
  /**
   * 查询心率带
   *
   * @param id 心率带ID
   * @return 心率带
   */
  public RunHeartRateDevice selectRunHeartRateDeviceById(Integer id);

  /**
   * 查询心率带列表
   *
   * @param runHeartRateDevice 心率带
   * @return 心率带集合
   */
  public List<RunHeartRateDevice> selectRunHeartRateDeviceList(
      RunHeartRateDevice runHeartRateDevice);

  /**
   * 新增心率带
   *
   * @param runHeartRateDevice 心率带
   * @return 结果
   */
  public int insertRunHeartRateDevice(RunHeartRateDevice runHeartRateDevice);

  /**
   * 修改心率带
   *
   * @param runHeartRateDevice 心率带
   * @return 结果
   */
  public int updateRunHeartRateDevice(RunHeartRateDevice runHeartRateDevice);

  /**
   * 删除心率带
   *
   * @param id 心率带ID
   * @return 结果
   */
  public int deleteRunHeartRateDeviceById(Integer id);

  /**
   * 批量删除心率带
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRunHeartRateDeviceByIds(String[] ids);

  RunHeartRateDevice selectRunHeartRateDeviceByDeviceId(String deviceId);

  RunHeartRateDevice selectRunHeartRateDeviceByUserId(Integer id);
}
