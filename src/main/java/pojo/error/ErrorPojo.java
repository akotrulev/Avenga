package pojo.error;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErrorPojo {
    private String type;
    private String title;
    private int status;
    private String traceId;
    private ValidationErrorPojo errors;
}
