package top.cerbur.graduation.framework.bo;

import lombok.*;

/**
 * @author cerbur
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostLocationBO {
    private Integer id;
    private String name;
    private String description;
}
