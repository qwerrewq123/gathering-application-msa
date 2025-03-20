package gathering.msa.fcm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fcm_token")
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String tokenValue;
    @Column(nullable = false)
    private LocalDate expirationDate;
    @Column(name = "user_id")
    private Long userId;

    public void changeExpirationDate(int month){
        expirationDate = LocalDate.now().plusMonths(month);
    }
}
