package com.example.transactionService;

import com.example.transactionService.dto.TransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping
    public ResponseEntity<String> transferFunds(@RequestBody TransactionRequest transactionRequest) {
        Boolean success = transactionsService.transferFunds(
                transactionRequest.getSourceAccountId(),
                transactionRequest.getDestinationAccountId(),
                transactionRequest.getAmount()
        );

        if (!success) {
            return new ResponseEntity<>("Transaction failed", HttpStatus.BAD_REQUEST);
        }

        //add success response with string body "Transaction successful"
        return new ResponseEntity<>("Transaction successful", HttpStatus.OK);
    }

}
