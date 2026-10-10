package az.ingress.e_commerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = " User ID cannot be null ")
    private Long userId;

    @NotEmpty(message = " Order must be contain at least one item ")
    @Valid
    private List<OrderItemRequest> items;
}
