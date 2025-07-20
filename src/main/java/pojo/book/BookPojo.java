package pojo.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPojo {
    private int id;
    private String title;
    private String description;
    private int pageCount;
    private String excerpt;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH-mm-ssZ")
    // Ideally this will be ZonedDateTime, however in the GET response, we get a ZonedDateTime, while in the POST it uses LocalDateTime
    private String publishDate;
}
