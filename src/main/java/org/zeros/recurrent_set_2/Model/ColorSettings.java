package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import javafx.scene.paint.Color;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorSettings {

    @Transient
    public static final ColorSettings DEFAULT = ColorSettings.builder()
            .name("DEFAULT")
            .build();
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @Builder.Default
    private Color backgroundColor = Color.color(1, 0.95, 1);
    @NonNull
    @Builder.Default
    private Color includedElementsColor = Color.BLACK;
    @NonNull
    @Builder.Default
    private Color boundaryGradientStartColor = Color.YELLOW;
    @NonNull
    @Builder.Default
    private Color BoundaryGradientEndColor = Color.web("#243253");
    @NonNull
    @Builder.Default
    private Boolean fadeOut = Boolean.TRUE;


}
