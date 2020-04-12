package com.jpashop.service;

import com.jpashop.domain.Address;
import com.jpashop.domain.Member;
import com.jpashop.domain.Order;
import com.jpashop.domain.OrderStatus;
import com.jpashop.domain.item.Book;
import com.jpashop.domain.item.Item;
import com.jpashop.execption.NotEnoughStockException;
import com.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    

    @Test
    public void order() throws Exception{

        Member member = CreateMember();

        Book book = CreateBook("시골 JPA", 10000, 10);


        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder =  orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER" , OrderStatus.ORDER ,getOrder.getStatus());
        assertEquals("주문한 상품종류 수가 정확해야 한다" , 1 , getOrder.getOrderItems().size());
        assertEquals("주문가격은 가격 * 수량 이다" ,10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문수량 만큼 재고가 주러야 한다" , 8 , book.getStockQuantity());
    }


    @Test(expected = NotEnoughStockException.class)
    public void orderOverStock() throws Exception{

        Member member = CreateMember();
        Item item = CreateBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        orderService.order(member.getId() , item.getId(), orderCount);

        fail("재고 수량 부족 예외가 발생해야 한다.");


    }

    @Test
    public void orderCancle() throws Exception{

        Member member = CreateMember();
        Book item = CreateBook("시골 JPA" , 10000 , 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancleOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);


        assertEquals("주문 취소상태는 Cancle 입니다." , OrderStatus.CANCEL , getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다" , 10 , item.getStockQuantity());



    }

    @Test
    public void orderStockCheck() throws Exception{


    }



    private Book CreateBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member CreateMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123=123"));
        em.persist(member);
        return member;
    }

}