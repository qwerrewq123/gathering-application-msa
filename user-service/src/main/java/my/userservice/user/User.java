package my.userservice.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Table(name="member")
@Builder
@AllArgsConstructor
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    @Column(name = "image_id")
    private Long imageId;


}
