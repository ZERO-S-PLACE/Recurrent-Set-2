package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Max(Integer.MAX_VALUE/2)
    @Min(1)
    private Integer iterationsMin = 350;
    @NonNull
    @Builder.Default
    @Max(Integer.MAX_VALUE/2)
    @Min(1)
    private Integer iterationsMax = 3000;
    @NonNull
    @Builder.Default
    @Max(Integer.MAX_VALUE/2)
    @Min(1)
    private Integer iterationsPreview =75;
    @NonNull
    @Builder.Default
    @Max(Integer.MAX_VALUE/2)
    @Min(1)
    private Integer iterationsExport =50;
    @NonNull
    @Builder.Default
    @Max(20000)
    @Min(100)
    private Integer exportHeight =2000;
    @NonNull
    @Builder.Default
    @Max(20000)
    @Min(100)
    private Integer exportWidth = 3000;
    @NonNull
    @Builder.Default
    @Max(Integer.MAX_VALUE/2)
    @Min(1)
    private Integer numberOfThreads = Runtime.getRuntime().availableProcessors();
    @NonNull
    @Builder.Default
    @Max(500)
    @Min(10)
    private Integer minChunkBorderSize =25;
    @NonNull
    @Builder.Default
    @Max(2000)
    @Min(50)
    private Integer maxChunkBorderSize =500;
    @NonNull
    @Builder.Default
    @Max(10)
    @Min(1)
    private Double defaultRescaleOnScroll=1.3;


    @Transient
    public static final ApplicationSettings DEFAULT = ApplicationSettings.builder()
            .name("DEFAULT")
            .build();

    @Transient
    public static final double MAXIMAL_EXPRESSION_VALUE =2;
    @Transient
    public static final double IMAGE_GENERATION_PROPERTIES_REFRESH_FREQUENCY=100;



}
