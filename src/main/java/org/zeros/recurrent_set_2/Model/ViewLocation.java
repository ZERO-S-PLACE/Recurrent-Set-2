package org.zeros.recurrent_set_2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import javafx.geometry.Point2D;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ViewLocation {

    @Transient
    public static ViewLocation DEFAULT = ViewLocation.builder()
            .centerPoint(new Complex(-0.6, 0))
            .horizontalSpan(3.6)
            .build();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NonNull
    @Builder.Default
    private String name = "";
    @NonNull
    @Builder.Default
    private Complex centerPoint = Complex.ZERO;
    @NonNull
    @Builder.Default
    @Min(0)
    private Double horizontalSpan = 2.0;
    @Transient
    @NonNull
    @Builder.Default
    @Min(0)
    private Double referenceScale = 1.0;

    public double getUnitsPerPixel(Point2D imageDimensions) {
        return horizontalSpan /imageDimensions.getX();
    }

    public Complex getPointOnSetCoordinate(Point2D imageDimensions,Point2D pointOnCanvas) {
        return getTopLeftPointCoordinate(imageDimensions).add(new Complex(pointOnCanvas.getX() * getUnitsPerPixel(imageDimensions),
                -pointOnCanvas.getY() * getUnitsPerPixel(imageDimensions)));
    }
    public Complex getTopLeftPointCoordinate(Point2D imageDimensions) {
        return centerPoint.add(new Complex(
                getUnitsPerPixel(imageDimensions) * ((double) -imageDimensions.getX() / 2),
                getUnitsPerPixel(imageDimensions) * ((double) imageDimensions.getY() / 2)));

    }

    public void applyOffset(Point2D imageDimensions,Point2D offset) {
        centerPoint=centerPoint.add(new Complex(-offset.getX() * getUnitsPerPixel(imageDimensions), offset.getY() * getUnitsPerPixel(imageDimensions)));
    }
}
