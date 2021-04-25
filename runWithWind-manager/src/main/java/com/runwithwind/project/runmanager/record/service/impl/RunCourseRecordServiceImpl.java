package com.runwithwind.project.runmanager.record.service.impl;

import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.runmanager.place.domain.RunPlace;
import com.runwithwind.project.runmanager.place.service.IRunPlaceService;
import com.runwithwind.project.runmanager.record.domain.RunCourseRecord;
import com.runwithwind.project.runmanager.record.mapper.RunCourseRecordMapper;
import com.runwithwind.project.runmanager.record.service.IRunCourseRecordService;
import com.runwithwind.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程记录Service业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunCourseRecordServiceImpl implements IRunCourseRecordService {
  @Autowired private RunCourseRecordMapper runCourseRecordMapper;
  @Autowired private IRunPlaceService runPlaceService;

  /**
   * 查询课程记录
   *
   * @param id 课程记录ID
   * @return 课程记录
   */
  @Override
  public RunCourseRecord selectRunCourseRecordById(Integer id) {
    return runCourseRecordMapper.selectRunCourseRecordById(id);
  }

  /**
   * 查询课程记录列表
   *
   * @param runCourseRecord 课程记录
   * @return 课程记录
   */
  @Override
  public List<RunCourseRecord> selectRunCourseRecordList(RunCourseRecord runCourseRecord) {
    User currentUser = ShiroUtils.getSysUser();
    if (currentUser.getUserType().equals("02")) {
      RunPlace runPlace = runPlaceService.selectRunPlaceByLoginName(currentUser.getLoginName());
      runCourseRecord.setPlaceId(runPlace.getId());
    }
    return runCourseRecordMapper.selectRunCourseRecordList(runCourseRecord);
  }

  /**
   * 新增课程记录
   *
   * @param runCourseRecord 课程记录
   * @return 结果
   */
  @Override
  public int insertRunCourseRecord(RunCourseRecord runCourseRecord) {
    return runCourseRecordMapper.insertRunCourseRecord(runCourseRecord);
  }

  /**
   * 修改课程记录
   *
   * @param runCourseRecord 课程记录
   * @return 结果
   */
  @Override
  public int updateRunCourseRecord(RunCourseRecord runCourseRecord) {
    return runCourseRecordMapper.updateRunCourseRecord(runCourseRecord);
  }

  /**
   * 删除课程记录对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseRecordByIds(String ids) {
    return runCourseRecordMapper.deleteRunCourseRecordByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除课程记录信息
   *
   * @param id 课程记录ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseRecordById(Integer id) {
    return runCourseRecordMapper.deleteRunCourseRecordById(id);
  }
}
