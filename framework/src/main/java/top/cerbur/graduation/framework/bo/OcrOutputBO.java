package top.cerbur.graduation.framework.bo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OcrOutputBO {
    private String url;
    private String schoolId;
    private String name;
}
