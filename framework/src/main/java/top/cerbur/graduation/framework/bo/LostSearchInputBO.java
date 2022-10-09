package top.cerbur.graduation.framework.bo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostSearchInputBO {

    /**
     * 查询条件
     */
    private Integer id;
    private Integer userId;
    private String schoolId;
    private Integer lostType;
    private Integer foundStatus;
    private List<String> schoolIdList;
    private List<Integer> idList;
    private String keyword;


    /**
     * 与业务有关非对外查询
     */
    private Integer deleteStatus;

    /**
     * 与 SQL 无关
     */
    private String className;

}
