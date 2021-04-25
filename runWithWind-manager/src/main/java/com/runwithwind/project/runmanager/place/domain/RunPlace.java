package com.runwithwind.project.runmanager.place.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 场地列对象 run_place
 *
 * @author jzf
 * @date 2021-01-18
 */
public class RunPlace extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** id */
  private Integer id;

  /** 场地名称 */
  @Excel(name = "${findComment}")
  private String placeName;

  /** 联系人手机号（登录后台使用） */
  @Excel(name = "${findComment}", readConverterExp = "登=录后台使用")
  private String phone;

  /** 负责人 */
  @Excel(name = "${findComment}")
  private String name;

  /** pad数量 */
  @Excel(name = "${findComment}")
  private Integer padCount;

  /** 心率设备数量 */
  @Excel(name = "${findComment}")
  private Integer deviceCount;

  /** 盒子数量 */
  @Excel(name = "${findComment}")
  private Integer boxCount;

  /** pad状态 1=上课中，2=未上课 */
  @Excel(name = "${findComment}")
  private Integer status;

  private String password;

  private String loginName;

  private int delFlag;

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public int getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(int delFlag) {
    this.delFlag = delFlag;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public String getPlaceName() {
    return placeName;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setPadCount(Integer padCount) {
    this.padCount = padCount;
  }

  public Integer getPadCount() {
    return padCount;
  }

  public void setDeviceCount(Integer deviceCount) {
    this.deviceCount = deviceCount;
  }

  public Integer getDeviceCount() {
    return deviceCount;
  }

  public void setBoxCount(Integer boxCount) {
    this.boxCount = boxCount;
  }

  public Integer getBoxCount() {
    return boxCount;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("placeName", getPlaceName())
        .append("phone", getPhone())
        .append("createTime", getCreateTime())
        .append("name", getName())
        .append("padCount", getPadCount())
        .append("deviceCount", getDeviceCount())
        .append("boxCount", getBoxCount())
        .append("status", getStatus())
        .toString();
  }
}
