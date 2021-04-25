package com.runwithwind.project.runmanager.pad.service.impl;

import com.runwithwind.common.constant.UserConstants;
import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.StringUtils;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.pad.domain.RunPad;
import com.runwithwind.project.runmanager.pad.mapper.RunPadMapper;
import com.runwithwind.project.runmanager.pad.service.IRunPadService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * padService业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunPadServiceImpl implements IRunPadService {
  @Autowired private RunPadMapper runPadMapper;
  @Autowired private IRunPlaceService runPlaceService;

  /**
   * 查询pad
   *
   * @param id padID
   * @return pad
   */
  @Override
  public RunPad selectRunPadById(Long id) {
    return runPadMapper.selectRunPadById(id);
  }

  /**
   * 查询pad列表
   *
   * @param runPad pad
   * @return pad
   */
  @Override
  public List<RunPad> selectRunPadList(RunPad runPad) {

    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runPad.setPlaceId(runPlace.getId());
    }
    return runPadMapper.selectRunPadList(runPad);
  }

  /**
   * 新增pad
   *
   * @param runPad pad
   * @return 结果
   */
  @Override
  public int insertRunPad(RunPad runPad) {
    User currentUser = ShiroUtils.getSysUser();
    RunPlace runPlace = new RunPlace();
    RunPad pad = new RunPad();
    pad.setIsBind(2);
    if (currentUser.getUserType().equals("02")) {
      runPlace = runPlaceService.selectRunPlaceByPhone(currentUser.getLoginName());
      runPad.setPlaceId(runPlace.getId());
      pad.setPlaceId(runPlace.getId());
      if (runPlace.getPadCount() <= selectRunPadList(pad).size()) {
        return 0;
      } else {
        runPad.setCreateTime(DateUtils.getNowDate());
        return runPadMapper.insertRunPad(runPad);
      }
    } else {
      runPad.setCreateTime(DateUtils.getNowDate());
      return runPadMapper.insertRunPad(runPad);
    }
  }

  /**
   * 修改pad
   *
   * @param runPad pad
   * @return 结果
   */
  @Override
  public int updateRunPad(RunPad runPad) {
    return runPadMapper.updateRunPad(runPad);
  }

  /**
   * 删除pad对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunPadByIds(String ids) {
    return runPadMapper.deleteRunPadByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除pad信息
   *
   * @param id padID
   * @return 结果
   */
  @Override
  public int deleteRunPadById(Long id) {
    return runPadMapper.deleteRunPadById(id);
  }

  @Override
  public RunPad selectRunPadByMac(String padMac) {
    return runPadMapper.selectRunPadByMac(padMac);
  }

  @Override
  public String checkPadMacUnique(RunPad pad) {
    Long id = StringUtils.isNull(pad.getId()) ? -1L : pad.getId();
    RunPad runPad = runPadMapper.selectRunPadByMac(pad.getMac());
    if (StringUtils.isNotNull(runPad) && runPad.getId().longValue() != id.longValue()) {
      return UserConstants.USER_PHONE_NOT_UNIQUE;
    }
    return UserConstants.USER_PHONE_UNIQUE;
  }
}
