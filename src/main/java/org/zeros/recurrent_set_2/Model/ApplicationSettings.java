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
    private Integer iterations = 500;
    @NonNull
    @Builder.Default
    private Integer iterationsPreView = 100;
    @NonNull
    @Builder.Default
    private Integer minIterationsSatisfiedToBeVisible = 3;
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
    private Integer minChunkBorderSize =20;
    @NonNull
    @Builder.Default
    private Integer maxChunkBorderSize =300;
    @NonNull
    @Builder.Default
    private Double defaultRescaleOnScroll=1.5;


    @Transient
    public static final ApplicationSettings DEFAULT = ApplicationSettings.builder()
            .name("DEFAULT")
            .build();

    @Transient
    public static final double MAXIMAL_EXPRESSION_VALUE =2;

}
