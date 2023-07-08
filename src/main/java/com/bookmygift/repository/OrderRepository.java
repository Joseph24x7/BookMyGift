package com.bookmygift.repository;

import com.bookmygift.entity.GiftTypeEnum;
import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.OrderStatusEnum;
import com.bookmygift.request.ShowOrderRequest;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    Optional<OrderEntity> findByOrderIdAndUsername(String orderId, String username);

    default List<OrderEntity> findOrdersByCriteria(ShowOrderRequest orderRequest) {
        Specification<OrderEntity> spec = Specification.where(null);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("username"), orderRequest.getUsername()));

        if (StringUtils.isNotBlank(orderRequest.getGiftType())) {
            Optional<GiftTypeEnum> gift = GiftTypeEnum.fromValue(orderRequest.getGiftType());
            if (gift.isPresent()) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("giftType"), gift.get()));
            } else {
                return Collections.emptyList();
            }
        }

        if (StringUtils.isNotBlank(orderRequest.getOrderStatus())) {
            Optional<OrderStatusEnum> order = OrderStatusEnum.fromValue(orderRequest.getOrderStatus());
            if (order.isPresent()) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus"), order.get()));
            } else {
                return Collections.emptyList();
            }
        }

        return findAll(spec);
    }

}
