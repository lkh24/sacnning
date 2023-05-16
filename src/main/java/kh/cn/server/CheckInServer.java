package kh.cn.server;

import kh.cn.util.MinioUtil;
import kh.cn.util.config.MinioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/8 11:23 AM
 */
@Service
public class CheckInServer {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig minioConfig;


    /**
     * 根据传入的文件路径去返回生成的图片
     * （这个方法最后也没有使用，可以看controller中的实现，减少内存消耗）
     *
     * @param fileUrl 本地图片路径
     * @return 网络地址（如果没有成功那么就返回null）
     */
    public String uploadFileMinio(String fileUrl) {

        boolean flag = false;
        // 判断存储桶是否存在
        if (!minioUtil.bucketExists(minioConfig.getBucketName())) {
            minioUtil.makeBucket(minioConfig.getBucketName());
        }
        // 生成文件名（这里随意起名）
        String fineName = fileUrl.substring(fileUrl.length() - 8, fileUrl.length() - 4);
        try {
            File file = new File(fileUrl);
            InputStream inputStream = new FileInputStream(file);
            // 上传文件
            flag = minioUtil.upload(inputStream, fineName, minioConfig.getBucketName());
        } catch (Exception e) {
            return null;
        }
        // 判断是否上传成功，成功就返回url，不成功就返回null
        if (flag) {
            return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fineName;
        }
        return null;
    }


}
