package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSettings {
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @Builder.Default
    private Integer iterationsMin = 350;
    @NonNull
    @Builder.Default
    private Integer iterationsMax = 3000;
    @NonNull
    @Builder.Default
    private Integer minIterationsSatisfiedToBeVisible = 1;
    @NonNull
    @Builder.Default
    private Integer exportHeight = 1795;
    @NonNull
    @Builder.Default
    private Integer exportWidth = 2551;
    @NonNull
    @Builder.Default
    private Integer numberOfThreads = 12;
    @NonNull
    @Builder.Default
    private Integer minChunkBorderSize =30;
    @NonNull
    @Builder.Default
    private Integer maxChunkBorderSize =400;
    @NonNull
    @Builder.Default
    private Double defaultRescaleOnScroll=1.3;


    @Transient
    public static final ApplicationSettings DEFAULT = ApplicationSettings.builder()
            .name("DEFAULT")
            .build();

    @Transient
    public static final double MAXIMAL_EXPRESSION_VALUE =2;

}
