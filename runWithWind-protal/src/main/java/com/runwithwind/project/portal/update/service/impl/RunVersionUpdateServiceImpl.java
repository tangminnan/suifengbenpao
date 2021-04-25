package com.runwithwind.project.portal.update.service.impl;

import java.util.Date;
import java.util.List;
import com.runwithwind.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runwithwind.project.portal.update.mapper.RunVersionUpdateMapper;
import com.runwithwind.project.portal.update.domain.RunVersionUpdate;
import com.runwithwind.project.portal.update.service.IRunVersionUpdateService;
import com.runwithwind.common.utils.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author jzf
 * @date 2021-01-06
 */
@Service
public class RunVersionUpdateServiceImpl implements IRunVersionUpdateService 
{
    @Autowired
    private RunVersionUpdateMapper runVersionUpdateMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RunVersionUpdate selectRunVersionUpdateById(Long id)
    {
        return runVersionUpdateMapper.selectRunVersionUpdateById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RunVersionUpdate> selectRunVersionUpdateList(RunVersionUpdate runVersionUpdate)
    {
        return runVersionUpdateMapper.selectRunVersionUpdateList(runVersionUpdate);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRunVersionUpdate(RunVersionUpdate runVersionUpdate)
    {
        return runVersionUpdateMapper.insertRunVersionUpdate(runVersionUpdate);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRunVersionUpdate(RunVersionUpdate runVersionUpdate)
    {
        runVersionUpdate.setUpdateTime(new Date());
        return runVersionUpdateMapper.updateRunVersionUpdate(runVersionUpdate);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRunVersionUpdateByIds(String ids)
    {
        return runVersionUpdateMapper.deleteRunVersionUpdateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRunVersionUpdateById(Long id)
    {
        return runVersionUpdateMapper.deleteRunVersionUpdateById(id);
    }

    @Override
    public RunVersionUpdate versionCheck(Integer type) {
        return runVersionUpdateMapper.versionCheck(type);
    }
}
