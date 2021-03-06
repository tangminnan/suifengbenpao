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

@Api(description = "???????????????", position = 3)
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

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
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

  @ApiOperation("????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "courseId", value = "??????id", required = true, dataType = "int"),
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  })
  @GetMapping("/getClassReport")
  public R getClassReport(Integer courseId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    // ???????????? ??????
    result.put("heardImg", user.getHeadImg());
    result.put("username", user.getName());
    result.put("age", user.getAge());

    // ?????????
    RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(courseId);
    result.put("courseName", runCourseRecord.getName());

    // ?????????????????????????????????????????????????????????ck??????????????????,???????????????
    Map avgData = detailService.selectAvgData(courseId, userId);
    result.put("avg", avgData);

    // ??????????????????
    int personNum = detailService.selectPersonNum(courseId);
    result.put("personNum", personNum);

    // ??????????????????ck?????????
    Map rank = detailService.selectRank(courseId, userId);
    result.put("rank", rank);

    // ???????????????
    List<Map> xlMap = detailService.selectxlChart(courseId, userId);
    result.put("xlMap", xlMap);

    // ????????????????????????
    // ??????1 60-80
    Map<String, Integer> xltime = new HashMap<>();
    Map<String, String> xltimeZb = new HashMap<>();
    List<Map> xl1 = detailService.selectxlAllot(courseId, userId, 60, 80);
    int xltime1 = getXlTime(xl1, 1);
    xltime.put("xltime1", xltime1);
    // ??????2 80-100
    List<Map> xl2 = detailService.selectxlAllot(courseId, userId, 80, 100);
    int xltime2 = getXlTime(xl2, 1);
    xltime.put("xltime2", xltime2);
    // ??????3 100-120
    List<Map> xl3 = detailService.selectxlAllot(courseId, userId, 100, 120);
    int xltime3 = getXlTime(xl3, 1);
    xltime.put("xltime3", xltime3);
    // ??????4 120-140
    List<Map> xl4 = detailService.selectxlAllot(courseId, userId, 120, 140);
    int xltime4 = getXlTime(xl4, 1);
    xltime.put("xltime4", xltime4);
    // ??????5 >140-200
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
    // ??????????????????
    List<Map> calorieMap = detailService.selectCalorieChart(courseId, userId);
    result.put("calorieMap", calorieMap);

    List<Map> allTime = detailService.selectCalorieChart(null, userId);
    return R.data(result);
  }

  // ???????????????
  String getBFB(int xltime, int xltimeNum) {
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(1);
    if (xltimeNum > 0) {
      return numberFormat.format((float) xltime / (float) xltimeNum * 100) + "%";
    } else {
      return 0.0 + "%";
    }
  }
  // ???????????? 1=????????????2=?????????
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

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  @GetMapping("/getUser")
  public R getUser(Integer userId) {
    Map<String, Object> result = new HashMap<String, Object>();
    RunUser user = userService.selectRunUserById(userId);
    result.put("data", user);
    return R.data(result);
  }

  @ApiOperation("??????????????????")
  @PostMapping(value = "/updateUser")
  public R updateUser(@ApiParam("RunUser") RunUser user) {
    System.out.println("=======user============" + user);
    if (user != null) {
      if (user.getImgFile() != null && user.getImgFile().getSize() > 0) {
        String filePath = runwithwindConfig.getUploadPath();
        System.out.println("=======??????=========" + runwithwindConfig.getUploadPath());
        // ??????????????????????????????
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
      return R.error("????????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "String")
  @GetMapping("/getMyDevice")
  public R getMyDevice(Long userId) {
    Map<String, Object> result = new HashMap<>();
    // ????????????
    RunHeartRateDevice runHeartRateDevice =
        runHeartRateDeviceService.selectRunHeartRateDeviceByUserId(userId);

    if (runHeartRateDevice != null) result.put("runHeartRateDevice", runHeartRateDevice);
    else result.put("runHeartRateDevice", new RunHeartRateDevice());
    // ????????????
    List<Map> historyDevice = runCourseDeviceService.selectHistoryByUserId(userId);
    result.put("historyDevice", historyDevice);
    return R.data(result);
  }

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "String")
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
        return R.error("?????????");
      }

    } else {
      return R.error("?????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "?????????", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  })
  @GetMapping("/addDevice")
  public R addDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // ?????????????????????????????????
      RunHeartRateDevice runHeartRateDevice1 = new RunHeartRateDevice();
      runHeartRateDevice1.setUserId(userId);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice1);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("??????????????????");
      } else {
        runHeartRateDevice.setUserId(userId);
        if (user != null && user.getPlaceId() != null && user.getPlaceId() > 0) {
          runHeartRateDevice.setPlaceId(user.getPlaceId());
        }
        runHeartRateDevice.setDeviceId(deviceId);
        runHeartRateDevice.setisbind(1);
        runHeartRateDevice.setStatus(2);
        if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
          return R.ok("?????????????????????");
        } else {
          return R.error("??????????????????????????????");
        }
      }
    } else {

      RunHeartRateDevice runHeartRateDevice1 = new RunHeartRateDevice();
      runHeartRateDevice1.setUserId(userId);

      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice1);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("??????????????????");
      } else {

        if (user != null && user.getPlaceId() != null && user.getPlaceId() > 0) {
          runHeartRateDevice1.setPlaceId(user.getPlaceId());
        }
        runHeartRateDevice1.setDeviceId(deviceId);
        runHeartRateDevice1.setisbind(1);
        runHeartRateDevice1.setStatus(2);
        if (runHeartRateDeviceService.insertRunHeartRateDevice(runHeartRateDevice1) > 0) {
          return R.ok("?????????????????????");
        } else {
          return R.error("??????????????????????????????");
        }
      }
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "?????????", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  })
  @GetMapping("/changeDevice")
  public R changeDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // ?????????????????????????????????
      runHeartRateDevice.setUserId(userId);
      runHeartRateDevice.setStatus(1);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("???????????????????????????");
      } else {
        // ?????????????????????????????????
        RunHeartRateDevice device = new RunHeartRateDevice();
        device.setDeviceId(deviceId);
        device.setisbind(1);
        if (runHeartRateDeviceService.selectRunHeartRateDeviceList(device).size() > 0) {
          return R.error("????????????????????????");
        } else {
          runHeartRateDevice.setDeviceId(deviceId);
          runHeartRateDevice.setisbind(1);
          runHeartRateDevice.setStatus(2);
          if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
            runHeartRateDeviceList.get(0).setisbind(2);
            runHeartRateDeviceList.get(0).setUserId(0);
            runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDeviceList.get(0));

            return R.ok("????????????");
          } else {
            return R.error("??????????????????????????????");
          }
        }
      }
    } else {
      return R.error("??????????????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "deviceId", value = "?????????", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  })
  @GetMapping("/unbindDevice")
  public R unbindDevice(String deviceId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunHeartRateDevice runHeartRateDevice = new RunHeartRateDevice();
    runHeartRateDevice = runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
    if (runHeartRateDevice != null) {
      // ?????????????????????????????????
      runHeartRateDevice.setUserId(userId);
      runHeartRateDevice.setStatus(1);
      List<RunHeartRateDevice> runHeartRateDeviceList =
          runHeartRateDeviceService.selectRunHeartRateDeviceList(runHeartRateDevice);
      if (runHeartRateDeviceList != null && runHeartRateDeviceList.size() > 0) {
        return R.error("???????????????????????????");
      } else {
        runHeartRateDevice.setisbind(2);
        runHeartRateDevice.setUserId(0);
        runHeartRateDevice.setStatus(2);
        if (runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice) > 0) {
          return R.ok("????????????");
        } else {
          return R.error("????????????????????????");
        }
      }
    } else {
      return R.error("??????????????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  @GetMapping("/unbindPlace")
  public R unbindPlace(Integer userId) {
    RunUser user = new RunUser();
    user.setId(userId);
    user.setPlaceId(0);
    if (userService.updateRunUser(user) > 0) {
      return R.ok("????????????");
    } else {
      return R.error("????????????????????????");
    }
  }

  @ApiOperation("??????????????????")
  @ApiImplicitParam(name = "placeName", value = "????????????", required = true, dataType = "String")
  @GetMapping("/selectPlaceList")
  public R selectPlaceLike(String placeName) {
    Map<String, Object> result = new HashMap<>();
    List<RunPlace> runPlaceList = runPlaceService.selectRunPlaceList(new RunPlace());
    if (runPlaceList.size() > 0) {
      return R.data(runPlaceList);
    } else {
      return R.error("????????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "placeId", value = "??????id", required = true, dataType = "Integer"),
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "Integer")
  })
  @GetMapping("/updatePlaceId")
  public R updatePlaceId(Integer placeId, Integer userId) {
    Map<String, Object> result = new HashMap<>();
    RunUser user = userService.selectRunUserById(userId);
    if (user != null) {
      if (user.getPlaceId() != null && user.getPlaceId() > 0) {
        return R.error("?????????????????????");
      } else {
        user.setPlaceId(placeId);
        if (userService.updateRunUser(user) > 0) {
          return R.ok("????????????");
        } else {
          return R.error("???????????????????????????");
        }
      }

    } else {
      return R.error("???????????????");
    }
  }

  @ApiOperation("????????????")
  @ApiImplicitParam(name = "userId", value = "??????id", required = true, dataType = "int")
  @GetMapping("/getMyMsg")
  public R getMyMsg(Long userId) {
    System.out.println("========userId=====" + userId);
    return R.ok();
  }

  @ApiOperation("???????????????")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "phone", value = "?????????", required = true, dataType = "String"),
    @ApiImplicitParam(name = "code", value = "?????????", required = true, dataType = "String")
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
          return R.error("???????????????????????????????????????????????????");
        } else {
          // session?????????????????????????????????+?????????
          if (!captcha.equalsIgnoreCase(phone + code)) {
            return R.error("?????????????????????");
          } else {
            RunUser user = new RunUser();
            user.setPhone(phone);
            boolean flag = userService.exit(user);
            if (!flag) {
              // return R.error("????????????????????????");
              // ????????????????????????????????????
              RunUser newUser = new RunUser();
              newUser.setPhone(phone);
              newUser.setPassword(passwordService.encryptPassword(phone, phone, user.getSalt()));
              newUser.setRegisterTime(DateUtils.getNowDate());
              newUser.setName("?????????");

              if (userService.insertRunUser(newUser) > 0) {
                UsernamePasswordToken token = new UsernamePasswordToken(phone, phone);
                subject.login(token);
                newUser.setLoginTime(new Date());
                userService.updateRunUser(newUser);
                data.put("data", newUser);
                return R.data(data);
              } else {
                return R.error("??????????????????????????????");
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
        return R.error("???????????????????????????????????????");
      }
    } catch (AuthenticationException e) {
      return R.error(e.toString());
    }
  }

  @ApiOperation("???????????????")
  @ApiImplicitParam(name = "phone", value = "?????????", required = true, dataType = "String")
  @GetMapping("/getCode")
  public R loginByCode(String phone) {
    Map<String, String> data = new HashMap<>();
    try {
      if (phone == null || "".equals(phone)) {
        return R.error("???????????????");
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

        // request.putQueryParameter("SignName", "????????????");
        // request.putQueryParameter("TemplateCode", "SMS_210064468");

        request.putQueryParameter("SignName", "??????");
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
