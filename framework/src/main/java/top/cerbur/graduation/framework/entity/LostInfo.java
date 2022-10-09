package top.cerbur.graduation.framework.entity;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostInfo {
    private Integer id;
    private String title;
    private Integer postUser;
    private Integer foundStatus;
    private Integer lostType;
    private String schoolId;
    private String lostName;
    private String description;
    private String image;
    private String location;
    private Integer deleteStatus;
    private Timestamp createTime;
    private Timestamp updateTime;
}