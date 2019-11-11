package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    //Code to check if the user is admin or not and wether he is signedin or signed out based on authorization
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(String uuid, String authorization) throws UserNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthTokenByAccessToken(authorization);
        if (userAuthEntity != null) {
            if ((userAuthEntity.getLogoutAt() == null)) {
                UserEntity userEntity = userDao.getUserByUuid(uuid);
                if (userEntity == null) {
                    throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
                }
                String role = userEntity.getRole();
                if(role.equalsIgnoreCase("admin"))
                {
                    userDao.deleteUser(userEntity);
                }
                else {
                    throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
                }
            }
            else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out");
            }
        }
        else{
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
    }
}
