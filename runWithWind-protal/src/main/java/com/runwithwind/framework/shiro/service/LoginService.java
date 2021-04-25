package com.runwithwind.framework.shiro.service;

import com.runwithwind.common.constant.Constants;
import com.runwithwind.common.constant.ShiroConstants;
import com.runwithwind.common.constant.UserConstants;
import com.runwithwind.common.exception.user.CaptchaException;
import com.runwithwind.common.exception.user.UserNotExistsException;
import com.runwithwind.common.exception.user.UserPasswordNotMatchException;
import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.MessageUtils;
import com.runwithwind.common.utils.ServletUtils;
import com.runwithwind.framework.manager.AsyncManager;
import com.runwithwind.framework.manager.factory.AsyncFactory;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 登录校验方法
 *
 * @author runwithwind
 */
@Component
public class LoginService {
  @Autowired private PasswordService passwordService;

  @Autowired private IRunUserService userService;

  /** 登录 */
  public RunUser login(String username, String password) {
    // 验证码校验
    if (ShiroConstants.CAPTCHA_ERROR.equals(
        ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
      AsyncManager.me()
          .execute(
              AsyncFactory.recordLogininfor(
                  username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
      throw new CaptchaException();
    }
    // 用户名或密码为空 错误
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      System.out.println(
          "============username=========" + username + "===password=============" + password);
      AsyncManager.me()
          .execute(
              AsyncFactory.recordLogininfor(
                  username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
      throw new UserNotExistsException();
    }
    // 密码如果不在指定范围内 错误
    if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
        || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
      AsyncManager.me()
          .execute(
              AsyncFactory.recordLogininfor(
                  username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
      throw new UserPasswordNotMatchException();
    }

    // 用户名不在指定范围内 错误
    if (username.length() < UserConstants.USERNAME_MIN_LENGTH
        || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
      AsyncManager.me()
          .execute(
              AsyncFactory.recordLogininfor(
                  username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
      throw new UserPasswordNotMatchException();
    }

    // 查询用户信息
    RunUser user = userService.selectRunUserByPhone(username);

    /**
     * if (user == null && maybeMobilePhoneNumber(username)) { user =
     * userService.selectUserByPhoneNumber(username); }
     *
     * <p>if (user == null && maybeEmail(username)) { user =
     * userService.selectUserByEmail(username); }
     */
    if (user == null) {
      AsyncManager.me()
          .execute(
              AsyncFactory.recordLogininfor(
                  username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
      throw new UserNotExistsException();
    }

    /*if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
    {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
        throw new UserDeleteException();
    }

    if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
    {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark())));
        throw new UserBlockedException();
    }*/

    passwordService.validate(user, password);

    AsyncManager.me()
        .execute(
            AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
    recordLoginInfo(user);
    return user;
  }

  /**
   * private boolean maybeEmail(String username) { if
   * (!username.matches(UserConstants.EMAIL_PATTERN)) { return false; } return true; }
   *
   * <p>private boolean maybeMobilePhoneNumber(String username) { if
   * (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) { return false; } return true; }
   */

  /** 记录登录信息 */
  public void recordLoginInfo(RunUser user) {
    user.setLoginTime(DateUtils.getNowDate());
    userService.updateRunUser(user);
  }
}
