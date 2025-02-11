package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @Builder.Default
    private Boolean areDefaultSettings = false;
    @NonNull
    @Builder.Default
    private Integer iterationsMin = 350;
    @NonNull
    @Builder.Default
    private Integer iterationsMax = 3000;
    @NonNull
    @Builder.Default
    private Integer iterationsPreview =75;
    @NonNull
    @Builder.Default
    private Integer minIterationsSatisfiedToBeVisible = 1;
    @NonNull
    @Builder.Default
    private Integer iterationsExport =5000;
    @NonNull
    @Builder.Default
    private Integer exportHeight = 1795;
    @NonNull
    @Builder.Default
    private Integer exportWidth = 2551;
    @NonNull
    @Builder.Default
    private Integer numberOfThreads = Runtime.getRuntime().availableProcessors();
    @NonNull
    @Builder.Default
    private Integer minChunkBorderSize =25;
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
