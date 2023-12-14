package com.forexservice.forexservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.forexservice.forexservice.dtos.CurrencyDto;
import com.forexservice.forexservice.dtos.ExchangeRateDto;
import com.forexservice.forexservice.dtos.LoginRequestDto;
import com.forexservice.forexservice.dtos.LoginResponseDto;
import com.forexservice.forexservice.dtos.RateDto;
import com.forexservice.forexservice.dtos.TransactionDto;
import com.forexservice.forexservice.dtos.TransferRequestDto;
import com.forexservice.forexservice.dtos.UserDto;
import com.forexservice.forexservice.pojos.Currency;
import com.forexservice.forexservice.pojos.ExchangeRate;
import com.forexservice.forexservice.pojos.Rate;
import com.forexservice.forexservice.pojos.Transaction;
import com.forexservice.forexservice.pojos.User;
import com.forexservice.forexservice.respositories.CurrencyRepository;
import com.forexservice.forexservice.respositories.ExchangeRateRepository;
import com.forexservice.forexservice.respositories.RateRepository;
import com.forexservice.forexservice.respositories.TransactionRepository;
import com.forexservice.forexservice.respositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app")
public class ForexController {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    // API for user signup
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        // Validate and save the user (consider password encryption)
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    // API for user login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User authenticatedUser = userRepository.findByUsernameAndPassword(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (authenticatedUser != null) {
            // Authentication successful
            String token = "DUMMY_TOKEN";

            // Create a response object containing the token and user data
            LoginResponseDto loginResponse = new LoginResponseDto(token, authenticatedUser);

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            // Authentication failed
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // API to get all available currencies
    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    // API to get all historical exchange rates
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        // Implement logic to fetch and return exchange rates
        // You may consider creating a service for fetching real-time exchange rates
        // For simplicity, return an empty list
        List<ExchangeRate> exchangeRates = List.of();
        if (exchangeRates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or any appropriate status code
        }

        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    // API to get user transactions
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionRepository.findBySenderIdOrRecipientId(userId, userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // API to get current rates of at least 5 world currencies
    @GetMapping("/current-rates")
    public ResponseEntity<List<RateDto>> getCurrentRates() {
        List<Rate> currentRates = rateRepository.findAll(); // Adjust this based on your logic
        List<RateDto> rateDtos = mapRatesToDTOs(currentRates);
        return ResponseEntity.ok(rateDtos);
    }

    // API to send money between currencies
    @PostMapping("/send-money")
    public ResponseEntity<TransactionDto> sendMoney(@RequestBody TransactionDto transactionDto) {
        // Validate input, perform necessary checks, etc.

        // Find the sender and recipient users (assuming they exist)
        User sender = userRepository.findById(transactionDto.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(transactionDto.getRecipient().getId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Find the currency (assuming it exists)
        Currency currency = currencyRepository.findById(transactionDto.getCurrency().getId())
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        // Create a new transaction entity
        Transaction newTransaction = new Transaction();
        newTransaction.setSender(sender);
        newTransaction.setRecipient(recipient);
        newTransaction.setCurrency(currency);
        newTransaction.setAmount(transactionDto.getAmount());
        newTransaction.setTimestamp(LocalDateTime.now());

        // Save the new transaction to the database
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        // You might want to perform additional logic here (e.g., update balances, etc.)

        // Map the saved transaction to DTO and return it
        TransactionDto result = mapTransactionToDto(savedTransaction);
        return ResponseEntity.ok(result);
    }

    // API to get historical data
    @GetMapping("/historical-data")
    public ResponseEntity<List<ExchangeRateDto>> getHistoricalData() {
        List<ExchangeRate> historicalData = exchangeRateRepository.findAll(); // Adjust this based on your logic
        List<ExchangeRateDto> exchangeRateDTOs = mapExchangeRatesToDTOs(historicalData);
        return ResponseEntity.ok(exchangeRateDTOs);
    }

    // Admin API to manage currency rates for particular days
    @PostMapping("/admin/manage-rates")
    public ResponseEntity<String> manageRates(@RequestBody RateDto rateDTO) {
        // Implement logic to save/update rates in the database
        Rate savedRate = rateRepository.save(mapDTOToRate(rateDTO));
        return ResponseEntity.ok("Rate saved/updated successfully");
    }

    // Helper method to map a RateDTO to a Rate entity
    private Rate mapDTOToRate(RateDto rateDTO) {
        Rate rate = new Rate();
        rate.setId(rateDTO.getId());

        // Assuming the CurrencyDTO contains only the ID
        Currency currency = currencyRepository.findById(rateDTO.getCurrency().getId())
                .orElseThrow(() -> new RuntimeException("Currency not found"));
        rate.setCurrency(currency);

        rate.setRate(rateDTO.getRate());
        rate.setDate(rateDTO.getDate());

        return rate;
    }

    // Helper method to map a list of Rate entities to a list of RateDTOs
    private List<RateDto> mapRatesToDTOs(List<Rate> rates) {
        return rates.stream()
                .map(this::mapRateToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map a Rate entity to a RateDTO
    private RateDto mapRateToDTO(Rate rate) {
        RateDto rateDTO = new RateDto();
        rateDTO.setId(rate.getId());
        rateDTO.setCurrency(mapCurrencyToDto(rate.getCurrency()));
        rateDTO.setRate(rate.getRate());
        rateDTO.setDate(rate.getDate());
        return rateDTO;
    }

    // Helper method to map a list of ExchangeRate entities to a list of ExchangeRateDTOs
    private List<ExchangeRateDto> mapExchangeRatesToDTOs(List<ExchangeRate> exchangeRates) {
        return exchangeRates.stream()
                .map(this::mapExchangeRateToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map an ExchangeRate entity to an ExchangeRateDTO
    private ExchangeRateDto mapExchangeRateToDTO(ExchangeRate exchangeRate) {
        ExchangeRateDto exchangeRateDTO = new ExchangeRateDto();
        exchangeRateDTO.setId(exchangeRate.getId());
        exchangeRateDTO.setCurrency(mapCurrencyToDto(exchangeRate.getCurrency()));
        exchangeRateDTO.setRate(exchangeRate.getRate());
        exchangeRateDTO.setDate(exchangeRate.getDate());
        return exchangeRateDTO;
    }

    // Helper method to map a Currency entity to a CurrencyDto
    private CurrencyDto mapCurrencyToDto(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setCode(currency.getCode());
        // Map other currency details as needed
        return currencyDto;
    }

    // Helper method to map a Transaction entity to a TransactionDto
    private TransactionDto mapTransactionToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setSender(mapUserToDto(transaction.getSender()));
        transactionDto.setRecipient(mapUserToDto(transaction.getRecipient()));
        transactionDto.setCurrency(mapCurrencyToDto(transaction.getCurrency()));
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTimestamp(transaction.getTimestamp());
        return transactionDto;
    }

    // Helper method to map a User entity to a UserDto
    private UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        // Map other user details as needed
        return userDto;
    }
}
