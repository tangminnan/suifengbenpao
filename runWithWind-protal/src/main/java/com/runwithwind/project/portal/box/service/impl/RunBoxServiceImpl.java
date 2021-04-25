package com.runwithwind.project.portal.box.service.impl;

import com.runwithwind.common.utils.DateUtils;
import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.box.domain.RunBox;
import com.runwithwind.project.portal.box.mapper.RunBoxMapper;
import com.runwithwind.project.portal.box.service.IRunBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 盒子Service业务层处理
 * 
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunBoxServiceImpl implements IRunBoxService
{
    @Autowired
    private RunBoxMapper runBoxMapper;

    /**
     * 查询盒子
     * 
     * @param id 盒子ID
     * @return 盒子
     */
    @Override
    public RunBox selectRunBoxById(Long id)
    {
        return runBoxMapper.selectRunBoxById(id);
    }

    /**
     * 查询盒子列表
     * 
     * @param runBox 盒子
     * @return 盒子
     */
    @Override
    public List<RunBox> selectRunBoxList(RunBox runBox)
    {
        return runBoxMapper.selectRunBoxList(runBox);
    }

    /**
     * 新增盒子
     * 
     * @param runBox 盒子
     * @return 结果
     */
    @Override
    public int insertRunBox(RunBox runBox)
    {
        runBox.setCreateTime(DateUtils.getNowDate());
        return runBoxMapper.insertRunBox(runBox);
    }

    /**
     * 修改盒子
     * 
     * @param runBox 盒子
     * @return 结果
     */
    @Override
    public int updateRunBox(RunBox runBox)
    {
        return runBoxMapper.updateRunBox(runBox);
    }

    /**
     * 删除盒子对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRunBoxByIds(String ids)
    {
        return runBoxMapper.deleteRunBoxByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除盒子信息
     * 
     * @param id 盒子ID
     * @return 结果
     */
    @Override
    public int deleteRunBoxById(Long id)
    {
        return runBoxMapper.deleteRunBoxById(id);
    }
}
