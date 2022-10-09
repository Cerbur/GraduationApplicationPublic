package top.cerbur.graduation.framework.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostSearchOutputBO {
    private Integer id;
    private String title;
    private Integer postUserId;
    private String postNickName;
    private String avatar;
    private Integer lostType;
    private String lostTypeName;
    private String schoolId;
    private String lostName;
    private String description;
    private String image;
    private String location;
    private Integer review;
    private Integer foundStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
