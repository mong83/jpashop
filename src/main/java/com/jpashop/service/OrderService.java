package com.jpashop.service;


import com.jpashop.domain.Delivery;
import com.jpashop.domain.Member;
import com.jpashop.domain.Order;
import com.jpashop.domain.OrderItem;
import com.jpashop.domain.item.Item;
import com.jpashop.repository.ItemRepository;
import com.jpashop.repository.MemberRepository;
import com.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId , Long itemid , int count){

        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemid);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery , orderItem);

        orderRepository.save(order);
        return order.getId();

    }

    /**
     * 주문취소
     */
    @Transactional
    public void cancleOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);

        order.cancle();
    }

    /*
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);

    }

     */

}
