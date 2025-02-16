package org.zeros.recurrent_set_2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecurrentExpression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NonNull
    @NotBlank
    @Builder.Default
    private String name = "Recurrent Expression";
    @NonNull
    @NotBlank
    private String firstExpression;
    @NonNull
    @NotBlank
    private String recurrentExpression;
    @NonNull
    @NotBlank
    @Builder.Default
    private String positionVariableName = "p";
    @NonNull
    @NotBlank
    @Builder.Default
    private String recurrentVariableName = "z";
    @NonNull
    @Builder.Default
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private ViewLocation defaultViewLocation = ViewLocation.builder().build();
    @NonNull
    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(name = "expression_locations", joinColumns = @JoinColumn(name = "expression_id"), inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<ViewLocation> savedViewLocations = new HashSet<>();

    @Transient
    public static RecurrentExpression MANDELBROT = RecurrentExpression.builder()
            .name("Mandelbrot")
            .firstExpression("p")
            .recurrentExpression("z^2+p")
            .build();
    @Transient
    public static RecurrentExpression JULIA_SET = RecurrentExpression.builder()
            .name("Julia Set (-0.10+0.65i)")
            .firstExpression("p")
            .recurrentExpression("z^2+(-0.10+0.65i)")
            .build();
    @Transient
    public static RecurrentExpression DEVIL_SHAPE = RecurrentExpression.builder()
            .name("Devil Shape")
            .firstExpression("p")
            .recurrentExpression("z^7-(z^6)/3-z^2+p^z")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X_SHAPE = RecurrentExpression.builder()
            .name("X Shape")
            .firstExpression("p")
            .recurrentExpression("z^(sin(p^z))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(15.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X1_SHAPE = RecurrentExpression.builder()
            .name("X1 Shape")
            .firstExpression("p")
            .recurrentExpression("z^(sin(p^cos(z)))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(15.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X2_SHAPE = RecurrentExpression.builder()
            .name("X2 Shape")
            .firstExpression("p")
            .recurrentExpression("(sin(z)+cos(z))/(sin(z)*cos(z))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.valueOf(0, 1))
                    .horizontalSpan(20.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X3_SHAPE = RecurrentExpression.builder()
            .name("X3 Shape")
            .firstExpression("p")
            .recurrentExpression("cos(sin(cos(sin(e))))*cos(sin(z))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X4_SHAPE = RecurrentExpression.builder()
            .name("X4 Shape")
            .firstExpression("p")
            .recurrentExpression("cos(z^p)+sin(z)+cos(p)")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X5_SHAPE = RecurrentExpression.builder()
            .name("X5 Shape")
            .firstExpression("p")
            .recurrentExpression("cos(pz^sin(pz))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X6_SHAPE = RecurrentExpression.builder()
            .name("X6 Shape")
            .firstExpression("p")
            .recurrentExpression("ctg(sin(p)^(z^sin(pz)))")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();
    @Transient
    public static RecurrentExpression X7_SHAPE = RecurrentExpression.builder()
            .name("X6 Shape")
            .firstExpression("0")
            .recurrentExpression("z^(psin(pz))-p^sin(pz)")
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X8_SHAPE = RecurrentExpression.builder()
            .name("X8 Shape")
            .firstExpression("p")
            .recurrentExpression("z^(1/p^(1/z^(1/p)))")
            //z^(1/p) also
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X9_SHAPE = RecurrentExpression.builder()
            .name("X9 Shape")
            .firstExpression("p")
            .recurrentExpression("z^(sin(7.31p))")
            //z^(sin(7.31p)) also
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression X10_SHAPE = RecurrentExpression.builder()
            .name("X9 Shape")
            .firstExpression("p")
            .recurrentExpression("zsin(z^z)^(psin(p^p))")
            //z^(sin(7.31p)) also
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    @Transient
    public static RecurrentExpression CITY_LIGHTS = RecurrentExpression.builder()
            .name("CITY_LIGHTS")
            .firstExpression("p")
            .recurrentExpression("tg(PI/(sin(zp)))")
            //z^(1/p) also
            .defaultViewLocation(ViewLocation.builder()
                    .centerPoint(Complex.ZERO)
                    .horizontalSpan(5.0)
                    .build())
            .build();

    public Set<String> getVariableNames() {
        return Set.of(positionVariableName, recurrentVariableName);
    }
}
