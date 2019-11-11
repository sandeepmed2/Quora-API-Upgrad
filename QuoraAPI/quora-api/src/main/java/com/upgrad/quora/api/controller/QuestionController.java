package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthorizationBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    //Importing the QuestionBusiness Service.
    @Autowired
    private QuestionBusinessService questionBusinessService;

    //Importing Authorization Business Service
    @Autowired
    private AuthorizationBusinessService authorizationBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {

        final UserAuthTokenEntity userAuthEntity = authorizationBusinessService.authorizeUser(parseAuthToken(authorization));

        final ZonedDateTime now = ZonedDateTime.now();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUser(userAuthEntity.getUser());
        questionEntity.setDate(now);

        final QuestionEntity createdQuestion = questionBusinessService.createQuestion(questionEntity, userAuthEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE , path = "/question/delete/{questionId}" ,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionid )
         throws AuthorizationFailedException , InvalidQuestionException {

        final UserAuthTokenEntity userAuthEntity = authorizationBusinessService.authorizeUser(parseAuthToken(authorization));

        QuestionEntity deletedQuestion = questionBusinessService.deleteQuestion(questionid, userAuthEntity);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");
        //on successful deletion of the question
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);

    }

    //code Get all question
    @RequestMapping(method = RequestMethod.GET, path = "/question/all" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestion(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        final UserAuthTokenEntity userAuthToken = authorizationBusinessService.authorizeUser(parseAuthToken(authorization));

        final List<QuestionEntity> allQuestion = questionBusinessService.getAllQuestion(userAuthToken);

        List<QuestionDetailsResponse> questionResponse = questionslist(allQuestion);

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponse, HttpStatus.OK);
    }
    //code to get all question basd on user ID
    @RequestMapping(method = RequestMethod.GET , path = "/question/all/{userId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization )
        throws AuthorizationFailedException , UserNotFoundException {

        final UserAuthTokenEntity userAuthEntity = authorizationBusinessService.authorizeUser(parseAuthToken(authorization));

        final List<QuestionEntity> allQuestionByUser = questionBusinessService.getAllQuestionsByUser(userId , userAuthEntity);
        //on successful creation of question
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionslist(allQuestionByUser), HttpStatus.OK);

    }
    //code to edit of question
    @RequestMapping(method = RequestMethod.PUT , path = "/question/edit/{questionId}" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable("questionId") final String questionId , @RequestHeader("authorization") final String authorization, QuestionEditRequest questionEditRequest)
    throws AuthorizationFailedException,InvalidQuestionException {

        final UserAuthTokenEntity userAuthEntity = authorizationBusinessService.authorizeUser(parseAuthToken(authorization));
        String content = questionEditRequest.getContent();

        QuestionEntity editedQuestion = questionBusinessService.editQuestionContent(questionId,userAuthEntity, content);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");
        //on successful edit of question
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

    //code to assign  all questions to list
    public List<QuestionDetailsResponse> questionslist(List<QuestionEntity> allQuestion){
        List<QuestionDetailsResponse> listofquestions = new ArrayList<>();
        for ( QuestionEntity questionEntity : allQuestion){
            QuestionDetailsResponse Response = new QuestionDetailsResponse();
            Response.id(questionEntity.getUuid());
            Response.content(questionEntity.getContent());
            listofquestions.add(Response);
        }
        return listofquestions;
    }

    private String parseAuthToken(final String authorization){
        //Authorization header will be in the format "Bearer JWT-token"
        //Split the authorization header based on "Bearer " prefix to extract only the JWT token required for service class
        //If authorization header doesn't contain "Bearer " prefix then pass it as it is since it will be from test cases
        return authorization.startsWith("Bearer ")? authorization.split("Bearer ")[1]: authorization;
    }
}
