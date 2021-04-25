package com.runwithwind.project.portal.record.service.impl;

import com.runwithwind.common.utils.text.Convert;
import com.runwithwind.project.portal.record.domain.RunCourseRecord;
import com.runwithwind.project.portal.record.mapper.RunCourseRecordMapper;
import com.runwithwind.project.portal.record.service.IRunCourseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程记录Service业务层处理
 * 
 * @author jzf
 * @date 2020-12-22
 */
@Service
public class RunCourseRecordServiceImpl implements IRunCourseRecordService
{
    @Autowired
    private RunCourseRecordMapper runCourseRecordMapper;

    /**
     * 查询课程记录
     * 
     * @param id 课程记录ID
     * @return 课程记录
     */
    @Override
    public RunCourseRecord selectRunCourseRecordById(Integer id)
    {
        return runCourseRecordMapper.selectRunCourseRecordById(id);
    }

    /**
     * 查询课程记录列表
     * 
     * @param runCourseRecord 课程记录
     * @return 课程记录
     */
    @Override
    public List<RunCourseRecord> selectRunCourseRecordList(RunCourseRecord runCourseRecord)
    {
        return runCourseRecordMapper.selectRunCourseRecordList(runCourseRecord);
    }

    /**
     * 新增课程记录
     * 
     * @param runCourseRecord 课程记录
     * @return 结果
     */
    @Override
    public int insertRunCourseRecord(RunCourseRecord runCourseRecord)
    {
        return runCourseRecordMapper.insertRunCourseRecord(runCourseRecord);
    }

    /**
     * 修改课程记录
     * 
     * @param runCourseRecord 课程记录
     * @return 结果
     */
    @Override
    public int updateRunCourseRecord(RunCourseRecord runCourseRecord)
    {
        return runCourseRecordMapper.updateRunCourseRecord(runCourseRecord);
    }

    /**
     * 删除课程记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRunCourseRecordByIds(String ids)
    {
        return runCourseRecordMapper.deleteRunCourseRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除课程记录信息
     * 
     * @param id 课程记录ID
     * @return 结果
     */
    @Override
    public int deleteRunCourseRecordById(Integer id)
    {
        return runCourseRecordMapper.deleteRunCourseRecordById(id);
    }

    @Override
    public RunCourseRecord selectRunCourseRecordByPadMac(String macId) {
        return runCourseRecordMapper.selectRunCourseRecordByPadMac(macId);
    }

    @Override
    public List<RunCourseRecord> getCourseByUserId(Integer userId) {
        return runCourseRecordMapper.getCourseByUserId(userId);
    }
}
