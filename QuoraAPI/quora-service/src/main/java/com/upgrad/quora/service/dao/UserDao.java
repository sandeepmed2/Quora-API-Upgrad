package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByContent(final String content){
        try {
            return entityManager.createNamedQuery("userByContent", UserEntity.class).setParameter("content", content)
                    .getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByUuid(final String userUuid){
        try {
            return entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",userUuid)
                    .getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public UserAuthTokenEntity getUserAuthTokenByAccessToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken",accessToken)
                    .getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public void updateLogOutTime(final UserAuthTokenEntity userAuthTokenEntity){
        entityManager.merge(userAuthTokenEntity);
    }
}
