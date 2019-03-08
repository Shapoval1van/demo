package com.ncube.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class MemberRequest {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private OffsetDateTime birthDate;
    @NonNull
    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$", message = "Not valid zip code")
    private String zipCode;
}
