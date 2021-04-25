package com.runwithwind.project.runmanager.record.service;

import java.util.List;
import com.runwithwind.project.runmanager.record.domain.RunCourseRecord;

/**
 * 课程记录Service接口
 * 
 * @author jzf
 * @date 2020-12-22
 */
public interface IRunCourseRecordService 
{
    /**
     * 查询课程记录
     * 
     * @param id 课程记录ID
     * @return 课程记录
     */
    public RunCourseRecord selectRunCourseRecordById(Integer id);

    /**
     * 查询课程记录列表
     * 
     * @param runCourseRecord 课程记录
     * @return 课程记录集合
     */
    public List<RunCourseRecord> selectRunCourseRecordList(RunCourseRecord runCourseRecord);

    /**
     * 新增课程记录
     * 
     * @param runCourseRecord 课程记录
     * @return 结果
     */
    public int insertRunCourseRecord(RunCourseRecord runCourseRecord);

    /**
     * 修改课程记录
     * 
     * @param runCourseRecord 课程记录
     * @return 结果
     */
    public int updateRunCourseRecord(RunCourseRecord runCourseRecord);

    /**
     * 批量删除课程记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRunCourseRecordByIds(String ids);

    /**
     * 删除课程记录信息
     * 
     * @param id 课程记录ID
     * @return 结果
     */
    public int deleteRunCourseRecordById(Integer id);
}
