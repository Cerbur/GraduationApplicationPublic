package top.cerbur.graduation.framework.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostReviewBO {
    private Integer id;
    private Integer infoId;
    private Integer postUser;
    private String postNickName;
    private Integer replyUser;
    private String replyNickName;
    private String avatar;
    private Integer deleteStatus;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
