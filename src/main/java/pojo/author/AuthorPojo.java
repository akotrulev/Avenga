package pojo.author;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorPojo {
    private int id;
    private int idBook;
    private String firstName;
    private String lastName;
}
