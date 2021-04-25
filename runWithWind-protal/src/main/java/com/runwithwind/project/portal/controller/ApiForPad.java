package com.runwithwind.project.portal.controller;

import com.runwithwind.common.utils.AccessTokenAndNextOpenId;
import com.runwithwind.common.utils.GZHMsgVo;
import com.runwithwind.common.utils.R;
import com.runwithwind.common.utils.file.FileUploadUtils;
import com.runwithwind.framework.config.WechatOAConfig;
import com.runwithwind.framework.config.runwithwindConfig;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.project.portal.box.domain.RunBox;
import com.runwithwind.project.portal.box.service.IRunBoxService;
import com.runwithwind.project.portal.courseDevice.domain.RunCourseDevice;
import com.runwithwind.project.portal.courseDevice.service.IRunCourseDeviceService;
import com.runwithwind.project.portal.detail.service.IRunCourseDetailService;
import com.runwithwind.project.portal.device.domain.RunHeartRateDevice;
import com.runwithwind.project.portal.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.portal.pad.domain.RunPad;
import com.runwithwind.project.portal.pad.service.IRunPadService;
import com.runwithwind.project.portal.place.domain.RunPlace;
import com.runwithwind.project.portal.place.service.IRunPlaceService;
import com.runwithwind.project.portal.record.domain.RunCourseRecord;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Api(description = "pad接口", position = 3)
@RestController
@RequestMapping("/api/pad")
public class ApiForPad extends BaseController {
  @Autowired IRunCourseRecordService runCourseRecordService;
  @Autowired IRunPlaceService runPlaceService;
  @Autowired IRunPadService runPadService;
  @Autowired IRunHeartRateDeviceService runHeartRateDeviceService;
  @Autowired IRunCourseDeviceService runCourseDeviceService;
  @Autowired IRunUserService userService;
  @Autowired IRunBoxService boxService;
  @Autowired IRunCourseDetailService detailService;

  private static final String SERVER_URL = "http://8.140.170.161:8084";

