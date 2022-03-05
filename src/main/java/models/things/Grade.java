package models.things;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cardId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;

    private Double grade;
}
