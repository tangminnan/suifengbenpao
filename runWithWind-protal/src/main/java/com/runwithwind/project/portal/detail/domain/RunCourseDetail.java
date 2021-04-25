package com.runwithwind.project.portal.detail.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程详情对象 run_course_detail
 *
 * @author jzf
 * @date 2020-12-22
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ApiModel(value = "RunCourseDetail", description = "设备实时数据")
public class RunCourseDetail {
  private static final long serialVersionUID = 1L;

  /** id */
  @ApiModelProperty(value = "id", hidden = true)
  private Long id;

  /** 课程id */
  @ApiModelProperty(value = "课程id", required = true)
  private Integer courseId;

  /** 用户id */
  @ApiModelProperty(value = "用户id")
  private Integer userId;

  /** 卡路里 */
  @ApiModelProperty(value = "卡路里")
  private Double calorie;

  /** 心率 */
  @ApiModelProperty(value = "心率")
  private Double heartRate;

  /** 心率 */
  @ApiModelProperty(value = "设备号", required = true)
  private String deviceId;

  /** 计步 */
  @ApiModelProperty(value = "计步")
  private Integer stepCount;

  /** CK值=卡路里消耗/体重，反映热量消耗对不同个体减脂的作用大小，CK值越大，减脂效果越好 */
  @ApiModelProperty(value = "ck值")
  private BigDecimal ck;

  /** 运动时间 */
  @ApiModelProperty(value = "运动时间", required = true)
  private String sportTime;

  @ApiModelProperty(value = "保存时间", hidden = true)
  private Date saveTime;

  private int start;

  private int end;

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public Integer getUserId() {
    return userId;
  }

  public Double getCalorie() {
    return calorie;
  }

  public void setCalorie(Double calorie) {
    this.calorie = calorie;
  }

  public Double getHeartRate() {
    return heartRate;
  }

  public void setHeartRate(Double heartRate) {
    this.heartRate = heartRate;
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

  public void setSportTime(String sportTime) {
    this.sportTime = sportTime;
  }

  public String getSportTime() {
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
    return "RunCourseDetail{"
        + "id="
        + id
        + ", courseId="
        + courseId
        + ", userId="
        + userId
        + ", calorie="
        + calorie
        + ", heartRate="
        + heartRate
        + ", deviceId='"
        + deviceId
        + '\''
        + ", stepCount="
        + stepCount
        + ", ck="
        + ck
        + ", sportTime="
        + sportTime
        + ", saveTime="
        + saveTime
        + '}';
  }
}
