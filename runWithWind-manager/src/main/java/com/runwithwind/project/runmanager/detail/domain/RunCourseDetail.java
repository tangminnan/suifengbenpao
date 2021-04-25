package com.runwithwind.project.runmanager.detail.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程详情对象 run_course_detail
 *
 * @author jzf
 * @date 2020-12-22
 */
public class RunCourseDetail extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** id */
  private Long id;

  /** 课程id */
  @Excel(name = "课程id")
  private Integer courseId;

  /** 用户id */
  @Excel(name = "用户id")
  private Integer userId;

  /** 卡路里 */
  @Excel(name = "卡路里")
  private BigDecimal calorie;

  /** 心率 */
  @Excel(name = "心率")
  private BigDecimal heartRate;

  /** 计步 */
  @Excel(name = "计步")
  private Integer stepCount;

  /** CK值=卡路里消耗/体重，反映热量消耗对不同个体减脂的作用大小，CK值越大，减脂效果越好 */
  @Excel(name = "ck值")
  private BigDecimal ck;

  /** 运动时间 */
  @Excel(name = "运动时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
  private Date sportTime;

  private Date saveTime;

  private String username;

  private String deviceId;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }

  public Integer getCourseId() {
    return courseId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setCalorie(BigDecimal calorie) {
    this.calorie = calorie;
  }

  public BigDecimal getCalorie() {
    return calorie;
  }

  public void setHeartRate(BigDecimal heartRate) {
    this.heartRate = heartRate;
  }

  public BigDecimal getHeartRate() {
    return heartRate;
  }

  public void setStepCount(Integer stepCount) {
    this.stepCount = stepCount;
  }

  public Integer getStepCount() {
    return stepCount;
  }

  public void setCk(BigDecimal ck) {
    this.ck = ck;
  }

  public BigDecimal getCk() {
    return ck;
  }

  public void setSportTime(Date sportTime) {
    this.sportTime = sportTime;
  }

  public Date getSportTime() {
    return sportTime;
  }

  public void setSaveTime(Date saveTime) {
    this.saveTime = saveTime;
  }

  public Date getSaveTime() {
    return saveTime;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("courseId", getCourseId())
        .append("userId", getUserId())
        .append("calorie", getCalorie())
        .append("heartRate", getHeartRate())
        .append("stepCount", getStepCount())
        .append("ck", getCk())
        .append("sportTime", getSportTime())
        .append("saveTime", getSaveTime())
        .toString();
  }
}
