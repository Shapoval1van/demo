package com.ncube.demo.service;

import com.ncube.demo.controller.dto.MemberRequest;
import com.ncube.demo.controller.dto.MemberResponse;
import com.ncube.demo.exception.ResourceNotFoundException;
import com.ncube.demo.repository.FileRepository;
import com.ncube.demo.repository.MemberRepository;
import com.ncube.demo.repository.model.Member;
import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Value("${demo.host.url}")
    private String imageUrlFormat;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void createNewMember() {
        String name = "Dart";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        OffsetDateTime birthDay = OffsetDateTime.now();
        MemberRequest newMemberRequest = new MemberRequest(name, lastName, birthDay, zipCode);
        Member newMember = Member.create(name, lastName, birthDay, zipCode);
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        MemberResponse memberResponse = memberService.createNewMember(newMemberRequest);

        assertEquals(newMember.getId(), memberResponse.getId());
        assertEquals(newMember.getLastName(), memberResponse.getLastName());
        assertEquals(newMember.getFirstName(), memberResponse.getFirstName());
        assertEquals(newMember.getZipCode(), memberResponse.getZipCode());
    }

    @SneakyThrows
    @Test
    public void getMember() {
        String name = "Dart";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        OffsetDateTime birthDay = OffsetDateTime.now();
        Member member = Member.create(name, lastName, birthDay, zipCode);

        String id = member.getId();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        MemberResponse memberResponse = memberService.getMember(id);
        assertEquals(member.getId(), memberResponse.getId());
        assertEquals(member.getLastName(), memberResponse.getLastName());
        assertEquals(member.getFirstName(), memberResponse.getFirstName());
        assertEquals(member.getZipCode(), memberResponse.getZipCode());
    }

    @Test
    public void getMember_shouldThrowException() throws ResourceNotFoundException {
        String id = "id";

        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Member: id not found");
        memberService.getMember(id);
    }

    @Test
    public void getAll() {
        String name = "Dart";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        OffsetDateTime birthDay = OffsetDateTime.now();
        Member first = Member.create(name, lastName, birthDay, zipCode);
        Member second = Member.create(name, lastName, birthDay, zipCode);

        when(memberRepository.findAll()).thenReturn(Arrays.asList(first, second));

        List<MemberResponse> members = memberService.getAll();
        assertEquals(2, members.size());
    }

    @Test
    public void merge() {
        String name = "Dart";
        String newName = "111";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        String url = "url";
        OffsetDateTime birthDay = OffsetDateTime.now();
        Member member = Member.create(name, lastName, birthDay, zipCode);
        Member updatedMember = Member.create(newName, newName, birthDay, zipCode);
        member.setUrl(url);
        MemberRequest updateMemberRequest = new MemberRequest(newName, newName, birthDay, zipCode);

        String id = member.getId();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        MemberResponse memberResponse = memberService.merge(id, updateMemberRequest);
        ArgumentCaptor<Member> argument = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(argument.capture());
        assertEquals("If a member already present we should be aware that we don't change URL to image",
                url,
                argument.getValue().getUrl());
        assertEquals(updatedMember.getId(), memberResponse.getId());
    }

    @Test
    public void delete_shouldThrowException() throws ResourceNotFoundException {
        String id = "id";

        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Member: id not found");
        memberService.delete(id);
    }

    @SneakyThrows
    @Test
    public void delete() {
        String name = "Dart";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        OffsetDateTime birthDay = OffsetDateTime.now();
        Member member = Member.create(name, lastName, birthDay, zipCode);

        String id = member.getId();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        doNothing().when(memberRepository).delete(member);

        memberService.delete(id);
        verify(memberRepository).delete(member);
    }

    @SneakyThrows
    @Test
    public void upload() {
        String name = "Dart";
        String lastName = "Weider";
        String zipCode = "1233-2132";
        String originalFileName = "tem.img";
        String newFileName = "111111tem.img";
        OffsetDateTime birthDay = OffsetDateTime.now();
        Member member = Member.create(name, lastName, birthDay, zipCode);
        InputStream stream = new ByteArrayInputStream(Charset.forName("UTF-16").encode("test").array());

        String id = member.getId();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        when(fileRepository.uploadFile(originalFileName, stream)).thenReturn(newFileName);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        String imageURL = memberService.upload(id, originalFileName, stream);

        ArgumentCaptor<Member> argument = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(argument.capture());
        assertNotNull("Should save member with URL to image", argument.getValue().getUrl());
        verify(fileRepository).uploadFile(originalFileName, stream);
        assertEquals(imageURL, String.format(imageUrlFormat, newFileName));
    }
}