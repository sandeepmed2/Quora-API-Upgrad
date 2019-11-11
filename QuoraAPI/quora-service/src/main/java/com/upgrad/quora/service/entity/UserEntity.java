package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity

@Table(name = "USERS", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByUserName", query = "SELECT u FROM UserEntity u WHERE u.userName = :userName"),
                @NamedQuery(name = "userByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :emailAddress"),
                @NamedQuery(name = "userByUuid", query = "SELECT u FROM UserEntity u WHERE u.uuid = :uuid")//,
                //@NamedQuery(name = "userByContent", query = "SELECT u FROM UserEntity u WHERE u.content = :content")
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

//    @Column(name = "content")
//    @NotNull
//    @Size(max = 500)
//    private String content;
//
//    @Column(name = "date")
//    @NotNull
//    @Size(max = 6)
//    private String date;
//
//    @Column(name = "user_id", unique = true)
//    @NotNull
//    @Size(max = 30)
//    private String user_id;

    @Column(name = "firstname")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "lastname")
    @NotNull
    @Size(max = 30)
    private String lastName;

    @Column(name = "username", unique = true)
    @NotNull
    @Size(max = 30)
    private String userName;

    @Column(name = "email", unique = true)
    @NotNull
    @Size(max = 50)
    private String email;

    @Column(name = "password")
    @NotNull
    @Size(max = 255)
    @ToStringExclude
    private String password;

    @Column(name = "salt")
    @NotNull
    @Size(max = 200)
    @ToStringExclude
    private String salt;

    @Column(name = "country")
    @Size(max = 30)
    private String country;

    @Column(name = "aboutme")
    @Size(max = 50)
    private String aboutMe;

    @Column(name = "dob")
    @Size(max = 30)
    private String dob;

    @Column(name = "role")
    @Size(max = 30)
    private String role;

    @Column(name = "contactnumber")
    @Size(max = 30)
    private String contactNumber;

    @Override
    public boolean equals(Object obj){
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

//    public String getcontent() {
//        return content;
//    }
//
//    public String date() {
//        return date;
//    }
//
//    public String getUser_Id() {
//        return user_id;
//    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
