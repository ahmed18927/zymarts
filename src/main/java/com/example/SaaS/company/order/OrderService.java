package com.example.SaaS.company.order;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.address.AddressRepository;
import com.example.SaaS.company.cart.repository.CartRepository;
import com.example.SaaS.company.order.dto.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;


    private static final List<String> ACTIVE_STATUSES_FOR_CANCEL = List.of("PENDING");

    // ---------- CREATE ----------
    public Order createOrder(Long customerId, Long companyId, CreateOrderRequest request) {

        Cart cart = cartRepository.findByCustomerAndStore(customerId)
                .orElseThrow(() -> new BusinessException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException("Address not found"));

        if (!address.getCustomerId().getId().equals(customerId)) {
            throw new BusinessException("Address does not belong to this customer");
        }

        Company company = companyRepository.getReferenceById(companyId);

        Order order = Order.builder()
                .customerId(cart.getCustomerId())
                .addressId(address)
                .companyId(company)
                .storeId(cart.getStoreId())
                .couponId(cart.getCouponId())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProductId();

            if (product.getStock() == null || product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("Insufficient stock for product id " + product.getId());
            }

            // decrement stock (reserve it for this order)
            product.setStock(product.getStock() - cartItem.getQuantity());

            BigDecimal price = product.getSellPrice();
            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .orderId(order)
                    .productId(product)
                    .price(price)
                    .quantity(cartItem.getQuantity())
                    .totalPrice(totalPrice)
                    .productData(toJson(Map.of(
                            "productId", product.getId(),
                            "sellPrice", price
                    )))
                    .build();

            orderItems.add(orderItem);
            subTotal = subTotal.add(totalPrice);
        }

        order.setItems(orderItems);

        BigDecimal discount = calculateCouponDiscount(cart.getCouponId(), subTotal);
        BigDecimal shippingPrice = BigDecimal.ZERO; // مفيش logic شحن لسه

        if (cart.getCouponId() != null) {
            order.setCouponData(toJson(Map.of(
                    "code", cart.getCouponId().getCode(),
                    "discountType", cart.getCouponId().getDiscountType(),
                    "discountValue", cart.getCouponId().getDiscountValue()
            )));
        }

        OrderPrice orderPrice = OrderPrice.builder()
                .orderId(order)
                .subTotal(subTotal)
                .discount(discount)
                .shippingPrice(shippingPrice)
                .build();

        order.setOrderPrice(orderPrice);

        Order saved = orderRepository.save(order);

        // مسح الـ Cart كله بعد ما الأوردر اتعمل
        cartRepository.delete(cart);

        return saved;
    }

    private BigDecimal calculateCouponDiscount(Coupon coupon, BigDecimal subTotal) {
        if (coupon == null || coupon.getIsActive() == null || !coupon.getIsActive()) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = subTotal.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        } else {
            discount = coupon.getDiscountValue();
        }

        return discount.compareTo(subTotal) > 0 ? subTotal : discount;
    }

    private String toJson(Map<String, ?> data) {
        try {
            return objectMapper.writeValueAsString(new LinkedHashMap<>(data));
        } catch (Exception e) {
            throw new BusinessException("Failed to serialize snapshot data");
        }
    }

    // ---------- SHOW ----------
    public Order findOrderForCustomer(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getCustomerId().getId().equals(customerId)) {
            throw new BusinessException("Order not found");
        }
        return order;
    }

    public Order findOrderForCompany(Long orderId, Long companyId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getCompanyId().getId().equals(companyId)) {
            throw new BusinessException("Order not found");
        }
        return order;
    }

    // ---------- LIST ----------
    public Page<Order> findOrdersForCustomer(Long customerId, String status, Pageable pageable) {
        return orderRepository.findByCustomer(customerId, status, pageable);
    }

    public Page<Order> findOrdersForCompany(Long companyId, String status, Pageable pageable) {
        return orderRepository.findByCompany(companyId, status, pageable);
    }

    // ---------- PAY (mock/manual) ----------
    public Order payOrder(Long orderId, Long customerId) {
        Order order = findOrderForCustomer(orderId, customerId);

        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("Only pending orders can be paid");
        }

        order.setStatus("PAID");
        return orderRepository.save(order);
    }

    // ---------- CANCEL ----------
    public Order cancelOrder(Long orderId, Long customerId) {
        Order order = findOrderForCustomer(orderId, customerId);

        if (!ACTIVE_STATUSES_FOR_CANCEL.contains(order.getStatus())) {
            throw new BusinessException("Only pending orders can be cancelled");
        }

        restoreStock(order);
        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProductId();
            product.setStock(product.getStock() + item.getQuantity());
        }
    }

    // ---------- ADMIN UPDATE ----------
    public Order updateOrderStatusForAdmin(Long orderId, Long companyId, String newStatus) {
        Order order = findOrderForCompany(orderId, companyId);

        if ("CANCELLED".equals(newStatus) && !"CANCELLED".equals(order.getStatus())) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Page<Order> findAllOrdersForSuperAdmin(String status, Pageable pageable) {
        return orderRepository.findAllByStatus(status, pageable);
    }

    public Order findOrderForSuperAdmin(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }

    public Order updateOrderStatusForSuperAdmin(Long orderId, String newStatus) {
        Order order = findOrderForSuperAdmin(orderId);

        if ("CANCELLED".equals(newStatus) && !"CANCELLED".equals(order.getStatus())) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
