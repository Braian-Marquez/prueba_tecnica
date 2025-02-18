package com.tsg.commons.models.enums;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResponseDTO {

    private int httpStatusCode;
    private List<String> description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
    private LocalDateTime timestamp;


}
