package com.runwithwind.project.portal.detail.service.impl;

import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.detail.domain.RunCourseDetail;
import com.runwithwind.project.portal.detail.mapper.RunCourseDetailMapper;
import com.runwithwind.project.portal.detail.service.IRunCourseDetailService;
import com.runwithwind.project.portal.place.domain.RunPlace;
import com.runwithwind.project.portal.place.service.IRunPlaceService;
import com.runwithwind.project.portal.record.domain.RunCourseRecord;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程详情Service业务层处理
 *
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunCourseDetailServiceImpl implements IRunCourseDetailService {
  @Autowired private RunCourseDetailMapper runCourseDetailMapper;
  @Autowired IRunUserService userService;
  @Autowired IRunCourseRecordService runCourseRecordService;
  @Autowired IRunPlaceService runPlaceService;

  /**
   * 查询课程详情
   *
   * @param id 课程详情ID
   * @return 课程详情
   */
  @Override
  public RunCourseDetail selectRunCourseDetailById(Long id) {
    return runCourseDetailMapper.selectRunCourseDetailById(id);
  }

  /**
   * 查询课程详情列表
   *
   * @param runCourseDetail 课程详情
   * @return 课程详情
   */
  @Override
  public List<RunCourseDetail> selectRunCourseDetailList(RunCourseDetail runCourseDetail) {
    return runCourseDetailMapper.selectRunCourseDetailList(runCourseDetail);
  }

  /**
   * 新增课程详情
   *
   * @param runCourseDetail 课程详情
   * @return 结果
   */
  @Override
  public int insertRunCourseDetail(RunCourseDetail runCourseDetail) {
    return runCourseDetailMapper.insertRunCourseDetail(runCourseDetail);
  }

  /**
   * 修改课程详情
   *
   * @param runCourseDetail 课程详情
   * @return 结果
   */
  @Override
  public int updateRunCourseDetail(RunCourseDetail runCourseDetail) {
    return runCourseDetailMapper.updateRunCourseDetail(runCourseDetail);
  }

  /**
   * 删除课程详情对象
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseDetailByIds(String ids) {
    return runCourseDetailMapper.deleteRunCourseDetailByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除课程详情信息
   *
   * @param id 课程详情ID
   * @return 结果
   */
  @Override
  public int deleteRunCourseDetailById(Long id) {
    return runCourseDetailMapper.deleteRunCourseDetailById(id);
  }

  @Override
  public int insertRunCourseDetailList(List<RunCourseDetail> runCourseDetailList) {
    return runCourseDetailMapper.insertRunCourseDetailList(runCourseDetailList);
  }

  @Override
  public Map selectAvgData(Integer courseId, Integer userId) {
    return runCourseDetailMapper.selectAvgData(courseId, userId);
  }

  @Override
  public int selectPersonNum(Integer courseId) {
    return runCourseDetailMapper.selectPersonNum(courseId);
  }

  @Override
  public Map selectRank(Integer courseId, Integer userId) {
    return runCourseDetailMapper.selectRank(courseId, userId);
  }

  @Override
  public List<Map> selectxlChart(Integer courseId, Integer userId) {
    return runCourseDetailMapper.selectxlChart(courseId, userId);
  }

  @Override
  public List<Map> selectxlAllot(Integer courseId, Integer userId, int start, int end) {
    return runCourseDetailMapper.selectxlAllot(courseId, userId, start, end);
  }

  @Override
  public List<Map> selectCalorieChart(Integer courseId, Integer userId) {
    return runCourseDetailMapper.selectCalorieChart(courseId, userId);
  }

  @Override
  public Map<String, Object> selectClassReport(Integer courseId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    // 用户头像 名字
    result.put("heardImg", user.getHeadImg());
    result.put("username", user.getName());

    // 课程名
    RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(courseId);
    result.put("courseName", runCourseRecord.getName());

    RunPlace runPlace = runPlaceService.selectRunPlaceById(runCourseRecord.getId());
    if (runPlace != null) {
      result.put("placeName", runPlace.getPlaceName());
    } else {
      result.put("placeName", "暂无");
    }

    // 运动总时长，心率平均，最高，最低，平均ck值，总卡路里,平均卡路里
    Map avgData = selectAvgData(courseId, userId);
    result.put("avg", avgData);

    // 上课总人数，
    int personNum = selectPersonNum(courseId);
    result.put("personNum", personNum);

    // 卡路里排名，ck值排名
    Map rank = selectRank(courseId, userId);
    result.put("rank", rank);

    return result;
  }

  @Override
  public List<String> selectUserBycourseId(Integer courseId) {
    return runCourseDetailMapper.selectUserBycourseId(courseId);
  }

  @Override
  public Map selectClassTime(Integer courseId, Integer id) {
    return runCourseDetailMapper.selectClassTime(courseId, id);
  }

  // 转换百分比
  String getBFB(int xltime, int xltimeNum) {
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(1);
    if (xltimeNum > 0) {
      return numberFormat.format((float) xltime / (float) xltimeNum * 100) + "%";
    } else {
      return 0.0 + "%";
    }
  }

  // 获取时长 1=长时间，2=短时间
  int getXlTime(List<Map> map, int type) {
    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = "";
    long second = 0;

    int count = 0;
    for (Map map1 : map) {
      if (!time.equals("")) {
        try {
          if (type == 1) {
            String sportTime = map1.get("sport_time").toString();
            Date nextTime = sdf.parse(sportTime);
            long nextDate = (long) nextTime.getTime();
            long lastDate = sdf.parse(time).getTime();
            long difference = (nextDate - lastDate);
            second = difference / (1000);
          } else {
            String sportTime = map1.get("sport_time").toString().replaceAll(" ", "");
            Date nextTime = sdf1.parse(sportTime);
            long nextDate = (long) nextTime.getTime();
            long lastDate = sdf1.parse(time).getTime();
            long difference = (nextDate - lastDate);
            second = difference / (1000);
          }
          if (second <= 5) {
            count += 5;
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      time = map1.get("sport_time").toString();
    }

    return count;
  }
}
