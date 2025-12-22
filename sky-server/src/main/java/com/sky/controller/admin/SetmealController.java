package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Author: 潜月
 *@Date: 2025/12/22 17:19
 *@Param:
 *@Return:
 *@Description:
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    public SetmealService setmealService;

    @PostMapping
    public Result insert(@RequestBody SetmealDTO setmealDTO){
        log.info("插入套餐{}",setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success ();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info ("根据id{}查询套餐",id);
        SetmealVO setmealVO=setmealService.getById(id);
        return Result.success (setmealVO);
    }

    @DeleteMapping
    public Result delete(List<Long> ids){
        log.info("删除id{}",ids);
        setmealService.delete(ids);
        return Result.success ();
    }

    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status,Long id){
        log.info ("将id{}设置为{}",id,status);
        setmealService.status(status,id);
        return Result.success ();
    }

    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询{}",setmealPageQueryDTO);
        PageResult pageResult=setmealService.page(setmealPageQueryDTO);
        return Result.success (pageResult);
    }

    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }
}
