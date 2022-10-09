package top.cerbur.graduation.framework.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author cerbur
 */
@Component
@Slf4j
public class OcrClient {


    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    Gson gson = new Gson();
    private String url;

    private String compress;

    @Value("${ocr.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${ocr.compress}")
    public void setCompress(String compress) {
        this.compress = compress;
    }

    public CompletableFuture<HttpResponse<String>> sendAsync(String base64) {
        Map<Object, Object> values = new HashMap<>(3);
        values.put("is_draw", "0");
        values.put("compress", this.compress);
        values.put("img", base64);
        HttpRequest request = HttpRequest.newBuilder(URI.create(this.url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(values))
                .build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        return httpClient.sendAsync(request, bodyHandler);
    }

    public List<String> getResult(HttpResponse<String> stringHttpResponse) {
        String s = StringEscapeUtils.unescapeJava(stringHttpResponse.body());
        OcrResult ocrResult = null;
        try {
            ocrResult = objectMapper.readValue(s, OcrResult.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        if (ocrResult == null || ocrResult.getCode() == null) {
            return new ArrayList<>();
        }
        if (ocrResult.getCode() != 200) {
            return new ArrayList<>();
        }
        List<OcrMap> ocrMapList = ocrResult.getOcrMapList();
        List<String> res = new ArrayList<>();
        ocrMapList.forEach(ocrMap -> {
            Double confidence = ocrMap.getConfidence();
            if (confidence.compareTo(0.9) > 0) {
                res.add(ocrMap.getValue());
            }
        });
        return res;
    }

    private HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder);
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
