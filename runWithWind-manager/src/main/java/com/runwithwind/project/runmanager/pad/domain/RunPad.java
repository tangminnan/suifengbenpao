package com.runwithwind.project.runmanager.pad.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * pad对象 run_pad
 *
 * @author jzf
 * @date 2020-12-22
 */
public class RunPad extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** id */
  @Excel(name = "id")
  private Long id;

  /** mac地址 */
  @Excel(name = "mac地址")
  private String mac;

  @Excel(name = "pad名称")
  private String name;

  private Integer placeId;

  private int isBind;

  private int isClass;

  private int delFlag;

  public int getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(int delFlag) {
    this.delFlag = delFlag;
  }

  public int getIsBind() {
    return isBind;
  }

  public void setIsBind(int isBind) {
    this.isBind = isBind;
  }

  public int getIsClass() {
    return isClass;
  }

  public void setIsClass(int isClass) {
    this.isClass = isClass;
  }

  public Integer getPlaceId() {
    return placeId;
  }

  public void setPlaceId(Integer placeId) {
    this.placeId = placeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getMac() {
    return mac;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("mac", getMac())
        .append("createTime", getCreateTime())
        .toString();
  }
}
