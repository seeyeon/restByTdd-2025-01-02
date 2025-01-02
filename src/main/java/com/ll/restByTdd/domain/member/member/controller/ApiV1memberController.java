package com.ll.restByTdd.domain.member.member.controller;

import com.ll.restByTdd.domain.member.member.entity.Member;
import com.ll.restByTdd.domain.member.member.service.MemberService;
import com.ll.restByTdd.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1memberController {
    private final MemberService memberService;

    record MemberJoinReqBody(
            String username,
            String password,
            String nickname
    ){
    }

    @PostMapping("/join")
    public RsData<Void> join(
            @RequestBody MemberJoinReqBody req
    ){

        Member member = memberService.join(req.username, req.password, req.nickname);

        return new RsData<>("201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getNickname()));
    }

}
