package top.cerbur.graduation.wechatapi.controller;

import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.cerbur.graduation.framework.bo.OcrInputBO;
import top.cerbur.graduation.framework.bo.OcrOutputBO;
import top.cerbur.graduation.framework.result.Result;
import top.cerbur.graduation.framework.result.Return;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.ILostService;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping
@CrossOrigin
public class ImageController {

    private ILostService lostService;

    @Autowired
    public void setLostService(ILostService lostService) {
        this.lostService = lostService;
    }

    @PostMapping("/image")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<OcrOutputBO> uploadImage(
            @RequestParam(value = "image")
            String image,
            @RequestParam(value = "ocr", defaultValue = "false")
            Boolean ocr,
            @ModelAttribute UserVO userVO
    ) throws QiniuException, ExecutionException, InterruptedException {

        OcrInputBO build = OcrInputBO.builder().base64(image).hasOcr(ocr).build();
        OcrOutputBO ocrOutputBO = lostService.ocrImage(build);
        return Return.success(ocrOutputBO);
    }
}
