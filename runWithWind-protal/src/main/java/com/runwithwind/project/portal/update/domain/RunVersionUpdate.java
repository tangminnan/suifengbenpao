package com.runwithwind.project.portal.update.domain;

import com.runwithwind.framework.aspectj.lang.annotation.Excel;
import com.runwithwind.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 【请填写功能名称】对象 run_version_update
 * 
 * @author jzf
 * @date 2021-01-06
 */
public class RunVersionUpdate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 版本号 */
    @Excel(name = "${findComment}")
    private Long versionNum;

    /** 版本名称 */
    @Excel(name = "${findComment}")
    private String versionName;

    /** 1 盒子; 2  pad */
    @Excel(name = "${findComment}")
    private Integer type;

    private Date updateTime;

    /** 更新内容 */
    @Excel(name = "${findComment}")
    private String versionContent;

    /** 下载地址 */
    @Excel(name = "${findComment}")
    private String linkUrl;

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setVersionNum(Long versionNum)
    {
        this.versionNum = versionNum;
    }

    public Long getVersionNum()
    {
        return versionNum;
    }
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getVersionName()
    {
        return versionName;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setVersionContent(String versionContent)
    {
        this.versionContent = versionContent;
    }

    public String getVersionContent()
    {
        return versionContent;
    }
    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl()
    {
        return linkUrl;
    }

}
