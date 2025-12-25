package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 *@Author: 潜月
 *@Date: 2025/12/25 22:36
 *@Param:
 *@Return:
 *@Description:
 **/
@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api (tags = "商店相关接口")
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation ("设置店铺状态")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("将状态设置为{}",status==1?"开售":"停售");
        ValueOperations valueOperations = redisTemplate.opsForValue ();
        valueOperations.set (KEY,status);
        return Result.success ();
    }
    @GetMapping ("/status")
    @ApiOperation ("获取店铺状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

}
