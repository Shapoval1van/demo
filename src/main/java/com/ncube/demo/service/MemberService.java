package com.ncube.demo.service;

import com.ncube.demo.controller.dto.MemberRequest;
import com.ncube.demo.controller.dto.MemberResponse;
import com.ncube.demo.exception.FileUploadException;
import com.ncube.demo.exception.ResourceNotFoundException;
import com.ncube.demo.repository.FileRepository;
import com.ncube.demo.repository.MemberRepository;
import com.ncube.demo.repository.model.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MemberService {

    @Value("${demo.host.url}")
    private String imageUrl;

    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    public MemberService(MemberRepository memberRepository,
                         FileRepository fileRepository) {
        this.memberRepository = memberRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public MemberResponse createNewMember(MemberRequest newMemberRequest) {
        return MemberResponse.toApi(
                memberRepository.save(
                        Member.create(newMemberRequest.getFirstName(),
                                newMemberRequest.getLastName(),
                                newMemberRequest.getBirthDate(),
                                newMemberRequest.getZipCode()
                        )
                ));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(String id) throws ResourceNotFoundException {
        return memberRepository.findById(id)
                .map(MemberResponse::toApi)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::toApi)
                .collect(toList());
    }

    @Transactional
    public MemberResponse merge(String id, MemberRequest memberRequest) {
        Member member = Member.create(memberRequest.getFirstName(),
                memberRequest.getLastName(),
                memberRequest.getBirthDate(),
                memberRequest.getZipCode()
        );
        member.setId(id);
        memberRepository.findById(id).ifPresent(m -> member.setUrl(m.getUrl()));
        return MemberResponse.toApi(memberRepository.save(member));
    }

    @Transactional
    public void delete(String id) throws ResourceNotFoundException {
        memberRepository.findById(id)
                .map(member -> {
                    memberRepository.delete(member);
                    return member;
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public String upload(String memberId, String originalFileName, InputStream fileStream) throws ResourceNotFoundException, FileUploadException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(memberId));
        String fileName = null;
        try {
            fileName = fileRepository.uploadFile(originalFileName, fileStream);
        } catch (IOException e) {
            throw new FileUploadException();
        }
        String fileUrl = String.format(imageUrl, fileName);
        member.setUrl(fileUrl);
        memberRepository.save(member);
        return fileUrl;
    }

    public File download(String fileName) {
        return fileRepository.downloadFile(fileName);
    }
}
