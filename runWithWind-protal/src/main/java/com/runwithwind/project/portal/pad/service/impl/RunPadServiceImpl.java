package com.runwithwind.project.portal.pad.service.impl;

import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.pad.domain.RunPad;
import com.runwithwind.project.portal.pad.mapper.RunPadMapper;
import com.runwithwind.project.portal.pad.service.IRunPadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * padService业务层处理
 * 
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunPadServiceImpl implements IRunPadService
{
    @Autowired
    private RunPadMapper runPadMapper;

    /**
     * 查询pad
     * 
     * @param id padID
     * @return pad
     */
    @Override
    public RunPad selectRunPadById(Long id)
    {
        return runPadMapper.selectRunPadById(id);
    }

    /**
     * 查询pad列表
     * 
     * @param runPad pad
     * @return pad
     */
    @Override
    public List<RunPad> selectRunPadList(RunPad runPad)
    {
        return runPadMapper.selectRunPadList(runPad);
    }

    /**
     * 新增pad
     * 
     * @param runPad pad
     * @return 结果
     */
    @Override
    public int insertRunPad(RunPad runPad)
    {
        runPad.setCreateTime(DateUtils.getNowDate());
        return runPadMapper.insertRunPad(runPad);
    }

    /**
     * 修改pad
     * 
     * @param runPad pad
     * @return 结果
     */
    @Override
    public int updateRunPad(RunPad runPad)
    {
        return runPadMapper.updateRunPad(runPad);
    }

    /**
     * 删除pad对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRunPadByIds(String ids)
    {
        return runPadMapper.deleteRunPadByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除pad信息
     * 
     * @param id padID
     * @return 结果
     */
    @Override
    public int deleteRunPadById(Long id)
    {
        return runPadMapper.deleteRunPadById(id);
    }

    @Override
    public RunPad selectRunPadByMac(String padMac) {
        return runPadMapper.selectRunPadByMac(padMac);
    }
}
