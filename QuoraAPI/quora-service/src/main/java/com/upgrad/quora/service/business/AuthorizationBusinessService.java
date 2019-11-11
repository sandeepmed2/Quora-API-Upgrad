package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationBusinessService {

    @Autowired
    private UserDao userDao;

    public UserAuthTokenEntity authorizeUser(final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = userDao.getUserAuthTokenByAccessToken(authorization);

        //Throw exception if provided access token does not exist in database
        if (userAuthToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Throw exception if user has logged out in which case logout time will not be null
        if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        return userAuthToken;
    }
}
