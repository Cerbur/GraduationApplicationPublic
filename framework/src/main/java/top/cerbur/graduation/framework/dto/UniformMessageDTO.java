package top.cerbur.graduation.framework.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UniformMessageDTO {
    private Integer lostId;
    private String openId;
    private String schoolId;
    private String lostName;
    private String location;
    private String description;

}
