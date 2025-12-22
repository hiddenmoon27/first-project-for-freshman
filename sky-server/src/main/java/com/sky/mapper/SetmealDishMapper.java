package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    void saveBatch (List<SetmealDish> setmealDishList);

    @Select ("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getBySetmealId (Long id);

    @Delete ("delete from setmeal_dish where setmeal_id=#{ids}")
    void delete (Long ids);



    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */

}
