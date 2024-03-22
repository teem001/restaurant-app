package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryID;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService{




    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);


        return null;
    }

    @Override
    public PaymentEvent validateAndCancel(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        return null;
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if(payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())){
            log.error("Customer with id {}, does not have enough credit for payment", payment.getCustomerId().getValue());
            failureMessages.add("customer with id =" + payment.getCustomerId().getValue()+"  does not have credit for payment");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                .customerId(payment.getCustomerId())
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build()
        );
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {

        Money totalCreditHistory = creditHistories.stream()
                .filter(p-> p.getTransactionType() == TransactionType.CREDIT)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);

        Money totalDebitHistory = creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == TransactionType.DEBIT)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);

        if(totalDebitHistory.isGreaterThan(totalCreditHistory)){
            log.error("Customer with id {} does not have enough credit according to credit history", creditEntry.getCustomerId().getValue());
            failureMessages.add("Customer with id "+ creditEntry.getCustomerId().getValue()+"do not have enough credit according to credit history");

        }
        if(creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))){
            log.error("total ");
        }

    }
}
