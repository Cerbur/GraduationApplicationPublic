package top.cerbur.graduation.framework.ocr;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OcrMap {
    @JsonProperty("value")
    private String value;
    @JsonProperty("confidence")
    private Double confidence;
}
