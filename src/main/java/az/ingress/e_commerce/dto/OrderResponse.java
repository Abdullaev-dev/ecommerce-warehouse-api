package az.ingress.e_commerce.dto;

import az.ingress.e_commerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private List<OrderItemResponse> items;
}
