package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 23:00
 *@Param:
 *@Return:
 *@Description:
 **/
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    public DishMapper dishMapper;
    @Autowired
    public DishFlavorMapper dishFlavorMapper;
    @Autowired
    public SetmealMapper setmealMapper;

    @Override
    public void save (DishDTO dishDTO) {
        Dish dish = new Dish ();
        BeanUtils.copyProperties (dishDTO, dish);
        dishMapper.save (dish);
        Long dishId = dish.getId ();
        List<DishFlavor> flavors = dishDTO.getFlavors ();
        if (flavors != null && flavors.size () > 0) {
            flavors.forEach (dishFlavor -> {
                dishFlavor.setDishId (dishId);
            });
            dishFlavorMapper.saveBatch (flavors);


        }
    }

    @Override
    public PageResult page (DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage (dishPageQueryDTO.getPage (),
                dishPageQueryDTO.getPageSize ());
        Page<DishVO> dishPage=dishMapper.page(dishPageQueryDTO);
        return new PageResult (dishPage.getTotal (),dishPage.getResult ());

    }

    @Override
    @Transactional
    public void deleteBatch (List<Long> ids) {
        /*首先确定该菜品是否是在售状态,在售无法删除
        * */
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus ()==1){
                throw new DeletionNotAllowedException (MessageConstant.DISH_ON_SALE);
            }
        }

        /*其次确定是否在套餐中,如果在套餐中也无法删除
        * */
        List<Long> setmealIds=setmealMapper.getSetMealByIds(ids);
        if(setmealIds==null &&setmealIds.size ()>0 )
        {
            throw new DeletionNotAllowedException (MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        /*确认可以删除后删除
        * */
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishID(id);
        }


    }

    /**
     * 根据id查询菜品,包括口味
     * @param id
     * @return
     */
    @Override
    public DishVO getById (Long id) {
        /**
         * 将菜品查询并且放入vo中
         */
        Dish dish=dishMapper.getById (id);
        DishVO dishVO=new DishVO ();
        BeanUtils.copyProperties (dish,dishVO);

        List<DishFlavor> flavors=dishFlavorMapper.geyById(id);

        dishVO.setFlavors (flavors);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void updateWithFlavors (DishDTO dishDTO) {
        Dish dish=new Dish ();
        BeanUtils.copyProperties (dishDTO,dish);
        dishMapper.update(dish);

        //先删除再添加,达到覆盖的目的
        dishFlavorMapper.deleteByDishID (dish.getId ());
        List<DishFlavor> flavors = dishDTO.getFlavors ();
        if (flavors != null && flavors.size () > 0) {
            flavors.forEach (dishFlavor -> {
                dishFlavor.setDishId (dish.getId ());
            });
            dishFlavorMapper.saveBatch (flavors);
        }
    }

    @Override
    public List<Dish> getByCategoryId (Long id) {
        List<Dish> dishList=dishMapper.getByCategoryId(id);
        return dishList;

    }
}
