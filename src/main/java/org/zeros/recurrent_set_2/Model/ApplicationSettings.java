package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

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
    private Integer iterations = 1000;
    @NonNull
    @Builder.Default
    private Integer iterationsPreView = 100;
    @NonNull
    @Builder.Default
    private Integer minIterationsSatisfiedToBeVisible = 10;
    @NonNull
    @Builder.Default
    private Integer exportHeight = 1795;
    @NonNull
    @Builder.Default
    private Integer exportWidth = 2551;
    @NonNull
    @Builder.Default
    private Integer numberOfThreads = 24;
    @NonNull
    @Builder.Default
    private Integer smallestChunkBorderSize = 50;


    @Transient
    public static final ApplicationSettings DEFAULT = ApplicationSettings.builder()
            .name("DEFAULT")
            .build();

}
