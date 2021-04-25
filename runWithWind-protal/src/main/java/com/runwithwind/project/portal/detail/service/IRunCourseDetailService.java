package com.runwithwind.project.portal.detail.service;

import com.runwithwind.project.portal.detail.domain.RunCourseDetail;

import java.util.List;
import java.util.Map;

/**
 * 课程详情Service接口
 *
 * @author jzf
 * @date 2020-12-22
 */
public interface IRunCourseDetailService {
  /**
   * 查询课程详情
   *
   * @param id 课程详情ID
   * @return 课程详情
   */
  public RunCourseDetail selectRunCourseDetailById(Long id);

  /**
   * 查询课程详情列表
   *
   * @param runCourseDetail 课程详情
   * @return 课程详情集合
   */
  public List<RunCourseDetail> selectRunCourseDetailList(RunCourseDetail runCourseDetail);

  /**
   * 新增课程详情
   *
   * @param runCourseDetail 课程详情
   * @return 结果
   */
  public int insertRunCourseDetail(RunCourseDetail runCourseDetail);

  /**
   * 修改课程详情
   *
   * @param runCourseDetail 课程详情
   * @return 结果
   */
  public int updateRunCourseDetail(RunCourseDetail runCourseDetail);

  /**
   * 批量删除课程详情
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRunCourseDetailByIds(String ids);

  /**
   * 删除课程详情信息
   *
   * @param id 课程详情ID
   * @return 结果
   */
  public int deleteRunCourseDetailById(Long id);

  int insertRunCourseDetailList(List<RunCourseDetail> runCourseDetailList);

  Map selectAvgData(Integer courseId, Integer userId);

  int selectPersonNum(Integer courseId);

  Map selectRank(Integer courseId, Integer userId);

  List<Map> selectxlChart(Integer courseId, Integer userId);

  List<Map> selectxlAllot(Integer courseId, Integer userId, int start, int end);

  List<Map> selectCalorieChart(Integer courseId, Integer userId);

  Map<String, Object> selectClassReport(Integer courseId, Integer userId);

  List<String> selectUserBycourseId(Integer courseId);

  Map selectClassTime(Integer courseId, Integer id);
}
