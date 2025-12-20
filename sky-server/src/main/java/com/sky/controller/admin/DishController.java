package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishFlavorMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 22:53
 *@Param:
 *@Return:
 *@Description:
 **/
@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    public DishService dishService;


    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.save(dishDTO);
        return Result.success ();
    }

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询,参数{}",dishPageQueryDTO);
        PageResult pageResult=dishService.page(dishPageQueryDTO);
        return Result.success (pageResult);
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info ("删除菜品,id为{}",ids);
        dishService.deleteBatch(ids);
        return Result.success ();
    }

}
