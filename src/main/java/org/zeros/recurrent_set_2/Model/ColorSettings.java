package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import javafx.scene.paint.Color;
import lombok.*;
import org.zeros.recurrent_set_2.Util.ColorConverter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ColorSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NonNull
    @NotBlank
    @Builder.Default
    private String name="X1";

    @NonNull
    @Builder.Default
    private Boolean areDefaultSettings = false;

    @NonNull
    @Builder.Default
    @Convert(converter = ColorConverter.class)
    private Color backgroundColor = Color.color(1, 0.95, 1);

    @NonNull
    @Builder.Default
    @Convert(converter = ColorConverter.class)
    private Color includedElementsColor = Color.BLACK;

    @NonNull
    @Builder.Default
    @Convert(converter = ColorConverter.class)
    private Color boundaryGradientStartColor = Color.YELLOW;

    @NonNull
    @Builder.Default
    @Convert(converter = ColorConverter.class)
    private Color BoundaryGradientEndColor = Color.web("#243253");

    @NonNull
    @Builder.Default
    @Max(Integer.MAX_VALUE)
    @Min(1)
    private Integer minIterationsSatisfiedToBeVisible = 1;

    @NonNull
    @Builder.Default
    private Boolean fadeOut = Boolean.TRUE;

    @Transient
    public static final ColorSettings DEFAULT = ColorSettings.builder()
            .areDefaultSettings(true)
            .name("DEFAULT")
            .build();

}
