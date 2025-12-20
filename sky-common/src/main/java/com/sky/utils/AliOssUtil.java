package com.sky.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com. aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth. CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss. common.comm.SignVersion;
import com. aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String bucketName;
    private String region;




    /**
     * 文件上传（纯环境变量方式）
     */
    public String upload(byte[] bytes, String objectName) throws com.aliyuncs.exceptions.ClientException {

        // 完全按照官方教程：从环境变量读取
        CredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建客户端配置
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 创建OSSClient实例
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    objectName,
                    new ByteArrayInputStream(bytes)
            );

            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("文件上传成功, ETag: {}", result.getETag());

        } catch (OSSException oe) {
            log.error("OSS服务异常:  {}", oe.getErrorMessage());
            throw new RuntimeException("文件上传失败: " + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("OSS客户端异常: {}", ce.getMessage());
            throw new RuntimeException("文件上传失败: " + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        String fileUrl = buildFileUrl(objectName);
        log.info("文件上传到: {}", fileUrl);
        return fileUrl;
    }

    private String buildFileUrl(String objectName) {
        return "https://" + bucketName + "." + endpoint + "/" + objectName;
    }
}