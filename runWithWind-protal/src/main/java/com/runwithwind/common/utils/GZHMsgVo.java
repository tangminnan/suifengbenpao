package com.runwithwind.common.utils;

/** 公众号推送消息封装 */
public class GZHMsgVo {
  // 微信用户的openId
  private String touser;
  // 消息模板id
  private String templateId;
  private String remark; // 备注
  private String keyword1; //
  private String keyword2; //
  private String keyword3; //

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getKeyword1() {
    return keyword1;
  }

  public void setKeyword1(String keyword1) {
    this.keyword1 = keyword1;
  }

  public String getKeyword2() {
    return keyword2;
  }

  public void setKeyword2(String keyword2) {
    this.keyword2 = keyword2;
  }

  public String getKeyword3() {
    return keyword3;
  }

  public void setKeyword3(String keyword3) {
    this.keyword3 = keyword3;
  }

  public String getKeyword4() {
    return keyword4;
  }

  public void setKeyword4(String keyword4) {
    this.keyword4 = keyword4;
  }

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  private String keyword4; //
  private String first; //
  // 跳转的网页路径
  private String smUrl;
  // 备注字体颜色
  private String remakColor;
  // 标题字体颜色
  private String titleColor;

  public String getTouser() {
    return touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getSmUrl() {
    return smUrl;
  }

  public void setSmUrl(String smUrl) {
    this.smUrl = smUrl;
  }

  public String getRemakColor() {
    return remakColor;
  }

  public void setRemakColor(String remakColor) {
    this.remakColor = remakColor;
  }

  public String getTitleColor() {
    return titleColor;
  }

  public void setTitleColor(String titleColor) {
    this.titleColor = titleColor;
  }
}
