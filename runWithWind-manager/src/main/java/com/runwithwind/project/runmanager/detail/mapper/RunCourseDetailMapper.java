package com.runwithwind.project.runmanager.detail.mapper;

import java.util.List;
import com.runwithwind.project.runmanager.detail.domain.RunCourseDetail;

/**
 * 课程详情Mapper接口
 * 
 * @author jzf
 * @date 2020-12-22
 */
public interface RunCourseDetailMapper 
{
    /**
     * 查询课程详情
     * 
     * @param id 课程详情ID
     * @return 课程详情
     */
    public RunCourseDetail selectRunCourseDetailById(Long id);

    /**
     * 查询课程详情列表
     * 
     * @param runCourseDetail 课程详情
     * @return 课程详情集合
     */
    public List<RunCourseDetail> selectRunCourseDetailList(RunCourseDetail runCourseDetail);

    /**
     * 新增课程详情
     * 
     * @param runCourseDetail 课程详情
     * @return 结果
     */
    public int insertRunCourseDetail(RunCourseDetail runCourseDetail);

    /**
     * 修改课程详情
     * 
     * @param runCourseDetail 课程详情
     * @return 结果
     */
    public int updateRunCourseDetail(RunCourseDetail runCourseDetail);

    /**
     * 删除课程详情
     * 
     * @param id 课程详情ID
     * @return 结果
     */
    public int deleteRunCourseDetailById(Long id);

    /**
     * 批量删除课程详情
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRunCourseDetailByIds(String[] ids);
}
