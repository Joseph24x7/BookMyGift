package com.bookmygift.repository;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.request.ShowOrderRequest;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderIdAndUsername(String orderId, String username);

    default List<Order> findOrdersByCriteria(ShowOrderRequest orderRequest) {
        Specification<Order> spec = Specification.where(null);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("username"), orderRequest.getUsername()));

        if (StringUtils.isNotBlank(orderRequest.getGiftType())) {
            Optional<GiftType> gift = GiftType.fromValue(orderRequest.getGiftType());
            if (gift.isPresent()) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("giftType"), gift.get()));
            }
        }

        if (StringUtils.isNotBlank(orderRequest.getOrderStatus())) {
            Optional<OrderStatus> order = OrderStatus.fromValue(orderRequest.getOrderStatus());
            if (order.isPresent()) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus"), order.get()));
            }
        }

        return findAll(spec);
    }

}
