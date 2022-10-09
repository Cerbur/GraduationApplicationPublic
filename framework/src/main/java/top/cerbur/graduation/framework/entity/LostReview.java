package top.cerbur.graduation.framework.entity;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostReview {
    private Integer id;
    private Integer infoId;
    private Integer postUser;
    private Integer replyUser;
    private String content;
    private Integer deleteStatus;
    private Timestamp createTime;
    private Timestamp updateTime;
}
