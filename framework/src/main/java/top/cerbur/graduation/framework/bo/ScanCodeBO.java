package top.cerbur.graduation.framework.bo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScanCodeBO {
    private Boolean isScan;
    private Integer id;
    private String uaCode;
}
