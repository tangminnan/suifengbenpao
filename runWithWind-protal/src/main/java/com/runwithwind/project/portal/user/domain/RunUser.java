package com.runwithwind.project.portal.user.domain;

import com.runwithwind.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 用户列对象 run_user
 *
 * @author jzf
 * @date 2020-12-22
 */
@ApiModel(value = "RunUser", description = "用户")
public class RunUser extends BaseEntity {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id", required = true)
  private Integer id;

  @ApiModelProperty(value = "姓名")
  private String name;

  @ApiModelProperty(value = "头像")
  private String headImg;

  @ApiModelProperty(value = "昵称")
  private String nickname;

  @ApiModelProperty(value = "微信id", hidden = true)
  private String openid;

  @ApiModelProperty(value = "手机号")
  private String phone;

  @ApiModelProperty(value = "密码", hidden = true)
  private String password;

  @ApiModelProperty(value = "性别")
  private String sex;

  @ApiModelProperty(value = "年龄")
  private Integer age;

  @ApiModelProperty(value = "体重")
  private Double weight;

  @ApiModelProperty(value = "类型 1=学员，2=教练", hidden = true)
  private Integer type;

  @ApiModelProperty(value = "设备号", hidden = true)
  private String deviceId;

  @ApiModelProperty(value = "注册时间", hidden = true)
  private Date registerTime;

  @ApiModelProperty(value = "登陆时间", hidden = true)
  private Date loginTime;

  @ApiModelProperty(value = "头像文件")
  private MultipartFile imgFile;

  private Integer placeId;

  public MultipartFile getImgFile() {
    return imgFile;
  }

  public void setImgFile(MultipartFile imgFile) {
    this.imgFile = imgFile;
  }

  /** 盐加密 */
  private String salt;

  public Integer getPlaceId() {
    return placeId;
  }

  public void setPlaceId(Integer placeId) {
    this.placeId = placeId;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
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

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Double getWeight() {
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
    return "RunUser{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", headImg='"
        + headImg
        + '\''
        + ", nickname='"
        + nickname
        + '\''
        + ", openid='"
        + openid
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", password='"
        + password
        + '\''
        + ", sex='"
        + sex
        + '\''
        + ", age="
        + age
        + ", weight="
        + weight
        + ", type="
        + type
        + ", deviceId='"
        + deviceId
        + '\''
        + ", registerTime="
        + registerTime
        + ", loginTime="
        + loginTime
        + ", imgFile="
        + imgFile
        + ", placeId="
        + placeId
        + ", salt='"
        + salt
        + '\''
        + '}';
  }
}
