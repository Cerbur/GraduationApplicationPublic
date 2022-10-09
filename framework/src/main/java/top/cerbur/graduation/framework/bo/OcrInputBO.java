package top.cerbur.graduation.framework.bo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OcrInputBO {
    private String base64;
    private Boolean hasOcr;
}
