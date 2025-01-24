package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

        @NonNull
        @NotBlank
        private String name;
        @NonNull
        @Builder.Default
        private Color backgroundColor=Color.web("#243253");
    @NonNull
    @Builder.Default
    private Color includedElementsColor=Color.BLACK;
    @NonNull
    @Builder.Default
    private Color boundaryGradientStartColor=Color.YELLOW;
    @NonNull
    @Builder.Default
    private Color BoundaryGradientEndColor=Color.web("#243253");
    @NonNull
    @Builder.Default
    private Boolean fadeOut=Boolean.TRUE;



    @Transient
    public static final ColorSettings DEFAULT = ColorSettings.builder()
            .name("DEFAULT")
            .build();



}
