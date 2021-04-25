package com.runwithwind.project.portal.update.mapper;

import java.util.List;
import com.runwithwind.project.portal.update.domain.RunVersionUpdate;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author jzf
 * @date 2021-01-06
 */
public interface RunVersionUpdateMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RunVersionUpdate selectRunVersionUpdateById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RunVersionUpdate> selectRunVersionUpdateList(RunVersionUpdate runVersionUpdate);

    /**
     * 新增【请填写功能名称】
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 结果
     */
    public int insertRunVersionUpdate(RunVersionUpdate runVersionUpdate);

    /**
     * 修改【请填写功能名称】
     * 
     * @param runVersionUpdate 【请填写功能名称】
     * @return 结果
     */
    public int updateRunVersionUpdate(RunVersionUpdate runVersionUpdate);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRunVersionUpdateById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRunVersionUpdateByIds(String[] ids);

    RunVersionUpdate versionCheck(Integer type);
}
