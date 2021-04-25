package com.runwithwind.project.runmanager.user.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户列对象 run_user
 *
 * @author jzf
 * @date 2020-12-22
 */
public class RunUser extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** id */
  private Integer id;

  /** 姓名 */
  @Excel(name = "姓名")
  private String name;

  /** $column.columnComment */
  private String headImg;

  /** 昵称 */
  @Excel(name = "昵称")
  private String nickname;

  /** 微信标识 */
  private String openid;

  /** 手机号 */
  private String phone;

  /** 密码 */
  private String password;

  /** 性别 1=女，2=男，3=未知 */
  @Excel(name = "性别")
  private String sex;

  /** 年龄 */
  @Excel(name = "年龄")
  private Integer age;

  /** $column.columnComment */
  @Excel(name = "体重")
  private BigDecimal weight;

  /** 1=学员，2=教练 */
  private Integer type;

  /** $column.columnComment */
  @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd")
  private Date registerTime;

  /** $column.columnComment */
  @Excel(name = "登录时间", width = 30, dateFormat = "yyyy-MM-dd")
  private Date loginTime;

  private Integer placeId;

  @Excel(name = "设备号")
  private String device;

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public Integer getPlaceId() {
    return placeId;
  }

  public void setPlaceId(Integer placeId) {
    this.placeId = placeId;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }

  public String getHeadImg() {
    return headImg;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getOpenid() {
    return openid;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getSex() {
    return sex;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getAge() {
    return age;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public BigDecimal getWeight() {
    return weight;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getType() {
    return type;
  }

  public void setRegisterTime(Date registerTime) {
    this.registerTime = registerTime;
  }

  public Date getRegisterTime() {
    return registerTime;
  }

  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }

  public Date getLoginTime() {
    return loginTime;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("name", getName())
        .append("headImg", getHeadImg())
        .append("nickname", getNickname())
        .append("openid", getOpenid())
        .append("phone", getPhone())
        .append("password", getPassword())
        .append("sex", getSex())
        .append("age", getAge())
        .append("weight", getWeight())
        .append("type", getType())
        .append("registerTime", getRegisterTime())
        .append("loginTime", getLoginTime())
        .toString();
  }
}
