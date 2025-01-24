package org.zeros.recurrent_set_2.Model;


import jakarta.persistence.Transient;
import lombok.*;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@Setter
@Builder
public class RecurrentExpression {
    @NonNull
    public String firstExpression;
    @NonNull
    public String recurrentExpression;
    @NonNull
    @Builder.Default
    public String positionVariableName ="p";
    @NonNull
    @Builder.Default
    public String recurrentVariableName ="z";
    @NonNull
    @Builder.Default
    private Double initialRangeMin = -2.6;
    @NonNull
    @Builder.Default
    private Double initialRangeMax = 2.0;

    public Set<String> getVariableNames(){
        return Set.of(positionVariableName, recurrentVariableName);
    }
    @Transient
    public static RecurrentExpression MANDELBROT =RecurrentExpression.builder()
            .firstExpression("p")
            .recurrentExpression("z^2+p")
            .build();
}
