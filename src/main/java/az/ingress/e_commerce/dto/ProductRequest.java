package az.ingress.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = " Product name cannot be blank")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be possitive")
    private BigDecimal price;

    @NotNull(message = " Stock quantity cannot be null")
    @Positive(message = " Stock quantity must be positive")
    private Integer stockQuantity;

    @NotNull(message = " Category ID cannot be null")
    private Long categoryId;


}
