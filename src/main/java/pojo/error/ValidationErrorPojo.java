package pojo.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Data
public class ValidationErrorPojo {
    @JsonProperty("$.pageCount")
    private ArrayList<String> pageCountErrors;
}
