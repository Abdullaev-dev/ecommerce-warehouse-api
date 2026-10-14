package az.ingress.e_commerce.service;

import az.ingress.e_commerce.dto.OrderItemRequest;
import az.ingress.e_commerce.dto.OrderRequest;
import az.ingress.e_commerce.dto.OrderResponse;
import az.ingress.e_commerce.entity.Order;
import az.ingress.e_commerce.entity.Product;
import az.ingress.e_commerce.entity.User;
import az.ingress.e_commerce.exception.InsufficientStockException;
import az.ingress.e_commerce.exception.ResourceNotFoundException;
import az.ingress.e_commerce.repository.OrderItemRepository;
import az.ingress.e_commerce.repository.OrderRepository;
import az.ingress.e_commerce.repository.ProductRepository;
import az.ingress.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_whenStockIsInsufficient_throwsInsufficientStockException() {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("1200.00"));
        product.setStockQuantity(1);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(5);

        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setItems(List.of(itemRequest));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class,
                () -> orderService.createOrder(request));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_whenUserNotFound_throwUserNotFoundException() {
        OrderRequest request = new OrderRequest();
        request.setUserId(99L);
        request.setItems(List.of());

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(request));

        verify(productRepository, never()).findById(any());

    }

    @Test
    void createOrder_whenStockIsSufficient_createsOrderAndReducesOrder() {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("1200.00"));
        product.setStockQuantity(10);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setItems(List.of(itemRequest));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        OrderResponse response = orderService.createOrder(request);

        assertEquals(0, new BigDecimal("2400.00").compareTo(response.getTotalPrice()));
    }
}
