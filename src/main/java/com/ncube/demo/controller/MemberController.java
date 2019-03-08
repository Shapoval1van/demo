package com.ncube.demo.controller;

import com.ncube.demo.controller.dto.MemberRequest;
import com.ncube.demo.controller.dto.MemberResponse;
import com.ncube.demo.controller.dto.PhotoResponse;
import com.ncube.demo.exception.FileUploadException;
import com.ncube.demo.exception.ResourceNotFoundException;
import com.ncube.demo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController()
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/members",
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE},
            produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberRequest newMember) {
        MemberResponse memberResponse = memberService.createNewMember(newMember);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/members",
            produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<List<MemberResponse>> getAll() {
        List<MemberResponse> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/members/{id}",
            produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public MemberResponse getMember(@PathVariable("id") String id) throws ResourceNotFoundException {
        return memberService.getMember(id);
    }

    /**
     * Add profile photo to member
     *
     * @param id   member id
     * @param file photo
     * @return link to photo
     * @throws ResourceNotFoundException if user not found
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/members/{id}/photo",
            consumes = {MULTIPART_FORM_DATA_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<PhotoResponse> uploadPhoto(@PathVariable String id,
                                                     @RequestPart("file") MultipartFile file)
            throws ResourceNotFoundException, FileUploadException {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        InputStream fileSteam = null;
        try {
            fileSteam = file.getInputStream();
        } catch (IOException e) {
            throw new FileUploadException();
        }
        String url = memberService.upload(id, file.getOriginalFilename(), fileSteam);
        return new ResponseEntity<>(new PhotoResponse(url), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/members/{id}",
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE},
            produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public MemberResponse updateMember(@PathVariable("id") String id,
                                       @RequestBody @Valid MemberRequest member) {
        return memberService.merge(id, member);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/members/{id}",
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE},
            produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public void delete(@PathVariable("id") String id) throws ResourceNotFoundException {
        memberService.delete(id);
    }
}
