package top.cerbur.graduation.framework.entity;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private Integer id;
    private Integer role;
    private String openId;
    private String name;
    private String nickName;
    private String schoolId;
    private String className;
    private String avatar;
    private Timestamp createTime;
    private Timestamp updateTime;
}
