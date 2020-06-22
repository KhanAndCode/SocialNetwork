package khanipov.andrew.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_additional_info")
public class AdditionalUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @JsonIgnore
    @OneToOne(mappedBy = "userInfo")
    private User user;
    private String country;
    private String city;
}
