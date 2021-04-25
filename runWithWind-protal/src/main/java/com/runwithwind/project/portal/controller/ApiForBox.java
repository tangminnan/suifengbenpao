package com.runwithwind.project.portal.controller;

import com.runwithwind.common.utils.R;
import com.runwithwind.framework.web.controller.BaseController;
import com.runwithwind.project.portal.box.service.IRunBoxService;
import com.runwithwind.project.portal.courseDevice.domain.RunCourseDevice;
import com.runwithwind.project.portal.courseDevice.service.IRunCourseDeviceService;
import com.runwithwind.project.portal.detail.domain.RunCourseDetail;
import com.runwithwind.project.portal.detail.service.IRunCourseDetailService;
import com.runwithwind.project.portal.device.service.IRunHeartRateDeviceService;
import com.runwithwind.project.portal.pad.service.IRunPadService;
import com.runwithwind.project.portal.place.service.IRunPlaceService;
import com.runwithwind.project.portal.record.domain.RunCourseRecord;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import com.runwithwind.project.portal.update.domain.RunVersionUpdate;
import com.runwithwind.project.portal.update.service.IRunVersionUpdateService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.portal.user.service.IRunUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(description = "盒子接口", position = 3)
@RestController
@RequestMapping("/api/box")
public class ApiForBox extends BaseController {
  @Autowired IRunCourseRecordService runCourseRecordService;
  @Autowired IRunPlaceService runPlaceService;
  @Autowired IRunPadService runPadService;
  @Autowired IRunHeartRateDeviceService runHeartRateDeviceService;
  @Autowired IRunCourseDeviceService runCourseDeviceService;
  @Autowired IRunUserService userService;
  @Autowired IRunBoxService boxService;
  @Autowired IRunCourseDetailService detailService;
  @Autowired private IRunVersionUpdateService versionUpdateService;

  private static final String SERVER_URL = "http://8.140.170.161:8084";

  @ApiOperation("获取上课状态")
  @ApiImplicitParam(name = "macId", value = "box的mac地址", required = true, dataType = "String")
  @GetMapping("/getClassStatus")
  public R getClassStatus(String macId) {
    Map<String, Object> result = new HashMap<>();
    RunCourseRecord runCourseRecord = new RunCourseRecord();
    runCourseRecord.setType(1);
    runCourseRecord.setBoxMac(macId);
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
        user.setDeviceId(device.getDeviceId());
        userList.add(user);
      }
      result.put("userList", userList);

      return R.data(result);
    } else {
      return R.error(201, "未上课");
    }
  }

  @ApiOperation("实时上传数据(暂定这种方式，之后有时间改成netty通讯)")
  @ApiImplicitParam(name = "runCourseDetailList", value = "设备数据上传", dataType = "RunCourseDetail")
  @PostMapping("/uploadData")
  public R uploadData(@RequestBody List<RunCourseDetail> runCourseDetailList) {
    for (RunCourseDetail runCourseDetail : runCourseDetailList) {
      if (runCourseDetail.getUserId() == null || runCourseDetail.getUserId().equals(0)) {
        runCourseDetail.setUserId(
            runHeartRateDeviceService
                .selectRunHeartRateDeviceByDeviceId(runCourseDetail.getDeviceId())
                .getUserId());
      }
      runCourseDetail.setSaveTime(new Date());
    }
    int result = detailService.insertRunCourseDetailList(runCourseDetailList);
    if (result > 0) {
      return R.ok();
    } else {
      return R.error();
    }
  }

  @ApiOperation("版本更新(盒子第一次打开时调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "code", value = "当前版本号", required = true),
    @ApiImplicitParam(name = "type", value = "类型(1=盒子，2=pad)", required = true, dataType = "String")
  })
  @GetMapping("/versionUpdate")
  public R list(Integer code, Integer type) {
    RunVersionUpdate version = versionUpdateService.versionCheck(type);
    if (version != null) {
      if (code < version.getVersionNum()) {
        return R.data(version);
      } else {
        return R.error(201, "已是最新版本");
      }
    } else {
      return R.error(202, "暂无版本信息");
    }
  }
}
