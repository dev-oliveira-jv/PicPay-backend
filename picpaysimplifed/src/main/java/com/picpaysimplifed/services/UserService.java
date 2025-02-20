package com.picpaysimplifed.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplifed.domain.user.User;
import com.picpaysimplifed.domain.user.UserType;
import com.picpaysimplifed.repositories.UserRepository;




@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{

        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception(" Usuário do tipo lojista não está autorizado a realizar esse tipo de transação ");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception(" Saldo insuficiente  ");
        }

      
    }

    public User findUserById( Long id) throws Exception{

        return this.repository.findUserById(id).orElseThrow( () -> new Exception(" Usuário não encontrado "));

    }

    public void saveUser( User user){

        this.repository.save(user);

    }


}
