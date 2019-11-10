package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class SigninBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signin(final String userName, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(userName);

        //Throw exception if given user name does not exist in database
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());

        //Throw exception if provided password does not match with the password stored in database
        if(!encryptedPassword.equals(userEntity.getPassword())){
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }

        //Construct a JWT token using JwtTokenProvider and persist it in the database before returning to controller
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
        userAuthToken.setUuid(UUID.randomUUID().toString());
        userAuthToken.setUser(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));
        userAuthToken.setLoginAt(now);
        userAuthToken.setExpiresAt(expiresAt);

        userDao.createAuthToken(userAuthToken);

        return userAuthToken;
    }
}
