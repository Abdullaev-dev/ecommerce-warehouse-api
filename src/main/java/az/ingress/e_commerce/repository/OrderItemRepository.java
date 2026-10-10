package az.ingress.e_commerce.repository;

import az.ingress.e_commerce.dto.OrderItemResponse;
import az.ingress.e_commerce.dto.OrderResponse;
import az.ingress.e_commerce.entity.Order;
import az.ingress.e_commerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

}
