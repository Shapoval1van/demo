package com.ncube.demo.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ncube.demo.repository.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class MemberResponse {
    private String id;
    private String firstName;
    private String lastName;
    private OffsetDateTime birthDate;
    private String zipCode;
    private String url;

    public static MemberResponse toApi(Member member) {
        return new MemberResponse(member.getId(),
                member.getFirstName(),
                member.getLastName(),
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(member.getBirthDate()), ZoneId.systemDefault()),
                member.getZipCode(),
                member.getUrl());
    }
}
