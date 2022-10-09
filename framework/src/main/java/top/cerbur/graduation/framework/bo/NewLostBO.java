package top.cerbur.graduation.framework.bo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewLostBO {

    private String title;
    private Integer postUser;
    private Integer lostType;
    private String schoolId;
    private String lostName;
    private String description;
    private String image;
    private String location;

}
