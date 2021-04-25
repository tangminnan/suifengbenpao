package com.runwithwind.project.portal.controller;

import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.StringUtils;
import com.runwithwind.framework.config.WechatOAConfig;
import com.runwithwind.framework.shiro.service.PasswordService;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.project.portal.box.service.IRunBoxService;
import com.runwithwind.project.portal.courseDevice.service.IRunCourseDeviceService;
import com.runwithwind.project.portal.detail.service.IRunCourseDetailService;
import com.runwithwind.project.portal.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.portal.pad.service.IRunPadService;
import com.runwithwind.project.portal.place.service.IRunPlaceService;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import com.runwithwind.project.portal.update.service.IRunVersionUpdateService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(description = "微信接口", position = 3)
@RestController
@RequestMapping("/api/wechat")
public class ApiForWeChat extends BaseController {
  @Autowired IRunCourseRecordService runCourseRecordService;
  @Autowired IRunPlaceService runPlaceService;
  @Autowired IRunPadService runPadService;
  @Autowired IRunHeartRateDeviceService runHeartRateDeviceService;
  @Autowired IRunCourseDeviceService runCourseDeviceService;
  @Autowired IRunUserService userService;
  @Autowired IRunBoxService boxService;
  @Autowired IRunCourseDetailService detailService;
  @Autowired private IRunVersionUpdateService versionUpdateService;
  @Autowired private PasswordService passwordService;

  private static final String SERVER_URL = "http://8.140.170.161:8084";

  @GetMapping("/index")
  @ResponseBody
  public R index(String code) {
    String openid = ""; // 暂定写死，之前是空字符串
    Map<String, String> map = new HashMap<>();
    if (StringUtils.isNotBlank(code)) {
      try {
        map = WechatOAConfig.getUserInfo(code);
        if (map.size() > 0) {
          return loginByOpenid(map);
        } else {
          return R.data(map);
        }
      } catch (Exception e) {
        return R.error(e.toString());
      }
    } else {
      return R.error("code获取失败");
    }
  }

  @GetMapping("/loginByOpenid")
  public R loginByOpenid(Map<String, String> map) {
    Subject subject = SecurityUtils.getSubject();
    RunUser user = new RunUser();
    user.setOpenid(map.get("openid"));
    boolean flag = userService.exit(user);
    if (!flag) {
      // return R.error("该手机号码未注册");
      // 如果没注册就直接注册成功
      RunUser newUser = new RunUser();
      newUser.setPassword(
          passwordService.encryptPassword(map.get("openid"), map.get("openid"), user.getSalt()));
      newUser.setRegisterTime(DateUtils.getNowDate());
      newUser.setName(filterEmoji(map.get("nickname")));
      newUser.setNickname(filterEmoji(map.get("nickname")));
      newUser.setHeadImg(map.get("headimgurl"));
      newUser.setOpenid(map.get("openid"));
      newUser.setType(1);
      newUser.setPhone(map.get("openid"));
      if (map.get("sex") == "1") newUser.setSex("男");
      else newUser.setSex("女");

      newUser.setRegisterTime(new Date());
      if (userService.insertRunUser(newUser) > 0) {
        UsernamePasswordToken token =
            new UsernamePasswordToken(map.get("openid"), map.get("openid"));
        subject.login(token);
        newUser.setLoginTime(new Date());
        userService.updateRunUser(newUser);
        return R.data(newUser);
      } else {
        return R.error("用户不存在且保存失败");
      }
    } else {
      RunUser udo = userService.selectRunUserByOpenId(map.get("openid"));
      String password = udo.getPassword();
      UsernamePasswordToken token = new UsernamePasswordToken(udo.getPhone(), map.get("openid"));
      subject.login(token);
      udo.setLoginTime(new Date());
      userService.updateRunUser(udo);
      return R.data(udo);
    }
  }

  /** 微信授权登录 */
  @GetMapping("/weChatLogin")
  public void wx_denglu(HttpServletRequest request, HttpServletResponse response) {
    String url = urlEncodeUTF8("http://wenjuan.biocareuk.com/weCahtCallBack"); // 回调页面的路径
    try {
      String uri =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
              + WechatOAConfig.APP_ID
              + "&redirect_uri="
              + url
              + "&response_type=code&scope=snsapi_base#wechat_redirect";
      response.sendRedirect(uri);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /** 回调 */
  @GetMapping("/weCahtCallBack")
  public Map<String, Object> getAccessToken(
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    String code = request.getParameter("code");
    String openid = null;
    if (StringUtils.isNotBlank(code)) {
      openid = WechatOAConfig.getUserInfo(code).get("openid");
      map.put("openid", openid);
    }
    return map;
  }

  /**
   * URL编码（utf-8）
   *
   * @param source
   * @return
   */
  public static String urlEncodeUTF8(String source) {
    String result = source;
    try {
      result = java.net.URLEncoder.encode(source, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  /** @author xiaojie 过滤表情的工具类 */
  public static String filterEmoji(String source) {
    if (source != null && source.length() > 0) {
      return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "*");
    } else {
      return source;
    }
  }
}
