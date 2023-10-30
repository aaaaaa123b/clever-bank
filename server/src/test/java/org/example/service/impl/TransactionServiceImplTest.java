package org.example.service.impl;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldSuccessCreate() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(transactionRepository.create(transaction)).thenReturn(transaction);

        Transaction createdTransaction = transactionService.create(transaction);

        verify(transactionRepository, times(1)).create(transaction);
        assertEquals(1L, createdTransaction.getId());
    }
}