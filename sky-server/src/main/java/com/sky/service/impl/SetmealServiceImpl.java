package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/22 17:38
 *@Param:
 *@Return:
 *@Description:
 **/
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    public SetmealMapper setmealMapper;
    @Autowired
    public SetmealDishMapper setmealDishMapper;
    @Autowired
    public DishMapper dishMapper;
    @Override
    public void insert (SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal ();
        BeanUtils.copyProperties (setmealDTO,setmeal);

        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishList= setmealDTO.getSetmealDishes ();
        setmealDishMapper.saveBatch(setmealDishList);


    }

    @Override
    public SetmealVO getById (Long id) {
        Setmeal setmeal=setmealMapper.getSetMealById (id);
        SetmealVO setmealVO=new SetmealVO ();
        BeanUtils.copyProperties (setmeal,setmealVO);

        List<SetmealDish> setmealDishes =setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes (setmealDishes);
        return setmealVO;
    }

    @Override
    public void delete (List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getSetMealById(id);
            if(StatusConstant.ENABLE == setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException (MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.delete(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.delete(setmealId);
        });
    }

    @Override
    public void status (Integer status, Long id) {
    /*起售套餐时要看菜品是否全部起售,若有停售菜品则无法起售
    * */
    if(status == StatusConstant.ENABLE){
        //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?

        List<Dish> dishList = dishMapper.getBySetmealId(id);
        if(dishList != null && dishList.size() > 0){
            dishList.forEach(dish -> {
                if(StatusConstant.DISABLE == dish.getStatus()){
                    throw new SetmealEnableFailedException (MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
    }

    Setmeal setmeal = Setmeal.builder()
            .id(id)
            .status(status)
            .build();
    setmealMapper.update(setmeal);

    }

    @Override
    public PageResult page (SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void update (SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //1、修改套餐表，执行update
        setmealMapper.update(setmeal);

        //套餐id
        Long setmealId = setmealDTO.getId();

        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.delete(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        setmealDishMapper.saveBatch(setmealDishes);
    }
}
