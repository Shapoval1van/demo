package com.ncube.demo.repository.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "member")
public class Member {
    @Id
    @Setter
    private String id;

    private String firstName;

    private String lastName;

    private Long birthDate;

    private String zipCode;

    @Setter
    private String url;

    public static Member create(String firstName,
                                String lastName,
                                OffsetDateTime birthDate,
                                String zipCode) {
        //TODO add picture
        return new Member(UUID.randomUUID().toString(),
                firstName,
                lastName,
                birthDate.toEpochSecond(),
                zipCode,
                null);
    }
}
