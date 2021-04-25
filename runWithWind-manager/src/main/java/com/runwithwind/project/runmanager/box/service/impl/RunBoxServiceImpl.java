package com.runwithwind.project.runmanager.box.service.impl;

import com.runwithwind.common.constant.UserConstants;
import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.StringUtils;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.box.domain.RunBox;
import com.runwithwind.project.runmanager.box.mapper.RunBoxMapper;
import com.runwithwind.project.runmanager.box.service.IRunBoxService;
import com.runwithwind.project.runmanager.pad.domain.RunPad;
import com.runwithwind.project.runmanager.pad.service.IRunPadService;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 盒子Service业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunBoxServiceImpl implements IRunBoxService {
  @Autowired private RunBoxMapper runBoxMapper;

  @Autowired private IRunPlaceService runPlaceService;

  @Autowired private IRunPadService runPadService;
  /**
   * 查询盒子
   *
   * @param id 盒子ID
   * @return 盒子
   */
  @Override
  public RunBox selectRunBoxById(Long id) {
    return runBoxMapper.selectRunBoxById(id);
  }

  /**
   * 查询盒子列表
   *
   * @param runBox 盒子
   * @return 盒子
   */
  @Override
  public List<RunBox> selectRunBoxList(RunBox runBox) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runBox.setBindPlace(runPlace.getId());
    }
    return runBoxMapper.selectRunBoxList(runBox);
  }

  /**
   * 新增盒子
   *
   * @param runBox 盒子
   * @return 结果
   */
  @Override
  public int insertRunBox(RunBox runBox) {
    User currentUser = ShiroUtils.getSysUser();
    RunPlace runPlace = new RunPlace();
    RunBox box = new RunBox();
    if (currentUser.getUserType().equals("02")) {
      runPlace = runPlaceService.selectRunPlaceByPhone(currentUser.getLoginName());
      runBox.setBindPlace(runPlace.getId());
      box.setBindPlace(runPlace.getId());

      if (runPlace.getBoxCount() <= selectRunBoxList(box).size()) {
        return 0;
      } else {
        if (runBox.getBindPad() != null) {
          RunPad runPad = runPadService.selectRunPadById(runBox.getBindPad());
          runPad.setIsBind(1);
          runPadService.updateRunPad(runPad);
        }
        runBox.setCreateTime(DateUtils.getNowDate());
        return runBoxMapper.insertRunBox(runBox);
      }
    } else {
      if (runBox.getBindPad() != null) {
        RunPad runPad = runPadService.selectRunPadById(runBox.getBindPad());
        runPad.setIsBind(1);
        runPadService.updateRunPad(runPad);
      }

      runBox.setCreateTime(DateUtils.getNowDate());
      return runBoxMapper.insertRunBox(runBox);
    }
  }

  /**
   * 修改盒子
   *
   * @param runBox 盒子
   * @return 结果
   */
  @Override
  public int updateRunBox(RunBox runBox) {
    if (runBox != null) {
      if (runBox.getBindPad() != null && runBox.getBindPad() > 0) {
        RunPad runPad = runPadService.selectRunPadById(runBox.getBindPad());
        runPad.setIsBind(1);
        runPadService.updateRunPad(runPad);
      } else {
        RunBox box = runBoxMapper.selectRunBoxById(runBox.getId());
        if (box.getBindPad() != null) {
          RunPad runPad = runPadService.selectRunPadById(box.getBindPad());
          runPad.setIsBind(2);
          runPadService.updateRunPad(runPad);
        }
      }
    }
    return runBoxMapper.updateRunBox(runBox);
  }

  /**
   * 删除盒子对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunBoxByIds(String ids) {
    return runBoxMapper.deleteRunBoxByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除盒子信息
   *
   * @param id 盒子ID
   * @return 结果
   */
  @Override
  public int deleteRunBoxById(Long id) {
    return runBoxMapper.deleteRunBoxById(id);
  }

  @Override
  public String checkBoxMacUnique(RunBox runBox) {
    Long id = StringUtils.isNull(runBox.getId()) ? -1L : runBox.getId();
    RunBox box = runBoxMapper.checkBoxMacUnique(runBox.getBoxMac());
    if (StringUtils.isNotNull(box) && box.getId().longValue() != id.longValue()) {
      return UserConstants.USER_PHONE_NOT_UNIQUE;
    }
    return UserConstants.USER_PHONE_UNIQUE;
  }
}
