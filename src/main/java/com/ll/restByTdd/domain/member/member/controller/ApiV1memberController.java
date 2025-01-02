package com.ll.restByTdd.domain.member.member.controller;

import com.ll.restByTdd.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1memberController {

    @PostMapping("/join")
    public RsData<Void> join(){
        return new RsData<>("201-1","무명님 환영합니다.");
    }

}
