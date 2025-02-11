package com.ll.restByTdd.com.ll.restByTdd.domain.member.memeber.controller;

import com.ll.restByTdd.domain.member.member.controller.ApiV1memberController;
import com.ll.restByTdd.domain.member.member.entity.Member;
import com.ll.restByTdd.domain.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest  // 스프링부트 테스트 클래스임을 나타냅니다.
@ActiveProfiles("test")  // 테스트 환경에서는 test 프로파일을 활성화합니다.
@AutoConfigureMockMvc // MockMvc를 자동으로 설정합니다.
@Transactional  // 각 테스트 메서드가 종료되면 롤백됩니다.
public class ApiV1memberControllerTest {

    @Autowired
    private MemberService memberService;
    //test에서는 private final MemberService memberService; 사용X(@Autowired 사용해야함)


    @Autowired    // MockMvc를 주입
    private MockMvc mvc;

    @Test
    @DisplayName("회원가입")
    void t1() throws Exception {
        // 회원가입 요청을 보냅니다.
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/join")
                        .content("""
                                        {
                                            "username": "usernew",
                                            "password": "1234",
                                            "nickname": "무명"
                                        }
                                        """.stripIndent())
                        .contentType(
                                new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                        )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1memberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isCreated())  //201결과값을 기대한다
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("무명님 환영합니다. 회원가입이 완료되었습니다."));

        Member member = memberService.findByUsername("usernew").get();
        assertThat(member.getNickname()).isEqualTo("무명");
    }
}
