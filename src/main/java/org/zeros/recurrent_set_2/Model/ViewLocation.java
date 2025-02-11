package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ViewLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NonNull
    @NotBlank
    @Builder.Default
    private String name="X1";
    @NonNull
    @Builder.Default
    private Complex centerPoint=Complex.ZERO;
    @NonNull
    @Builder.Default
    private Double horizontalSpan=2.0;


    @Transient
    @NonNull
    @Builder.Default
    private Double referenceScale=1.0;
    @Transient
    public static ViewLocation DEFAULT = ViewLocation.builder()
            .centerPoint(new Complex(-0.6,0))
            .horizontalSpan(3.6)
            .build();

}
