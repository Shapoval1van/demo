package com.ncube.demo.controller;

import com.ncube.demo.service.MemberService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {
    private final MemberService memberService;

    public PhotoController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/photo/{name}")
    @ResponseBody
    public FileSystemResource downloadPhoto(@PathVariable String name) {
        return new FileSystemResource(memberService.download(name));
    }
}
