package com.example.Lab10Demo;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private PaymentRepository repository;

    public PaymentService(PaymentRepository repository){
        this.repository = repository;
    }

    public void sendPayment(String senderEmail, String senderPassword,
                            String receiverEmail, AccountModel.Currency currency, double amount){
      UserModel sender = getUser(senderEmail, senderPassword);
      UserModel receiver = getUser(receiverEmail);
      AccountModel senderAccount = getUserAccountForCurrency(sender, currency);
      AccountModel receiverAccount = getUserAccountForCurrency(receiver, currency);
      if(amount > senderAccount.getAmount()){
          throw PaymentException.accountHasNotEnoughAmountForPayment();
      }
      PaymentModel payment = new PaymentModel(UUID.randomUUID().toString(), senderAccount.getId(),
              receiverAccount.getId(), currency, amount);
      if(!repository.savePayment(payment)){
          throw PaymentException.paymentCouldNotBeProcessed();
      }
    }
    private UserModel getUser(String email){
        Optional<UserModel> user = repository.getUser(email);
        if(!user.isPresent()){
          throw PaymentException.userNotFound();
        }
        return user.get();
    }
    private UserModel getUser(String email, String password){
        UserModel user = getUser(email);
        if(!password.equals(user.getPassword())){
            throw PaymentException.badCredentials();
        }
        return user;
    }
    private AccountModel getUserAccountForCurrency(UserModel user, AccountModel.Currency currency){
        Optional<AccountModel> account = repository.getUserAccounts(user.getId()).stream().filter(a-> currency.equals(a.getCurrency()))
                .findFirst();
        if(account.isEmpty()){
            throw PaymentException.userHasNoAccountForCurrency();
        }
        return account.get();
    }
}
