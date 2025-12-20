package com.sky.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 22:37
 *@Param:
 *@Return:
 *@Description:
 **/

@RestController
@Slf4j
@RequestMapping("/admin/common/upload")
public class CommonController {
    @Autowired
    public AliOssUtil aliOssUtil;

    @PostMapping
    public Result<String> upload(MultipartFile file){
        String originName= file.getOriginalFilename ();
        String extension = originName.substring (originName.lastIndexOf ("."));
        String fullname = UUID.randomUUID () + extension;

        String url = null;
        try {
            url = aliOssUtil.upload (file.getBytes (), fullname);
            return Result.success (url);
        }
         catch (Exception e) {
             log.error("文件上传失败：{}", e);
        }

        return Result.error (MessageConstant.UPLOAD_FAILED);

    }
}
