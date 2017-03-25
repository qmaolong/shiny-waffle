package com.covilla.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
public class QrCodeUtil {
    /**
     * 生成二维码文件
     * @param content
     * @return
     */
    public static String generateCodeToFile(String name, String path, String content){
        try {
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
            File file1 = new File(path, name + ".jpg");
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
            return "/asset/bk/img/tmp/" + name + ".jpg";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateCodeToString(String content, String type){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
            MatrixToImageWriter.writeToStream(bitMatrix, type, out);

            BASE64Encoder encoder = new BASE64Encoder();
            return "data:image/png;base64," + encoder.encode(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
