package com.covilla.util;

import com.covilla.common.Config;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件上传
 * Created by qmaolong on 2017/2/28.
 */
public class FileUploader {
    public static String PHYSICAL_PATH = Config.STATIC_LOCATION;
    public static AtomicInteger ATOM = new AtomicInteger(1);

    /**
     * 文件上传
     * @param file
     * @param relativePath
     * @param fileName
     * @return
     */
    public static String upload(MultipartFile file, String relativePath, String fileName){
        String path = PHYSICAL_PATH + relativePath;//绝对路径

        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relativePath + "/" + fileName;
    }

    public static String upload(MultipartFile file, FileTypeEnum fileType){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String relativePath = fileType.getDirect() + "/"
                + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)<9?"0":"")
                + (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH)<10?"0":"")
                + calendar.get(Calendar.DAY_OF_MONTH);
        String fileName = DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss")
                + String.valueOf(10000 + ATOM.getAndIncrement()%10000).substring(1)
                + "." + file.getOriginalFilename().split("\\.")[file.getOriginalFilename().split("\\.").length-1];

        return upload(file, relativePath, fileName);
    }

    /**
     * 删除文件
     * @param path
     */
    public static void delete(String path){
        File file = new File(PHYSICAL_PATH + path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 上传类型
     */
    public enum FileTypeEnum{
        COMMON("common", "通用"),
        CARD_COVER("card_cover", "会员卡封面"),
        ADVERTISE("advertise", "广告图片");
        private String desc;
        private String direct;

        FileTypeEnum(String direct, String desc) {
            this.desc = desc;
            this.direct = direct;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDirect() {
            return direct;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }
    }

}
