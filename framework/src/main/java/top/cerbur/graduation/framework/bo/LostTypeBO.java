package top.cerbur.graduation.framework.bo;

import lombok.*;

/**
 * @author cerbur
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostTypeBO {
    private Integer id;
    private String name;
}
