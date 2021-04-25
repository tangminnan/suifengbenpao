package com.runwithwind.project.portal.place.service;


import com.runwithwind.project.portal.place.domain.RunPlace;

import java.util.List;

/**
 * 场地列Service接口
 * 
 * @author jzf
 * @date 2021-01-18
 */
public interface IRunPlaceService 
{
    /**
     * 查询场地列
     * 
     * @param id 场地列ID
     * @return 场地列
     */
    public RunPlace selectRunPlaceById(Integer id);

    public RunPlace selectRunPlaceByPhone(String phone);

    /**
     * 查询场地列列表
     * 
     * @param runPlace 场地列
     * @return 场地列集合
     */
    public List<RunPlace> selectRunPlaceList(RunPlace runPlace);

    /**
     * 新增场地列
     * 
     * @param runPlace 场地列
     * @return 结果
     */
    public int insertRunPlace(RunPlace runPlace);

    /**
     * 修改场地列
     * 
     * @param runPlace 场地列
     * @return 结果
     */
    public int updateRunPlace(RunPlace runPlace);

    /**
     * 批量删除场地列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRunPlaceByIds(String ids);

    /**
     * 删除场地列信息
     * 
     * @param id 场地列ID
     * @return 结果
     */
    public int deleteRunPlaceById(Integer id);
}
