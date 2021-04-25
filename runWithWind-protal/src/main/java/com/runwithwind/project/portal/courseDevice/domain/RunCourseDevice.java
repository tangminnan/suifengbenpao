package com.runwithwind.project.portal.courseDevice.domain;

import java.util.Date;
import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;

/**
 * 课程中设备记录对象 run_course_device
 * 
 * @author jzf
 * @date 2021-01-04
 */
public class RunCourseDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 课程id */
    @Excel(name = "${findComment}")
    private Integer courseId;

    /** 设备号 */
    @Excel(name = "${findComment}")
    private String deviceId;

    private Integer userId;

    private Integer type;

    /** 加入时间 */
    @Excel(name = "${findComment}")
    private String startTime;

    /** 添加时间 */
    @Excel(name = "${findComment}", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCourseId(Integer courseId)
    {
        this.courseId = courseId;
    }

    public Integer getCourseId()
    {
        return courseId;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartTime()
    {
        return startTime;
    }
    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    @Override
    public String toString() {
        return "RunCourseDevice{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", deviceId='" + deviceId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
