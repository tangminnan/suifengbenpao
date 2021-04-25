package com.runwithwind.common.utils;

/** 微信关注用户的openId的调用凭证 */
public class AccessTokenAndNextOpenId {
  private String accessToken;
  private String nextOpenId;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getNextOpenId() {
    return nextOpenId;
  }

  public void setNextOpenId(String nextOpenId) {
    this.nextOpenId = nextOpenId;
  }

  @Override
  public String toString() {
    return "AccessTokenAndNextOpenId{"
        + "accessToken='"
        + accessToken
        + '\''
        + ", nextOpenId='"
        + nextOpenId
        + '\''
        + '}';
  }
}
