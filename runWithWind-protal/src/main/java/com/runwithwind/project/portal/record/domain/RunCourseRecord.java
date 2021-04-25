package com.runwithwind.project.portal.record.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 课程记录对象 run_course_record
 * 
 * @author jzf
 * @date 2020-12-22
 */
public class RunCourseRecord
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 课程名 */
    @Excel(name = "课程名称")
    private String name;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** pad id */
    @Excel(name = "padmac")
    private String padMac;
    @Excel(name = "pad名称")
    private String padName;

    /** 场地id */
    @Excel(name = "场地id")
    private Integer placeId;
    @Excel(name = "场地名称")
    private String placeName;

    private String boxMac;

    /** 类型，预留 */
    private Integer type;


    public String getBoxMac() {
        return boxMac;
    }

    public void setBoxMac(String boxMac) {
        this.boxMac = boxMac;
    }

    public String getPadName() {
        return padName;
    }

    public void setPadName(String padName) {
        this.padName = padName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setPadMac(String padMac)
    {
        this.padMac = padMac;
    }

    public String getPadMac()
    {
        return padMac;
    }
    public void setPlaceId(Integer placeId)
    {
        this.placeId = placeId;
    }

    public Integer getPlaceId()
    {
        return placeId;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("padMac", getPadMac())
            .append("placeId", getPlaceId())
            .append("type", getType())
            .toString();
    }
}
