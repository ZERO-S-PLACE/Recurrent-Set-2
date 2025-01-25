package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;

@Builder
@Getter
@Setter
public class ViewLocation {
    @NonNull
    @Builder.Default
    private Complex centerPoint=Complex.ZERO;
    @NonNull
    @Builder.Default
    private Double horizontalSpan=2.0;

    @Transient
    public static ViewLocation DEFAULT = ViewLocation.builder()
            .centerPoint(new Complex(-0.6,0))
            .horizontalSpan(3.6)
            .build();

}
