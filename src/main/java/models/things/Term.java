package models.things;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@NamedQuery(
        name = "getLastTerm",
        query = "select max(term) from Term"
)
public class Term {
    @Id
    private Integer id;
    private Integer term;
    @OneToMany(mappedBy = "term")
    private Set<Course> courses;
}
