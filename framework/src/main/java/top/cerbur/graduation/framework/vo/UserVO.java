package top.cerbur.graduation.framework.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserVO {
    private Integer id;
    private Integer role;
    private String openId;
    private String name;
    private String nickName;
    private String schoolId;
    private String className;
    private String avatar;
    private String token;
}
