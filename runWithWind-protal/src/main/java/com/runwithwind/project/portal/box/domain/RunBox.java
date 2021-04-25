package com.runwithwind.project.portal.box.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 盒子对象 run_box
 * 
 * @author jzf
 * @date 2020-12-22
 */
public class RunBox extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 盒子mac */
    @Excel(name = "盒子mac")
    private String boxMac;

    /** $column.columnComment */
    @Excel(name = "盒子名称")
    private String boxName;

    private Integer bindPlace;

    private String placeName;

    private int status;

    private Long bindPad;

    private String padName;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPadName() {
        return padName;
    }

    public void setPadName(String padName) {
        this.padName = padName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBoxMac(String boxMac)
    {
        this.boxMac = boxMac;
    }

    public String getBoxMac()
    {
        return boxMac;
    }
    public void setBoxName(String boxName)
    {
        this.boxName = boxName;
    }

    public String getBoxName()
    {
        return boxName;
    }
    public void setBindPlace(Integer bindPlace)
    {
        this.bindPlace = bindPlace;
    }

    public Integer getBindPlace()
    {
        return bindPlace;
    }
    public void setBindPad(Long bindPad)
    {
        this.bindPad = bindPad;
    }

    public Long getBindPad()
    {
        return bindPad;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("boxMac", getBoxMac())
            .append("boxName", getBoxName())
            .append("bindPlace", getBindPlace())
            .append("bindPad", getBindPad())
            .append("createTime", getCreateTime())
            .toString();
    }
}
