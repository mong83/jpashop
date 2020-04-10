package com.jpashop.service;

import com.jpashop.domain.Member;
import com.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void join() throws Exception {
        Member member = new Member();
        member.setName("kyk");

        Long saveId = memberService.join(member);
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));

    }
    @Test(expected = IllegalStateException.class)
    public void joinDup() throws Exception {
        Member member1 = new Member();
        member1.setName("kyk");

        Member member2 = new Member();
        member2.setName("kyk");

        memberService.join(member1);
        memberService.join(member2);


        fail("예외가 발생해야 한다");
    }

}