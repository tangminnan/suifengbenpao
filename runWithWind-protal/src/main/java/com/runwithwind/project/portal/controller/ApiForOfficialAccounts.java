package com.runwithwind.project.portal.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.file.FileUploadUtils;
import com.runwithwind.framework.config.runwithwindConfig;
import com.runwithwind.framework.shiro.service.PasswordService;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.project.portal.box.service.IRunBoxService;
import com.runwithwind.project.portal.courseDevice.service.IRunCourseDeviceService;
import com.runwithwind.project.portal.detail.service.IRunCourseDetailService;
import com.runwithwind.project.portal.device.domain.RunHeartRateDevice;
import com.runwithwind.project.portal.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.portal.pad.service.IRunPadService;
import com.runwithwind.project.portal.place.domain.RunPlace;
import com.runwithwind.project.portal.place.service.IRunPlaceService;
import com.runwithwind.project.portal.record.domain.RunCourseRecord;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import com.runwithwind.project.portal.update.service.IRunVersionUpdateService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "公众号接口", position = 3)
@RestController
@RequestMapping("/api/officialAccounts")
public class ApiForOfficialAccounts extends BaseController {
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

  @ApiOperation("我的课程")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  @GetMapping("/getMyClass")
  public R getClassStatus(Integer userId) {
    Map<String, Object> result = new HashMap<>();
    List<RunCourseRecord> runCourseRecordList = runCourseRecordService.getCourseByUserId(userId);
    result.put("data", runCourseRecordList);
    List<Map> sport = detailService.selectCalorieChart(null, userId);
    int sportTime = getXlTime(sport, 2);
    result.put("sportTime", sportTime);
    return R.data(result);
  }

