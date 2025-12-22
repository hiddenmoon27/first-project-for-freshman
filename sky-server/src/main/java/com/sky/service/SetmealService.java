package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/22 17:38
 *@Param:
 *@Return:
 *@Description:
 **/
public interface SetmealService {
    void insert (SetmealDTO setmealDTO);

    SetmealVO getById (Long id);

    void delete (List<Long> ids);

    void status (Integer status, Long id);

    PageResult page (SetmealPageQueryDTO setmealPageQueryDTO);

    void update (SetmealDTO setmealDTO);
}
