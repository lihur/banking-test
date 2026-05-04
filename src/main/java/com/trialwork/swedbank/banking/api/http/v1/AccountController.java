package com.trialwork.swedbank.banking.api.http.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import com.trialwork.swedbank.banking.api.http.v1.model.*;
import com.trialwork.swedbank.banking.api.http.v1.mapper.AccountBalanceResponseMapper;
import com.trialwork.swedbank.banking.command.*;
import com.trialwork.swedbank.banking.command.BalanceRetriever;
import com.trialwork.swedbank.banking.common.validator.iban.ValidIban;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AccountController {

    private final BalanceRetriever balanceRetriever;
    private final Depositor depositor;
    private final Debiter debiter;
    private final Exchanger exchanger;

    @Operation(summary = "Get account balance", description = "Retrieves all balances for the given IBAN across all currencies.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved balances"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid IBAN format",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "AccountNotFoundExample",
                    summary = "Example of an account not found error",
                    value = """
                    {
                      "status": 404,
                      "message": "account not found for {userCode=00000001, iban=EE471000001020145685}",
                      "errors": {
                        "iban": "EE471000001020145685",
                        "userCode": "00000001"
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping(value = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AccountBalanceResponse getBalance(@PathVariable @ValidIban final String iban) {
        log.debug("Rest request to get balances for IBAN: {}", iban);

        final var balances = balanceRetriever.retrieve(iban);

        return AccountBalanceResponseMapper.map(balances);
    }

    @Operation(
        summary = "Add money to account",
        description = "Increases the balance of a specific currency for the given IBAN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deposited"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found. Possible causes: " +
                          "1. Account (IBAN) does not exist; " +
                          "2. Currency not supported. ",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "AccountNotFoundExample",
                    summary = "Example of an account not found error",
                    value = """
                    {
                      "status": 404,
                      "message": "account not found for {userCode=00000001, iban=EE471000001020145685}",
                      "errors": {
                        "iban": "EE471000001020145685",
                        "userCode": "00000001"
                      }
                    }
                    """
                )
            )
        )
    })
    @PostMapping(value = "/{iban}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deposit(
        @PathVariable @ValidIban final String iban,
        @Valid @RequestBody final DepositRequest request
    ) {
        log.debug("Rest request to deposit {} {} to IBAN: {}", request.amount(), request.currency(), iban);

        depositor.deposit(iban, request.currency(), request.amount());
    }

    @Operation(
        summary = "Debit money from account",
        description = "Decreases the balance of a specific currency. Calls an external logging system before processing."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Money successfully debited"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters or insufficient funds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "AccountNotFoundExample",
                    summary = "Example of an account not found error",
                    value = """
                    {
                      "status": 404,
                      "message": "account not found for {userCode=00000001, iban=EE471000001020145685}",
                      "errors": {
                        "iban": "EE471000001020145685",
                        "userCode": "00000001"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Insufficient funds",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "InsufficientFunds",
                    value = """
                            {
                                "status": 422,
                                "message": "Insufficient funds for debit operation"
                            }
                            """
                )
            )
        )
    })
    @PostMapping(value = "/{iban}/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void debit(
        @PathVariable @ValidIban final String iban,
        @Valid @RequestBody final DebitRequest request
    ) {
        log.debug("Rest request to debit {} {} from IBAN: {}", request.amount(), request.currency(), iban);

        debiter.debit(iban, request.currency(), request.amount());
    }

    @Operation(
        summary = "Exchange currency",
        description = "Converts an amount from one currency to another based on fixed exchange rates."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exchange completed successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request, same currency exchange, or insufficient funds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found. Possible causes: " +
                          "1. Account (IBAN) does not exist; " +
                          "2. Currency not supported; " +
                          "3. Exchange rate not defined for the pair.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        name = "AccountNotFoundExample",
                        summary = "Example of an account not found error",
                        value = """
                        {
                          "status": 404,
                          "message": "account not found for {userCode=00000001, iban=EE471000001020145685}",
                          "errors": {
                            "iban": "EE471000001020145685",
                            "userCode": "00000001"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Insufficient funds",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "InsufficientFunds",
                    value = """
                        {
                            "status": 422,
                            "message": "Insufficient funds for debit operation"
                        }
                    """
                )
            )
        )
    })
    @PostMapping(value = "/{iban}/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exchange(
        @PathVariable @ValidIban final String iban,
        @Valid @RequestBody final ExchangeRequest request
    ) {
        log.debug(
            "Rest request to exchange {} {} to {} for IBAN: {}",
            request.amount(),
            request.fromCurrency(),
            request.toCurrency(),
            iban
        );

        exchanger.exchange(iban, request.fromCurrency(), request.toCurrency(), request.amount());
    }
}