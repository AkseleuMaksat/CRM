package kz.akseleu.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_name", nullable = false)
    String user_name;

    @Column(name = "course_id", nullable = false)
    int course_id;

    @Column(name = "commentary")
    String commentary;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "handled")
    boolean handled;

    @Transient
    private String course_name;
}
