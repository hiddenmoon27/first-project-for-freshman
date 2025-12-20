package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 22:08
 *@Param:
 *@Return:
 *@Description:
 **/
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties props) {
        // 把 props 的值传给 AliOssUtil
        log.info ("开始创建阿里云工具类对象{}",props);
        return new AliOssUtil(
                props.getEndpoint(),
                props.getBucketName(),
                props.getRegion()
        );
    }
}
