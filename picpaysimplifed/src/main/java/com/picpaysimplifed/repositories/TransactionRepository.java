package com.picpaysimplifed.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picpaysimplifed.domain.transaction.Transaction;




public interface TransactionRepository extends JpaRepository <Transaction, Long>{

}
