package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.GetAllQuestionsResponse;
//import com.upgrad.quora.api.model.SignoutResponse;
//import com.upgrad.quora.api.model.SignupUserRequest;
//import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.GetAllQuestionsBusinessService;
//import com.upgrad.quora.service.business.SignoutBusinessService;
//import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
//import com.upgrad.quora.service.exception.SignOutRestrictedException;
//import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

//    @Autowired
//    private SignupBusinessService signupBusinessService;

    @Autowired
    private SigninBusinessService signinBusinessService;

//    @Autowired
//    private SignoutBusinessService signoutBusinessService;

//    @RequestMapping(method= RequestMethod.POST, path="/user/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
//
//        final UserEntity userEntity = new UserEntity();
//        userEntity.setUuid(UUID.randomUUID().toString());
//        userEntity.setFirstName(signupUserRequest.getFirstName());
//        userEntity.setLastName(signupUserRequest.getLastName());
//        userEntity.setUserName(signupUserRequest.getUserName());
//        userEntity.setEmail(signupUserRequest.getEmailAddress());
//        userEntity.setPassword(signupUserRequest.getPassword());
//        userEntity.setCountry(signupUserRequest.getCountry());
//        userEntity.setAboutMe(signupUserRequest.getAboutMe());
//        userEntity.setDob(signupUserRequest.getDob());
//        userEntity.setRole("nonadmin"); //Role for new users is nonadmin by default
//        userEntity.setContactNumber(signupUserRequest.getContactNumber());
//
//        final UserEntity createdUserEntity = signupBusinessService.signup(userEntity);
//        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
//        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
//    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/getAllQuestions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAllQuestions> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        //The authorization header will be in the format "Basic base64encoded username:password"
        //First split the header and separate Basic to retrieve the base64encoded username:password
        //Decode the base64encoded string and split it based on : to retrieve username and password
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthTokenEntity userAuthToken = signinBusinessService.signin(decodedArray[0],decodedArray[1]);
        UserEntity user = userAuthToken.getUser();

//        //Signin response will be the response body for a successful signin request
//        SigninResponse signinResponse = new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");

        //JWT access token is added to the successful sigin response header
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token",userAuthToken.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,headers,HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException{
//        //Authorization header will be in the format "Bearer JWT-token"
//        //Split the authorization header based on "Bearer " prefix to extract only the JWT token required for service class
//        UserEntity user = signoutBusinessService.signout(authorization.split("Bearer ")[1]);
//
//        SignoutResponse signoutResponse = new SignoutResponse().id(user.getUuid()).message("SIGNED OUT SUCCESSFULLY");
//        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
//    }

}