package africa.semicolon.library.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@Entity
public class Member extends User{

    private Date dateOfMembership;
    private int totalBooksCheckedOut;
}
