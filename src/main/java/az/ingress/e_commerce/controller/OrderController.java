package az.ingress.e_commerce.controller;

import az.ingress.e_commerce.dto.OrderRequest;
import az.ingress.e_commerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest request){
        return ResponseEntity.ok().body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
        public ResponseEntity<?> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }
}
