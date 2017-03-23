package com.covilla.controller.api;

import com.covilla.service.ServiceException;
import com.covilla.util.FileUploader;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qmaolong on 2016/9/18.
 */
@Controller
@RequestMapping("upload")
public class UploadController {
    public static AtomicInteger ATOM = new AtomicInteger(1);

    @RequestMapping(value = "/imgUpload")
    @ResponseBody
    public ResultMsg imgUpload(@RequestParam(value = "file", required = false) MultipartFile file) {
        if (file.getSize() > 1048576){
            throw new ServiceException("图片大小不能超过1M");
        }
        //判断格式
        String[] supportImgs = {".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG"};
        String format = null;
        for (String support : supportImgs){
            if (file.getOriginalFilename().endsWith(support)){
                format = support;
                break;
            }
        }
        if (ValidatorUtil.isNull(format)){
            throw new ServiceException("图片格式不支持");
        }

        String imgUrl = FileUploader.upload(file, FileUploader.FileTypeEnum.COMMON);
        return ResultMsg.buildSuccessMsg(imgUrl);
    }

}
