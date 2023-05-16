package kh.cn.util;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/6 12:19 PM
 * minio存储工具
 */
@Component
public class MinioUtil {
    @Autowired
    private MinioClient minioClient;

    /**
     * 查询bucket中是否已经存在
     *
     * @param bucketName 桶名
     * @return 是否存在
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs
                    .builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @param bucket 桶名
     * @return 是否创建成功
     */
    public Boolean makeBucket(String bucket) {
        try {
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 文件上传
     * @param file 文件路径
     * @param fileName 存储的bucket
     * @param bucketName 文件名
     * @return 是否上传成功
     */
    public Boolean upload(InputStream file, String fileName, String bucketName){
        try {
            PutObjectArgs objectArgs = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fileName)
                    // 将分块大小设置为 10MB
                    .stream(file, -1, 10485760L)
                    .contentType("image/jpeg")
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
