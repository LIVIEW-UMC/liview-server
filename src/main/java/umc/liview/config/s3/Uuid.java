package umc.liview.config.s3;

import jakarta.persistence.*;
import lombok.*;
import umc.liview.common.basetime.BaseTimeEntity;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Uuid extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(unique = true)
    private String uuid;

}



