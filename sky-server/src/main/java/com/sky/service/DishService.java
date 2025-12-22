package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 22:59
 *@Param:
 *@Return:
 *@Description:
 **/
@Service
public interface DishService {

    public void save (DishDTO dishDTO) ;

    PageResult page (DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch (List<Long> ids);

    DishVO getById (Long id);

    void updateWithFlavors (DishDTO dishDTO);

    List<Dish> getByCategoryId (Long id);
}
