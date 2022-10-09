package top.cerbur.graduation.framework.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Status {

    REQUESTERROREXCEPTION(-400, "请求方法不支持"),


    REQUEST_PARAM_ERROR(-401, "参数错误"),
    JWT_ERROR(-402, "jwt失效"),

    CODE_USE_ERROR(-404, "二维码已被扫描"),

    CODE_MISS_ERROR(-403, "二维码失效"),

    CODE_WAIT_SURE_ERROR(-405, "二维码等待确认"),

    CODE_WAIT_SCAN_ERROR(-406, "二维码等待被扫描"),
    LOGIN_ERROR(-501, "微信登录请重试失败"),
    JWT_NEW_FAILED(-502, "jwt 生成失败请重试"),

    UPLOAD_FILE_FAILED(-503, "文件上传失败"),

    // 成功
    SUCCESS(1, "成功");

    private final int code;
    private final String msg;

}