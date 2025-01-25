package org.zeros.recurrent_set_2.Model;


import jakarta.persistence.Transient;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@Setter
@Builder
public class RecurrentExpression {
    @NonNull
    private String firstExpression;
    @NonNull
    private String recurrentExpression;
    @NonNull
    @Builder.Default
    private String positionVariableName ="p";
    @NonNull
    @Builder.Default
    private String recurrentVariableName ="z";
    @NonNull
    @Builder.Default
    private ViewLocation defaultViewLocation=ViewLocation.DEFAULT;
    @NonNull
    @Builder.Default
    private Set<ViewLocation> savedViewLocations=Set.of(ViewLocation.DEFAULT);


    public Set<String> getVariableNames(){
        return Set.of(positionVariableName, recurrentVariableName);
    }
    @Transient
    public static RecurrentExpression MANDELBROT =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^2+p")
            .build();
    @Transient
    public static RecurrentExpression JULIA_SET =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^2+(-0.10+0.65i)")
            .build();
    @Transient
    public static RecurrentExpression DEVIL_SHAPE =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^7-(z^6)/3-z^2+p^z")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X_SHAPE =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^(sin(p^z))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(15.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X1_SHAPE =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^(sin(p^cos(z)))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(15.0)
                    .build())
            .build();
}
