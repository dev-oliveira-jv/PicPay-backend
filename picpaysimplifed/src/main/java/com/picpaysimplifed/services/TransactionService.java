package com.picpaysimplifed.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplifed.domain.transaction.Transaction;
import com.picpaysimplifed.domain.user.User;
import com.picpaysimplifed.dtos.TransactionDTO;
import com.picpaysimplifed.repositories.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private UserService UserService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction (TransactionDTO transaction) throws Exception{

        User sender = this.UserService.findUserById(transaction.senderId());
        User receiver = this.UserService.findUserById(transaction.receiverId());

        UserService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.autorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada ");
        }


        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.UserService.saveUser(sender);
        this.UserService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso ");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso ");

        return newTransaction;
        
    }

    public boolean autorizeTransaction (User sender, BigDecimal value){

        ResponseEntity<Map> authorizationsResponse   = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(authorizationsResponse.getStatusCode() == HttpStatus.OK ){
            String authorization = (String) authorizationsResponse.getBody().get("status");
            return "success".equalsIgnoreCase(authorization);
        } else return false;
        
    }

}
