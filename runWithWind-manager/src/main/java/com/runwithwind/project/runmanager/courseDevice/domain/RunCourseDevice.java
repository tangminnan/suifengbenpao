package com.runwithwind.project.runmanager.courseDevice.domain;

import java.util.Date;
import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 课程中设备记录对象 run_course_device
 *
 * @author jzf
 * @date 2021-01-19
 */
public class RunCourseDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 课程id */
    @Excel(name = "${findComment}")
    private Long courseId;

    /** 设备号 */
    @Excel(name = "${findComment}")
    private String deviceId;

    /** 加入时间 */
    @Excel(name = "${findComment}")
    private String startTime;

    /** 用户id */
    @Excel(name = "${findComment}")
    private Long userId;

    /** 1=上课中，2=下课 */
    @Excel(name = "${findComment}")
    private Integer type;

    /** 添加时间 */
    @Excel(name = "${findComment}", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
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

    public String getStartTime()
    {
        return startTime;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("courseId", getCourseId())
            .append("deviceId", getDeviceId())
            .append("startTime", getStartTime())
            .append("userId", getUserId())
            .append("type", getType())
            .append("addTime", getAddTime())
            .toString();
    }
}
