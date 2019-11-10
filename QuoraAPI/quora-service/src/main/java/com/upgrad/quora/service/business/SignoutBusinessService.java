package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignoutBusinessService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signout(final String authorization) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthTokenByAccessToken(authorization);

        //Throw exception if either the JWT access token is invalid or if the user has already signed out
        //In case user has already signed out then the logout time of the user will not be null
        if(userAuthToken == null || (userAuthToken != null && userAuthToken.getLogoutAt() != null)) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        userAuthToken.setLogoutAt(ZonedDateTime.now());
        userDao.updateLogOutTime(userAuthToken);
        return userAuthToken.getUser();
    }
}