  @ApiOperation("发起课程")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "macId", value = "pad的mac地址", required = true, dataType = "String"),
    @ApiImplicitParam(name = "className", value = "课程名称", required = true, dataType = "String")
  })
  @GetMapping("/attendClass")
  public R attendClass(String macId, String className) {

    RunCourseRecord runCourseRecord = new RunCourseRecord();
    runCourseRecord.setPadMac(macId);
    List<RunCourseRecord> runCourseRecordList =
        runCourseRecordService.selectRunCourseRecordList(runCourseRecord);

    boolean flag = true;
    if (runCourseRecordList != null && runCourseRecordList.size() > 0) {
      for (RunCourseRecord courseRecord : runCourseRecordList) {
        if (courseRecord.getType() == 1) flag = false;
      }
    }
    if (flag) {
      RunPad runPad = runPadService.selectRunPadByMac(macId);
      RunCourseRecord runCourseRecord1 = new RunCourseRecord();
      runCourseRecord.setStartTime(new Date());
      runCourseRecord.setPadMac(macId);
      runCourseRecord.setName(className);

      // 如果pad已存在
      if (runPad != null) {
        RunBox box = new RunBox();
        box.setBindPad(runPad.getId());
        List<RunBox> boxList = boxService.selectRunBoxList(box);
        if (boxList.size() > 0) runCourseRecord.setBoxMac(boxList.get(0).getBoxMac());
        else return R.error("未绑定盒子");

        RunBox box1 = new RunBox();
        box1.setId(boxList.get(0).getId());
        box1.setStatus(1);
        boxService.updateRunBox(box1);

        RunPlace runPlace = new RunPlace();
        List<RunPlace> runPlaceList = runPlaceService.selectRunPlaceList(runPlace);
        if (runPlaceList.size() > 0) {
          RunPlace place = runPlaceList.get(0);
          place.setStatus(1);
          if (runPlaceService.updateRunPlace(place) > 0) {
            runCourseRecord.setPlaceId(place.getId());
          }
        } else {
          return R.error(-1, "pad暂未绑定场地，请前往后台绑定");
        }
        runCourseRecord.setType(1);
        if (runCourseRecordService.insertRunCourseRecord(runCourseRecord) > 0) {

          return R.data(runCourseRecord);
        } else {
          return R.error(-1, "保存失败，请重新发起课程");
        }
      } else {

        return R.error(-1, "pad未录入，请前往后台录入并绑定场地");
      }
    } else {
      return R.error(-1, "当前pad正在上课中");
    }
  }

  @ApiOperation("添加设备上课")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "classId", value = "课程id", required = true, dataType = "int"),
    @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String"),
    @ApiImplicitParam(name = "startTime", value = "打卡时间", required = true, dataType = "String")
  })
  @GetMapping("/attendDevice")
  public R attendDevice(Integer classId, String deviceId, String startTime) {
    // 查询课程
    if (classId != null) {
      RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(classId);

      if (runCourseRecord != null) {
        // 查询设备是否存在并且已绑定
        RunHeartRateDevice runHeartRateDevice =
            runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(deviceId);
        if (runHeartRateDevice != null) {

          // 如果已绑定，直接添加上课
          if (runHeartRateDevice.getisbind() == 1
              && runHeartRateDevice.getUserId() != null
              && runHeartRateDevice.getUserId() > 0) {
            RunUser user = userService.selectRunUserById(runHeartRateDevice.getUserId());
            if (user != null) {
              // 查询该设备有没有在上课
              RunCourseDevice runCourseDevice = new RunCourseDevice();
              runCourseDevice.setDeviceId(deviceId);
              runCourseDevice.setType(1);
              List<RunCourseDevice> runCourseDeviceList =
                  runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
              if (runCourseDeviceList.size() > 0) {
                return R.error(500, "当前设备正在上课中");
              } else {
                // 添加记录
                runCourseDevice.setStartTime(startTime);
                runCourseDevice.setCourseId(classId);
                runCourseDevice.setAddTime(new Date());
                runCourseDevice.setUserId(runHeartRateDevice.getUserId());

                if (runCourseDeviceService.insertRunCourseDevice(runCourseDevice) > 0) {
                  runHeartRateDevice.setStatus(1);
                  runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice);
                  user.setDeviceId(deviceId);
                  if (user.getHeadImg() == null) {
                    user.setHeadImg("");
                  }
                  return R.data(user);
                } else {
                  return R.error("保存失败");
                }
              }

            } else {
              return R.error(-1, "绑定用户已不存在请前往后台重新绑定");
            }

          } else {
            return R.error(-1, "未绑定用户");
          }
        } else {

          // 查询该设备有没有在上课
          RunCourseDevice runCourseDevice = new RunCourseDevice();
          runCourseDevice.setDeviceId(deviceId);
          runCourseDevice.setType(1);
          List<RunCourseDevice> runCourseDeviceList =
              runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
          if (runCourseDeviceList.size() > 0) {
            return R.error(500, "当前设备正在上课中");
          } else {
            return R.error(202, "新设备，前往拍照绑定新用户");
          }
        }
      } else {
        return R.error("课程不存在");
      }
    } else {
      return R.error("课程id为空");
    }
  }

  @ApiOperation("通过拍照添加用户并添加到课程中")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "classId", value = "课程id", required = true, dataType = "int"),
    @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String"),
    @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = true, dataType = "String"),
    @ApiImplicitParam(name = "startTime", value = "打卡时间", required = true, dataType = "String")
  })
  @GetMapping("/attendUserByPhoto")
  public R attendUserByPhoto(Integer classId, String deviceId, String imgUrl, String startTime) {
    // 查询课程
    if (classId != null) {
      RunCourseRecord runCourseRecord = runCourseRecordService.selectRunCourseRecordById(classId);

      if (runCourseRecord != null) {
        RunCourseDevice runCourseDevice = new RunCourseDevice();
        runCourseDevice.setDeviceId(deviceId);
        runCourseDevice.setType(1);

        List<RunCourseDevice> runCourseDeviceList =
            runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
        if (runCourseDeviceList.size() > 0) {
          return R.error(500, "当前设备正在上课中");
        } else {
          // 添加记录
          runCourseDevice.setStartTime(startTime);
          runCourseDevice.setCourseId(classId);
          runCourseDevice.setAddTime(new Date());
          runCourseDevice.setType(1);

          RunUser user = new RunUser();
          user.setHeadImg(imgUrl);
          user.setType(1);
          user.setRegisterTime(new Date());
          user.setName("新用户");

          if (userService.insertRunUser(user) > 0) {
            // 添加课程记录
            runCourseDevice.setUserId(user.getId());
            if (runCourseDeviceService.insertRunCourseDevice(runCourseDevice) > 0) {
              user.setDeviceId(deviceId);
              return R.data(user);
            } else {
              return R.error("保存失败");
            }
          } else {
            return R.error("新建用户失败");
          }
        }
      } else {
        return R.error("课程不存在");
      }
    } else {
      return R.error("课程id为空");
    }
  }

  @ApiOperation("上传文件接口(拍照)")
  @PostMapping(value = "/uploadFile", headers = "content-type=multipart/form-data")
  public R uploadFile(
      @ApiParam(name = "file", value = "file", required = true) MultipartFile file) {
    // 上传文件路径
    String filePath = runwithwindConfig.getUploadPath();
    // 上传并返回新文件名称
    String fileName = null;
    try {
      fileName = FileUploadUtils.upload(filePath, file);
      return R.data(SERVER_URL + fileName);
    } catch (IOException e) {
      e.printStackTrace();
      return R.error(e.toString());
    }
  }

  @ApiOperation("下课")
  @ApiImplicitParam(name = "macId", value = "pad的mac地址", required = true, dataType = "String")
  @GetMapping("/downClass")
  public R downClass(String macId) {
    RunPad pad = runPadService.selectRunPadByMac(macId);

    // 更改课程状态
    RunCourseRecord runCourseRecord = new RunCourseRecord();
    runCourseRecord.setType(1);
    runCourseRecord.setPadMac(macId);
    if (runCourseRecordService.selectRunCourseRecordList(runCourseRecord).size() > 0)
      runCourseRecord = runCourseRecordService.selectRunCourseRecordList(runCourseRecord).get(0);
    else return R.error(501, "已下课,请勿重复操作");
    runCourseRecord.setType(2);
    runCourseRecord.setEndTime(new Date());
    runCourseRecordService.updateRunCourseRecord(runCourseRecord);

    // 更改场地状态
    RunPlace place = new RunPlace();
    List<RunPlace> runPlaceList = runPlaceService.selectRunPlaceList(place);
    runPlaceList.get(0).setStatus(2);
    runPlaceService.updateRunPlace(runPlaceList.get(0));

    // 更改课程的所有设备状态
    RunCourseDevice device = new RunCourseDevice();
    device.setCourseId(runCourseRecord.getId());

    List<RunCourseDevice> deviceList = runCourseDeviceService.selectRunCourseDeviceList(device);
    for (RunCourseDevice runCourseDevice : deviceList) {
      RunHeartRateDevice runHeartRateDevice =
          runHeartRateDeviceService.selectRunHeartRateDeviceByDeviceId(
              runCourseDevice.getDeviceId());
      if (runHeartRateDevice != null) {
        runHeartRateDevice.setStatus(2);
        runHeartRateDeviceService.updateRunHeartRateDevice(runHeartRateDevice);
      }
      runCourseDevice.setType(2);
      runCourseDeviceService.updateRunCourseDevice(runCourseDevice);
    }

    // 更改盒子状态
    RunBox box = new RunBox();
    box.setBindPad(pad.getId());
    List<RunBox> runBoxList = boxService.selectRunBoxList(box);
    runBoxList.get(0).setStatus(2);
    boxService.updateRunBox(runBoxList.get(0));

    // 查询上课人员，有微信openid的集合
    List<String> openid = detailService.selectUserBycourseId(runCourseRecord.getId());
    System.out.println("===========openid================" + openid);
    RunCourseRecord finalRunCourseRecord = runCourseRecord;
    new Thread(
            () -> {
              sendReport(openid, finalRunCourseRecord.getId());
            })
        .start();
    return R.ok();
  }

  @ApiOperation("获取上课状态")
  @ApiImplicitParam(name = "macId", value = "pad的mac地址", required = true, dataType = "String")
  @GetMapping("/getClassStatus")
  public R getClassStatus(String macId) {
    Map<String, Object> result = new HashMap<>();
    RunCourseRecord runCourseRecord = new RunCourseRecord();
    runCourseRecord.setType(1);
    runCourseRecord.setPadMac(macId);
    List<RunCourseRecord> courseRecordList =
        runCourseRecordService.selectRunCourseRecordList(runCourseRecord);
    if (courseRecordList != null && courseRecordList.size() > 0) {
      result.put("course", courseRecordList.get(0));

      RunCourseDevice courseDevice = new RunCourseDevice();
      courseDevice.setCourseId(courseRecordList.get(0).getId());
      courseDevice.setType(1);
      List<RunCourseDevice> runCourseDeviceList =
          runCourseDeviceService.selectRunCourseDeviceList(courseDevice);
      List<RunUser> userList = new ArrayList<>();
      for (RunCourseDevice device : runCourseDeviceList) {
        RunUser user = userService.selectRunUserById(device.getUserId());
        if (user != null) {
          user.setDeviceId(device.getDeviceId());
          if (user.getHeadImg() == null) {
            user.setHeadImg("");
          }
          userList.add(user);
        }
      }
      result.put("userList", userList);

      return R.data(result);
    } else {
      return R.error(201, "未上课");
    }
  }

  @ApiOperation("某人设备号下课")
  @ApiImplicitParam(name = "deviceId", value = "设备号", required = true, dataType = "String")
  @GetMapping("/downClassBydevice")
  public R downClassBydevice(String deviceId, Integer courseId) {
    RunCourseDevice runCourseDevice = new RunCourseDevice();
    runCourseDevice.setType(1);
    runCourseDevice.setDeviceId(deviceId);
    List<RunCourseDevice> runCourseDeviceList =
        runCourseDeviceService.selectRunCourseDeviceList(runCourseDevice);
    if (runCourseDeviceList.size() > 0) {
      runCourseDeviceList.get(0).setType(2);
      if (runCourseDeviceService.updateRunCourseDevice(runCourseDeviceList.get(0)) > 0) {
        // 查询该课程是否还有设备在线，没有直接下线
        return R.ok("下课成功");
      } else {
        return R.error(201, "下课失败,请重试");
      }
    } else {
      return R.error(202, "该设备暂未上课");
    }
  }

  R sendReport(List<String> userOpenid, Integer courseId) {
    AccessTokenAndNextOpenId accessTokenAndNextOpenId = WechatOAConfig.getAccessToken();
    // 获取接口调用凭证
    System.out.println("=====accessTokenAndNextOpenId================" + accessTokenAndNextOpenId);
    if (accessTokenAndNextOpenId == null
        || accessTokenAndNextOpenId.getAccessToken() == null
        || accessTokenAndNextOpenId.getNextOpenId() == null) {
      return R.error("获取接口调用凭证失败");
    } else {
      String accessToken = accessTokenAndNextOpenId.getAccessToken();
      /* String accessToken =
          "41_CWO_esaOutN8NMu8adTppT8PTDMzLQqPXeShHUgWiQ2f-9CC3zcD4DYFxvcWcoNA8dv15EcPhQrJGcVfPnK0CmNia_gmqhdLR343m07nthusZoyw2M_xiq4Ig_Jh88HfEKW-tAivI5vT6-vOFUSiAIAZOM";
      Map<String, Object> paramsMap = new HashMap<String, Object>();*/

      List<GZHMsgVo> voList = new ArrayList<GZHMsgVo>();
      for (int i = 0; i < userOpenid.size(); i++) {
        String openid = userOpenid.get(i);
        RunUser user = userService.selectRunUserByOpenId(openid);

        if (user != null) {
          Map map = detailService.selectClassReport(courseId, user.getId());

          Map map1 = detailService.selectClassTime(courseId, user.getId());
          Map<String, Object> json = (HashMap) map.get("avg");

          GZHMsgVo vo = new GZHMsgVo();
          vo.setFirst("尊敬的用户，您本次课程已结束！");
          vo.setTouser(openid);
          vo.setTemplateId(WechatOAConfig.TEMPLATE_ID);
          vo.setKeyword1(map.get("courseName") + "");
          vo.setKeyword2("西贝");
          vo.setKeyword3(map.get("placeName") + "");

          vo.setKeyword4(map1.get("min") + " -" + map1.get("max").toString().split(" ")[1]);
          String remark = "";
          DecimalFormat df = new DecimalFormat("#.#");
          Double calorie = (Double) json.get("calorie");
          Double maxHeart = (Double) json.get("maxHeart");
          Double avgHeart = (Double) json.get("avgHeart");
          Double minHeart = (Double) json.get("minHeart");

          remark += "本次运动共消耗卡路里";
          if (json.get("calorie") != null) remark += df.format(calorie) + "cal,最高心率为";
          else remark += "0cal,最高心率为";

          if (json.get("maxHeart") != null) remark += df.format(maxHeart) + ",平均心率为";
          else remark += "0,平均心率为";

          if (json.get("avgHeart") != null) remark += df.format(avgHeart) + ",最低心率为";
          else remark += "0,最低心率为";

          if (json.get("minHeart") != null) remark += df.format(minHeart);
          else remark += "0";
          vo.setRemark(remark);

          vo.setRemakColor("#173177");
          vo.setSmUrl(WechatOAConfig.SM_URL + courseId + "&userId=" + user.getId()); // 详情跳转路径
          voList.add(vo);
        }
      }
      WechatOAConfig.sendMessagePush(voList, accessToken);

      return R.ok("课程报告推送成功");
    }
  }
}
