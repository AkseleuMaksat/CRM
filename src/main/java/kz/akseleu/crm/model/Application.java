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
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "commentary")
    private String commentary;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "handled")
    private boolean handled;
}
