package com.bw.movie.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具类
 */
public class QRcodeUtil {

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @param filePath
     * @param format
     */
    public static void createQRcode(String content, int width, int height, String filePath, String format) {
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            File outputFile = new File(filePath);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取二维码图片内容
     *
     * @param path
     * @return
     */
    public static String readQRcode(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            String res = result.getText();
            if (res == null || "".equals(res)) {
                return "内容为空";
            }
            return res;
        } catch (Exception e) {
            return "读取二维码图片异常";
        }
    }

    /**
     * 将BufferedImage持有的图像转化为指定图像格式的字节数组。
     *
     * @param bufferedImage 图像。
     * @param formatName    图像格式名称。
     * @return 指定图像格式的字节数组。
     * @throws IOException IO异常。
     */
    private static byte[] imageToByte(final BufferedImage bufferedImage, final String formatName) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, formatName, output);
        try {
            return output.toByteArray();
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

}
