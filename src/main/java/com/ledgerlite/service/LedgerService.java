package main.java.com.ledgerlite.service;

import main.java.com.ledgerlite.domain.*;
import main.java.com.ledgerlite.exception.ValidationException;
import main.java.com.ledgerlite.persisence.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class LedgerService {
    private final Repository<Transaction> transactionRepository;
    private final Repository<Category> categoryRepository;

    public LedgerService(
            Repository<Transaction> transactionRepository,
            Repository<Category> categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        initializeDefaultCategories();
    }

    private void initializeDefaultCategories() {
        for (Category category : Category.defaultCategories()){
            categoryRepository.save(category);
        }
    }

    // Транзакции

    public Income addIncome(LocalDate date, Money amount, Category category, String note){
        validateTransactionParams(date,amount,category);

        Income income = new Income(date,amount,category,note);
        transactionRepository.save(income);
        return income;
    }

    public Expense addExpense(LocalDate date, Money amount, Category category, String note){
        validateTransactionParams(date,amount,category);

        Expense expense = new Expense(date,amount,category,note);
        transactionRepository.save(expense);
        return expense;
    }

    private void validateTransactionParams(LocalDate date, Money amount, Category category) {
        if (date.isAfter(LocalDate.now())){
            throw new ValidationException("Транзакция не может быть в будущем");
        }
        if (!categoryRepository.exists(category.code())){
            throw new ValidationException("Такой категории нет: " + category.name() + categoryRepository.findAll());
        }
    }

    public Optional<Transaction> getTransansaction(String id){
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByDate(LocalDate date){
        return transactionRepository.findAll().stream()
                .filter(t -> t.getDate().equals(date))
                .toList();
    }

    public void removeTransaction(String id){
        transactionRepository.delete(id);
    }

    //Категории

    public Category addCategory(String code,String name){
        if (categoryRepository.exists(code)){
            throw new ValidationException("Категория " + name + " уже существует.");
        }

        Category category = new Category(code, name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategory(String code){
        return categoryRepository.findById(code);
    }

    public void removeCategory(String code){
        boolean hasTransactions = transactionRepository.findAll().stream()
                .anyMatch(t -> t.getCategory().code().equals(code));

        if (hasTransactions) {
            throw new ValidationException("Нельзя удалить категорию у которой есть транзакции.");
        }

        categoryRepository.delete(code);
    }

    //Сводка

    public Money getTotalIncome(){
        return transactionRepository.findAll().stream()
                .filter(t -> t instanceof Income)
                .map(Transaction::getAmount)
                .reduce(Money.zero(Currency.getInstance("RUB")),Money::add);
    }

    public Money getTotalExpense(){
        return transactionRepository.findAll().stream()
                .filter(t -> t instanceof Expense)
                .map(Transaction::getAmount)
                .reduce(Money.zero(Currency.getInstance("RUB")),Money::add);
    }

}
