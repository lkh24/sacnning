package kh.cn.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import kh.cn.domin.User;
import kh.cn.util.CacheUtil;
import kh.cn.util.MinioUtil;
import kh.cn.util.config.MinioConfig;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/5 3:35 PM
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CheckInController {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private CacheUtil cacheUtil;

//    @CrossOrigin // 允许跨域访问
//    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateQRCode() {
        // 生成二维码图片
        String qrCodeUrl = "";
        try {
            // 生成二维码图片，并保存到本地
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode("88loveyou.com", BarcodeFormat.QR_CODE, 200, 200);

//            转化对象为BufferedImage
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
//           将image对象写入到ByteArrayOutputStream中
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png",os);
//            讲写入内存的对象写出到ByteArrayInputStream中
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//            最后调用上传图片的方法上传文件
            Boolean upload = minioUtil.upload(is, "fineName", minioConfig.getBucketName());
            if (upload) {
                qrCodeUrl =  minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + "fineName";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("qrCode", qrCodeUrl);
        return ResponseEntity.ok(result);
    }

//    前端轮询接口：http://localhost:63342/api/confirm
    @PostMapping("/confirm")
    @ResponseBody
    public Map<String, Object> verifySucceed(@RequestParam("qrCodeStatus") String qrCodeStatus) {

        return new HashMap<>();
    }

    @CrossOrigin // 允许跨域访问
    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String content, @RequestParam int width, @RequestParam int height) throws Exception {
        // 加载二维码参数
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix qrMatrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

        // 将二维码渲染为图片
        int qrWidth = qrMatrix.getWidth();
        int qrHeight = qrMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < qrWidth; x++) {
            for (int y = 0; y < qrHeight; y++) {
                qrImage.setRGB(x, y, qrMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 将图片转换为字节数组
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", out);
        byte[] imgBytes = out.toByteArray();

        // 返回响应
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.OK);
    }

    /**
     * 这里可以接受用户的请求
     * @param user 用户body
     * @return 返回提示
     */
    @PostMapping
    public ResponseEntity<?> successCheckIn(@RequestBody User user){
        HashMap<String, Object> response = new HashMap<String, Object>() {};
//        请求成功
        response.put("success", false);
//        判断是否符合现在的字符串
        if (cacheUtil.get("sj").equals(user.getSjToken())){
//            TODO: 修改用户的签到状态逻辑
            response.put("message","true");
        }else{
//            TODO: 返回操作失败提示
            response.put("message","false");
        }
        // 设置返回状态码
        HttpStatus status = response.get("message").equals(true) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseEntity.status(status);

        return ResponseEntity.ok(response);
    }


}


