package top.cerbur.graduation.framework.result;

public class Return {
    public static <T> Result<T> success(T data){
        return new Result<>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success() {
        return new Result<>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> error(Status status) {
        return new Result<>(status.getCode(), status.getMsg(), null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
