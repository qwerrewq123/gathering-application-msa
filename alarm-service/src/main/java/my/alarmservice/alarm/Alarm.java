package my.alarmservice.alarm;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Setter
@AllArgsConstructor
@Builder
@Table(name = "alarm")
public class Alarm {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime date;
    private Boolean checked;
    @Column(name = "user_id")
    private Long userId;
}
