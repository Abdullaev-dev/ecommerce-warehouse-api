package az.ingress.e_commerce.service;

import az.ingress.e_commerce.dto.OrderItemRequest;
import az.ingress.e_commerce.dto.OrderItemResponse;
import az.ingress.e_commerce.dto.OrderRequest;
import az.ingress.e_commerce.dto.OrderResponse;
import az.ingress.e_commerce.entity.Order;
import az.ingress.e_commerce.entity.OrderItem;
import az.ingress.e_commerce.entity.Product;
import az.ingress.e_commerce.entity.User;
import az.ingress.e_commerce.enums.OrderStatus;
import az.ingress.e_commerce.exception.InsufficientStockException;
import az.ingress.e_commerce.exception.ResourceNotFoundException;
import az.ingress.e_commerce.repository.OrderItemRepository;
import az.ingress.e_commerce.repository.OrderRepository;
import az.ingress.e_commerce.repository.ProductRepository;
import az.ingress.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: "
                        + product.getName());
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());

            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);

            orderItems.add(orderItem);
        }

        Order order = new Order();

        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        for(OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                ))

        .toList();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderDate(),
                savedOrder.getStatus(),
                savedOrder.getTotalPrice(),
                itemResponses
        );
    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalPrice(),
                itemResponses
        );
    }

    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

                    List<OrderItemResponse> itemResponses = orderItems.stream()
                            .map(item -> new OrderItemResponse(
                                    item.getProduct().getName(),
                                    item.getQuantity(),
                                    item.getPriceAtPurchase()
                            ))
                            .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getOrderDate(),
                            order.getStatus(),
                            order.getTotalPrice(),
                            itemResponses
                    );
                })
                .toList();

    }

}
