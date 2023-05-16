package kh.cn.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/5 5:32 PM
 */
public class QRCodeUtil {
    /**
     * 生成二维码图片
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @param format  二维码格式
     * @return 二维码图片路径
     * @throws Exception 异常信息
     */
    public static String createQRCode(String content, int width, int height, String format) throws Exception {
        // 定义二维码参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 生成二维码
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        // 保存二维码图片
        Path path = Paths.get("qrcode." + format);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        return path.toString();
    }



    public static void main(String[] args) {
        try {
            String content = "http://www.88loveyou.com"; // 二维码内容
            int width = 300; // 二维码宽度
            int height = 300; // 二维码高度
            String format = "png"; // 二维码格式
            String imageUrl = QRCodeUtil.createQRCode(content, width, height, format); // 生成二维码图片并获取保存路径
            System.out.println("二维码图片路径：" + imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
