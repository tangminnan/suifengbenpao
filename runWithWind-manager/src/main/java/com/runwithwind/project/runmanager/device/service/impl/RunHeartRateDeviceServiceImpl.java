package com.runwithwind.project.runmanager.device.service.impl;

import com.runwithwind.common.constant.UserConstants;
import com.runwithwind.common.utils.StringUtils;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.device.domain.RunHeartRateDevice;
import com.runwithwind.project.runmanager.device.mapper.RunHeartRateDeviceMapper;
import com.runwithwind.project.runmanager.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.system.user.domain.User;
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

  @Autowired private IRunPlaceService runPlaceService;

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
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runHeartRateDevice.setPlaceId(runPlace.getId());
    }
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
    RunPlace runPlace = new RunPlace();
    RunHeartRateDevice heartRateDevice = new RunHeartRateDevice();
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      runPlace = runPlaceService.selectRunPlaceByPhone(currentUser.getLoginName());
      runHeartRateDevice.setPlaceId(runPlace.getId());
      heartRateDevice.setPlaceId(runPlace.getId());
      if (runPlace.getDeviceCount() <= selectRunHeartRateDeviceList(heartRateDevice).size()) {
        return 0;
      } else {
        return runHeartRateDeviceMapper.insertRunHeartRateDevice(runHeartRateDevice);
      }
    } else {
      return runHeartRateDeviceMapper.insertRunHeartRateDevice(runHeartRateDevice);
    }
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
  public String checkDeviceIdUnique(RunHeartRateDevice runHeartRateDevice) {
    Long id = StringUtils.isNull(runHeartRateDevice.getId()) ? -1L : runHeartRateDevice.getId();
    RunHeartRateDevice heartRateDevice =
        runHeartRateDeviceMapper.selectRunHeartRateDeviceByDeviceId(
            runHeartRateDevice.getDeviceId());
    if (StringUtils.isNotNull(heartRateDevice)
        && heartRateDevice.getId().longValue() != id.longValue()) {
      return UserConstants.USER_PHONE_NOT_UNIQUE;
    }
    return UserConstants.USER_PHONE_UNIQUE;
  }

  @Override
  public RunHeartRateDevice selectRunHeartRateDeviceByUserId(Integer id) {
    return runHeartRateDeviceMapper.selectRunHeartRateDeviceByUserId(id);
  }
}
