package top.cerbur.graduation.wechatapi.controller.advice;

import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.cerbur.graduation.framework.result.Result;
import top.cerbur.graduation.framework.result.Return;
import top.cerbur.graduation.framework.result.Status;
import top.cerbur.graduation.wechatapi.exception.*;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Status> getConstraintViolationException(ConstraintViolationException e) {
        log.info(e.getMessage());
        return Return.error(Status.REQUEST_PARAM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Status> getMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info(e.getMessage());
        return Return.error(Status.REQUEST_PARAM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(WxErrorException.class)
    public Result<Status> getConstraintViolationException(WxErrorException e) {
        log.error(e.getMessage());
        return Return.error(Status.LOGIN_ERROR);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result<Status> getMethodArgumentTypeMismatchException(UnsupportedEncodingException e) {
        log.error(e.getMessage(), e);
        return Return.error(Status.JWT_NEW_FAILED);
    }

    @ExceptionHandler(NeedAuthorizeException.class)
    public Result<Status> getNeedAuthorizeException(NeedAuthorizeException e) {
        log.info(e.getMessage());
        return Return.error(Status.JWT_ERROR);
    }


    @ExceptionHandler(QiniuException.class)
    public Result<Status> getQiniuException(QiniuException e) {
        log.info(e.getMessage());
        return Return.error(Status.UPLOAD_FILE_FAILED);
    }

    @ExceptionHandler(CodeTokenException.class)
    public Result<Status> getCodeTokenException(CodeTokenException e) {
        log.info(e.getMessage());
        return Return.error(Status.CODE_MISS_ERROR);
    }

    @ExceptionHandler(CodeTokenNoUseException.class)
    public Result<Status> getCodeTokenNoUseException(CodeTokenNoUseException e) {
        log.info(e.getMessage());
        return Return.error(Status.CODE_WAIT_SCAN_ERROR);
    }

    @ExceptionHandler(CodeTokenIsUseException.class)
    public Result<Status> getCodeCodeTokenIsUseException(CodeTokenIsUseException e) {
        log.info(e.getMessage());
        return Return.error(Status.CODE_USE_ERROR);
    }

    @ExceptionHandler(CodeTokenNoSureException.class)
    public Result<Status> getCodeTokenNoSureException(CodeTokenNoSureException e) {
        log.info(e.getMessage());
        return Return.error(Status.CODE_WAIT_SURE_ERROR);
    }
}
