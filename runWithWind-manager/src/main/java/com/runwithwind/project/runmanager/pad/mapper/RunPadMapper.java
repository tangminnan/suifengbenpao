package com.runwithwind.project.runmanager.pad.mapper;

import java.util.List;
import com.runwithwind.project.runmanager.pad.domain.RunPad;

/**
 * padMapper接口
 * 
 * @author jzf
 * @date 2020-12-22
 */
public interface RunPadMapper 
{
    /**
     * 查询pad
     * 
     * @param id padID
     * @return pad
     */
    public RunPad selectRunPadById(Long id);

    /**
     * 查询pad列表
     * 
     * @param runPad pad
     * @return pad集合
     */
    public List<RunPad> selectRunPadList(RunPad runPad);

    /**
     * 新增pad
     * 
     * @param runPad pad
     * @return 结果
     */
    public int insertRunPad(RunPad runPad);

    /**
     * 修改pad
     * 
     * @param runPad pad
     * @return 结果
     */
    public int updateRunPad(RunPad runPad);

    /**
     * 删除pad
     * 
     * @param id padID
     * @return 结果
     */
    public int deleteRunPadById(Long id);

    /**
     * 批量删除pad
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRunPadByIds(String[] ids);

    public RunPad selectRunPadByMac(String padMac);
}
