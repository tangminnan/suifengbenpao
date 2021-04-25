package com.runwithwind.project.portal.device.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;

/**
 * 心率带对象 run_heart_rate_device
 *
 * @author jzf
 * @date 2020-12-22
 */
public class RunHeartRateDevice {
  private static final long serialVersionUID = 1L;

  /** id */
  private Integer id;

  /** 心率设备号 */
  @Excel(name = "设备号")
  private String deviceId;

  // 1=绑定，2=未绑定
  private int isbind;

  /** 设备名称 */
  @Excel(name = "设备名称")
  private String name;

  /** 用户id */
  @Excel(name = "用户ID")
  private Integer userId;

  private String username;

  // 1=上课中，2=未上课
  private int status;

  private Integer placeId;

  public Integer getPlaceId() {
    return placeId;
  }

  public void setPlaceId(Integer placeId) {
    this.placeId = placeId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setisbind(int isbind) {
    this.isbind = isbind;
  }

  public int getisbind() {
    return isbind;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getUserId() {
    return userId;
  }

  @Override
  public String toString() {
    return "RunHeartRateDevice{"
        + "id="
        + id
        + ", deviceId='"
        + deviceId
        + '\''
        + ", isbind="
        + isbind
        + ", name='"
        + name
        + '\''
        + ", userId="
        + userId
        + ", username='"
        + username
        + '\''
        + ", status="
        + status
        + '}';
  }
}
