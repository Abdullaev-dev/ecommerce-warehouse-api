package az.ingress.e_commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = " Product ID cannot be null ")
    private Long productId;


    @NotNull(message = " Quantity cannot be null ")
    @Positive(message = " Quantity must be positive ")
    private Integer quantity;


}
