package com.runwithwind.project.runmanager.detail.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runwithwind.project.runmanager.detail.mapper.RunCourseDetailMapper;
import com.runwithwind.project.runmanager.detail.domain.RunCourseDetail;
import com.runwithwind.project.runmanager.detail.service.IRunCourseDetailService;
import com.runwithwind.common.utils.text.Convert;

/**
 * 课程详情Service业务层处理
 * 
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunCourseDetailServiceImpl implements IRunCourseDetailService 
{
    @Autowired
    private RunCourseDetailMapper runCourseDetailMapper;

    /**
     * 查询课程详情
     * 
     * @param id 课程详情ID
     * @return 课程详情
     */
    @Override
    public RunCourseDetail selectRunCourseDetailById(Long id)
    {
        return runCourseDetailMapper.selectRunCourseDetailById(id);
    }

    /**
     * 查询课程详情列表
     * 
     * @param runCourseDetail 课程详情
     * @return 课程详情
     */
    @Override
    public List<RunCourseDetail> selectRunCourseDetailList(RunCourseDetail runCourseDetail)
    {
        return runCourseDetailMapper.selectRunCourseDetailList(runCourseDetail);
    }

    /**
     * 新增课程详情
     * 
     * @param runCourseDetail 课程详情
     * @return 结果
     */
    @Override
    public int insertRunCourseDetail(RunCourseDetail runCourseDetail)
    {
        return runCourseDetailMapper.insertRunCourseDetail(runCourseDetail);
    }

    /**
     * 修改课程详情
     * 
     * @param runCourseDetail 课程详情
     * @return 结果
     */
    @Override
    public int updateRunCourseDetail(RunCourseDetail runCourseDetail)
    {
        return runCourseDetailMapper.updateRunCourseDetail(runCourseDetail);
    }

    /**
     * 删除课程详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRunCourseDetailByIds(String ids)
    {
        return runCourseDetailMapper.deleteRunCourseDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除课程详情信息
     * 
     * @param id 课程详情ID
     * @return 结果
     */
    @Override
    public int deleteRunCourseDetailById(Long id)
    {
        return runCourseDetailMapper.deleteRunCourseDetailById(id);
    }
}
