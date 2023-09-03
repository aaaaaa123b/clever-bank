package org.example.service.impl;

import org.example.enumeration.TransactionType;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;

import org.example.service.TransactionService;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    void create() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(transactionRepository.create(transaction)).thenReturn(transaction);

        Transaction createdTransaction = transactionService.create(transaction);

        verify(transactionRepository, times(1)).create(transaction);
        assertEquals(1L, createdTransaction.getId());
    }
}