  @ApiOperation("课程报告")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "courseId", value = "课程id", required = true, dataType = "int"),
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  })
  @GetMapping("/getClassReport")
  public R getClassReport(Integer courseId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    // 用户头像 名字
    result.put("heardImg", user.getHeadImg());
    result.put("username", user.getName());
    result.put("age", user.getAge());

    // 课程名
    RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(courseId);
    result.put("courseName", runCourseRecord.getName());

    // 运动总时长，心率平均，最高，最低，平均ck值，总卡路里,平均卡路里
    Map avgData = detailService.selectAvgData(courseId, userId);
    result.put("avg", avgData);

    // 上课总人数，
    int personNum = detailService.selectPersonNum(courseId);
    result.put("personNum", personNum);

    // 卡路里排名，ck值排名
    Map rank = detailService.selectRank(courseId, userId);
    result.put("rank", rank);

    // 心率折线图
    List<Map> xlMap = detailService.selectxlChart(courseId, userId);
    result.put("xlMap", xlMap);

    // 心率区间分配计算
    // 区间1 60-80
    Map<String, Integer> xltime = new HashMap<>();
    Map<String, String> xltimeZb = new HashMap<>();
    List<Map> xl1 = detailService.selectxlAllot(courseId, userId, 60, 80);
    int xltime1 = getXlTime(xl1, 1);
    xltime.put("xltime1", xltime1);
    // 区间2 80-100
    List<Map> xl2 = detailService.selectxlAllot(courseId, userId, 80, 100);
    int xltime2 = getXlTime(xl2, 1);
    xltime.put("xltime2", xltime2);
    // 区间3 100-120
    List<Map> xl3 = detailService.selectxlAllot(courseId, userId, 100, 120);
    int xltime3 = getXlTime(xl3, 1);
    xltime.put("xltime3", xltime3);
    // 区间4 120-140
    List<Map> xl4 = detailService.selectxlAllot(courseId, userId, 120, 140);
    int xltime4 = getXlTime(xl4, 1);
    xltime.put("xltime4", xltime4);
    // 区间5 >140-200
    List<Map> xl5 = detailService.selectxlAllot(courseId, userId, 140, 200);
    int xltime5 = getXlTime(xl5, 1);
    xltime.put("xltime5", xltime5);
    result.put("xltime", xltime);

    int xltimeNum = xltime1 + xltime2 + xltime3 + xltime4 + xltime5;
    xltimeZb.put("xltimeZb1", getBFB(xltime1, xltimeNum));
    xltimeZb.put("xltimeZb2", getBFB(xltime2, xltimeNum));
    xltimeZb.put("xltimeZb3", getBFB(xltime3, xltimeNum));
    xltimeZb.put("xltimeZb4", getBFB(xltime4, xltimeNum));
    xltimeZb.put("xltimeZb5", getBFB(xltime5, xltimeNum));
    result.put("xltimeZb", xltimeZb);
    // 卡路里折线图
    List<Map> calorieMap = detailService.selectCalorieChart(courseId, userId);
    result.put("calorieMap", calorieMap);

    List<Map> allTime = detailService.selectCalorieChart(null, userId);
    return R.data(result);
  }

  // 转换百分比
  String getBFB(int xltime, int xltimeNum) {
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(1);
    if (xltimeNum > 0) {
      return numberFormat.format((float) xltime / (float) xltimeNum * 100) + "%";
    } else {
      return 0.0 + "%";
    }
  }
  // 获取时长 1=长时间，2=短时间
  int getXlTime(List<Map> map, int type) {
    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = "";
    long second = 0;

    int count = 0;
    for (Map map1 : map) {
      if (!time.equals("")) {
        try {
          if (type == 1) {
            String sportTime = map1.get("sport_time").toString();
            Date nextTime = sdf.parse(sportTime);
            long nextDate = (long) nextTime.getTime();
            long lastDate = sdf.parse(time).getTime();
            long difference = (nextDate - lastDate);
            second = difference / (1000);
          } else {
            String sportTime = map1.get("sport_time").toString().replaceAll(" ", "");
            Date nextTime = sdf1.parse(sportTime);
            long nextDate = (long) nextTime.getTime();
            long lastDate = sdf1.parse(time).getTime();
            long difference = (nextDate - lastDate);
            second = difference / (1000);
          }
          if (second <= 5) {
            count += 5;
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      time = map1.get("sport_time").toString();
    }

    return count;
  }

  @ApiOperation("个人信息")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  @GetMapping("/getUser")
  public R getUser(Integer userId) {
    Map<String, Object> result = new HashMap<String, Object>();
    RunUser user = userService.selectRunUserById(userId);
    result.put("data", user);
    return R.data(result);
  }

  @ApiOperation("更新个人信息")
  @PostMapping(value = "/updateUser")
  public R updateUser(@ApiParam("RunUser") RunUser user) {
    System.out.println("=======user============" + user);
    if (user != null) {
      if (user.getImgFile() != null && user.getImgFile().getSize() > 0) {
        String filePath = runwithwindConfig.getUploadPath();
        System.out.println("=======地址=========" + runwithwindConfig.getUploadPath());
        // 上传并返回新文件名称
        String fileName = null;
        try {
          fileName = FileUploadUtils.upload(filePath, user.getImgFile());
          user.setHeadImg(SERVER_URL + fileName);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (userService.updateRunUser(user) > 0) {
        return R.ok();
      } else {
        return R.error();
      }
    } else {
      return R.error("保存失败");
    }
  }

  @ApiOperation("我的设备")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
  @GetMapping("/getMyDevice")
  public R getMyDevice(Long userId) {
    Map<String, Object> result = new HashMap<>();
    // 当前绑定
    RunHeartRateDevice runHeartRateDevice =
        runHeartRateDeviceService.selectRunHeartRateDeviceByUserId(userId);

    if (runHeartRateDevice != null) result.put("runHeartRateDevice", runHeartRateDevice);
    else result.put("runHeartRateDevice", new RunHeartRateDevice());
    // 历史绑定
    List<Map> historyDevice = runCourseDeviceService.selectHistoryByUserId(userId);
    result.put("historyDevice", historyDevice);
    return R.data(result);
  }

  @ApiOperation("我的场馆")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
  @GetMapping("/getMyPlace")
  public R getMyPlace(Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    System.out.println("====user==========" + user);
    if (user.getPlaceId() != null && user.getPlaceId() > 0) {
      System.out.println("=======id============" + user.getPlaceId());
      RunPlace place = runPlaceService.selectRunPlaceById(user.getPlaceId());
      if (place != null) {
        return R.data(place);
      } else {
        return R.error("未绑定");
      }

    } else {
      return R.error("未绑定");
    }
  }

  @ApiOperation("添加设备")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "设备号", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  })
  @GetMapping("/addDevice")
  public R addDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // 查询用户是否有绑定设备
      RunHeartRateDevice runHeartRateDevice1 = new RunHeartRateDevice();
      runHeartRateDevice1.setUserId(userId);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice1);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("您已绑定设备");
      } else {
        runHeartRateDevice.setUserId(userId);
        if (user != null && user.getPlaceId() != null && user.getPlaceId() > 0) {
          runHeartRateDevice.setPlaceId(user.getPlaceId());
        }
        runHeartRateDevice.setDeviceId(deviceId);
        runHeartRateDevice.setisbind(1);
        runHeartRateDevice.setStatus(2);
        if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
          return R.ok("添加并绑定成功");
        } else {
          return R.error("添加失败，请重新尝试");
        }
      }
    } else {

      RunHeartRateDevice runHeartRateDevice1 = new RunHeartRateDevice();
      runHeartRateDevice1.setUserId(userId);

      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice1);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("您已绑定设备");
      } else {

        if (user != null && user.getPlaceId() != null && user.getPlaceId() > 0) {
          runHeartRateDevice1.setPlaceId(user.getPlaceId());
        }
        runHeartRateDevice1.setDeviceId(deviceId);
        runHeartRateDevice1.setisbind(1);
        runHeartRateDevice1.setStatus(2);
        if (runHeartRateDeviceService.insertRunHeartRateDevice(runHeartRateDevice1) > 0) {
          return R.ok("添加并绑定成功");
        } else {
          return R.error("添加失败，请重新尝试");
        }
      }
    }
  }

  @ApiOperation("换绑设备")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "设备号", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  })
  @GetMapping("/changeDevice")
  public R changeDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // 查询用户是否有绑定设备
      runHeartRateDevice.setUserId(userId);
      runHeartRateDevice.setStatus(1);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("上课中，不允许操作");
      } else {
        // 查询设备是否已经被绑定
        RunHeartRateDevice device = new RunHeartRateDevice();
        device.setDeviceId(deviceId);
        device.setisbind(1);
        if (runHeartRateDeviceService.selectRunHeartRateDeviceList(device).size() > 0) {
          return R.error("该设备号已被绑定");
        } else {
          runHeartRateDevice.setDeviceId(deviceId);
          runHeartRateDevice.setisbind(1);
          runHeartRateDevice.setStatus(2);
          if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
            runHeartRateDeviceList.get(0).setisbind(2);
            runHeartRateDeviceList.get(0).setUserId(0);
            runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDeviceList.get(0));

            return R.ok("换绑成功");
          } else {
            return R.error("换绑失败，请重新尝试");
          }
        }
      }
    } else {
      return R.error("设备号不存在");
    }
  }

  @ApiOperation("解绑设备")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "设备号", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  })
  @GetMapping("/unbindDevice")
  public R unbindDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // 查询用户是否有绑定设备
      runHeartRateDevice.setUserId(userId);
      runHeartRateDevice.setStatus(1);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("上课中，不允许操作");
      } else {
        runHeartRateDevice.setisbind(2);
        runHeartRateDevice.setUserId(0);
        runHeartRateDevice.setStatus(2);
        if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
          return R.ok("解绑成功");
        } else {
          return R.error("解绑失败，请重试");
        }
      }
    } else {
      return R.error("设备号不存在");
    }
  }

  @ApiOperation("解绑场馆")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  @GetMapping("/unbindPlace")
  public R unbindPlace(Integer userId) {
    RunUser user = new RunUser();
    user.setId(userId);
    user.setPlaceId(0);
    if (userService.updateRunUser(user) > 0) {
      return R.ok("解绑成功");
    } else {
      return R.error("解绑失败，请重试");
    }
  }

  @ApiOperation("模糊查询场馆")
  @ApiImplicitParam(name = "placeName", value = "场馆名称", required = true, dataType = "String")
  @GetMapping("/selectPlaceList")
  public R selectPlaceLike(String placeName) {
    Map<String, Object> result = new HashMap<>();
    List<RunPlace> runPlaceList = runPlaceService.selectRunPlaceList(new RunPlace());
    if (runPlaceList.size() > 0) {
      return R.data(runPlaceList);
    } else {
      return R.error("没有场馆");
    }
  }

  @ApiOperation("添加场馆")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "placeId", value = "场馆id", required = true, dataType = "Integer"),
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer")
  })
  @GetMapping("/updatePlaceId")
  public R updatePlaceId(Integer placeId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    if (user != null) {
      if (user.getPlaceId() != null && user.getPlaceId() > 0) {
        return R.error("用户已绑定场馆");
      } else {
        user.setPlaceId(placeId);
        if (userService.updateRunUser(user) > 0) {
          return R.ok("绑定成功");
        } else {
          return R.error("绑定失败，请重试！");
        }
      }

    } else {
      return R.error("用户不存在");
    }
  }

  @ApiOperation("我的消息")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int")
  @GetMapping("/getMyMsg")
  public R getMyMsg(Long userId) {
    System.out.println("========userId=====" + userId);
    return R.ok();
  }

  @ApiOperation("验证码登录")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
    @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
  })
  @GetMapping("/loginByCode")
  public R loginByCode(String phone, String code) {
    Subject subject = SecurityUtils.getSubject();
    // Object object = subject.getSession().getAttribute("sys.login.check.code");
    Object object = phone + code;
    Map<String, Object> data = new HashMap<>();
    try {
      if (object != null) {
        String captcha = object.toString();
        if (captcha == null || "".equals(captcha)) {
          return R.error("验证码已失效，请重新点击发送验证码");
        } else {
          // session中存放的验证码是手机号+验证码
          if (!captcha.equalsIgnoreCase(phone + code)) {
            return R.error("手机验证码错误");
          } else {
            RunUser user = new RunUser();
            user.setPhone(phone);
            boolean flag = userService.exit(user);
            if (!flag) {
              // return R.error("该手机号码未注册");
              // 如果没注册就直接注册成功
              RunUser newUser = new RunUser();
              newUser.setPhone(phone);
              newUser.setPassword(passwordService.encryptPassword(phone, phone, user.getSalt()));
              newUser.setRegisterTime(DateUtils.getNowDate());
              newUser.setName("新用户");

              if (userService.insertRunUser(newUser) > 0) {
                UsernamePasswordToken token = new UsernamePasswordToken(phone, phone);
                subject.login(token);
                newUser.setLoginTime(new Date());
                userService.updateRunUser(newUser);
                data.put("data", newUser);
                return R.data(data);
              } else {
                return R.error("用户不存在且保存失败");
              }
            } else {
              RunUser udo = userService.selectRunUserByPhone(phone);
              String password = udo.getPassword();
              UsernamePasswordToken token = new UsernamePasswordToken(phone, phone);
              subject.login(token);
              udo.setLoginTime(new Date());
              userService.updateRunUser(udo);

              data.put("data", udo);
              return R.data(data);
            }
          }
        }
      } else {
        return R.error("手机号或验证码为空或已失效");
      }
    } catch (AuthenticationException e) {
      return R.error(e.toString());
    }
  }

  @ApiOperation("发送验证码")
  @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
  @GetMapping("/getCode")
  public R loginByCode(String phone) {
    Map<String, String> data = new HashMap<>();
    try {
      if (phone == null || "".equals(phone)) {
        return R.error("手机号为空");
      } else {
        DefaultProfile profile =
            DefaultProfile.getProfile(
                "default", "LTAI4FoEGySbeiHqHwnwHjPn", "kBtJkLFvUrqkFv6xDF7v3izA920tox");
        IAcsClient client = new DefaultAcsClient(profile);

        // Integer templateParam = (int) ((Math.random() * 9 + 1) * 100000);
        Integer templateParam = 000000;
        System.out.println("=========templateParam========" + templateParam);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);

        // request.putQueryParameter("SignName", "随峰奔跑");
        // request.putQueryParameter("TemplateCode", "SMS_210064468");

        request.putQueryParameter("SignName", "静途");
        request.putQueryParameter("TemplateCode", "SMS_189761686");

        request.putQueryParameter("TemplateParam", "{\"code\":\"" + templateParam + "\"}");

        CommonResponse response = client.getCommonResponse(request);

        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("sys.login.check.code", phone + templateParam);

        data.put("sessionId", subject.getSession().getId().toString());
        return R.data(data);
      }
    } catch (Exception e) {
      return R.error(e.toString());
    }
  }
}
