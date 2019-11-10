package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        //Authorization header will be in the format "Bearer JWT-token"
        //Split the authorization header based on "Bearer " prefix to extract only the JWT token required for service class
        final UserEntity user = commonBusinessService.getUser(userId, authorization.split("Bearer ")[1]);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(user.getFirstName()).lastName(user.getLastName())
                .userName(user.getUserName()).emailAddress(user.getEmail()).country(user.getCountry()).aboutMe(user.getAboutMe())
                .dob(user.getDob()).contactNumber(user.getContactNumber());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
