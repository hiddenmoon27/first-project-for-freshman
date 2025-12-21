package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 23:09
 *@Param:
 *@Return:
 *@Description:
 **/
@Mapper
public interface DishFlavorMapper {


    void saveBatch (List<DishFlavor> flavors);

    @Delete ("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishID (Long dishId);

    @Select ("select * from dish_flavor where dish_id=#{id};")
    List<DishFlavor> geyById (Long id);
}
