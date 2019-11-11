package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        return questionDao.createQuestion(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questionId, UserAuthTokenEntity userAuthEntity) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = validateQuestion(questionId);
        UserEntity userEntity = userAuthEntity.getUser();
        String role = userEntity.getRole();

        Integer authUserId = userAuthEntity.getUser().getId();
        Integer queUserId = questionEntity.getUser().getId();
        //if user is not admin and also trying to delete other question.
         if ((role.equals("nonadmin")) && (authUserId != queUserId)) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        return questionDao.deleteQuestion(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestion()  {
        return questionDao.getAllQuestion();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(String userId, UserAuthTokenEntity userAuthEntity) throws UserNotFoundException {

        UserEntity userEntity = userAuthEntity.getUser();

        if (!userEntity.getUuid().equals(userId)) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userId);
    }

    public List<QuestionEntity> getAllQuestionById(String uuid) throws AuthorizationFailedException {
        return questionDao.getAllQuestion();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(String questionId, UserAuthTokenEntity userAuthEntity , String content) throws AuthorizationFailedException, InvalidQuestionException {

       QuestionEntity questionEntity = validateQuestion(questionId);

       Integer authUserId = userAuthEntity.getUser().getId();
       Integer queUserId = questionEntity.getUser().getId();
        if (authUserId != queUserId){
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setContent(content);
        return  questionDao.editQuestion(questionEntity);
    }

     public QuestionEntity validateQuestion(String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if(questionEntity==null) {
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        return questionEntity;
    }
}

