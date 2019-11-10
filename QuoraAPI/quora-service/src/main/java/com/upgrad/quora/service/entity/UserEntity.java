package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "QUESTION", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByContent", query = "SELECT u FROM UserEntity u WHERE u.content = :content")
        }
)
public class UserEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "content")
    @NotNull
    @Size(max = 500)
    private String content;

    @Column(name = "date")
    @NotNull
    @Size(max = 6)
    private String date;

    @Column(name = "user_id", unique = true)
    @NotNull
    @Size(max = 30)
    private String user_id;


    @Override
    public boolean equals(Object obj){
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getcontent() {
        return content;
    }

    public String date() {
        return date;
    }

    public String getUser_Id() {
        return user_id;
    }
}