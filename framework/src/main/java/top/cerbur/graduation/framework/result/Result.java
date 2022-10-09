package top.cerbur.graduation.framework.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;
}
