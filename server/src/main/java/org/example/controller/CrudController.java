package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DependencyProvider;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.example.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrudController extends HttpServlet {

    public static final String API_PREFIX = ".*/api/v1/crud";

    public static final String CREATE_USER = API_PREFIX + "/users$";
    public static final String GET_USER = API_PREFIX + "/users/(\\d+)";
    public static final String UPDATE_USER = API_PREFIX + "/users/(\\d+)";
    public static final String DELETE_USER = API_PREFIX + "/users/(\\d+)";

    public static final String CREATE_ACCOUNT = API_PREFIX + "/accounts$";
    public static final String GET_ACCOUNT = API_PREFIX + "/accounts/(\\d+)";
    public static final String UPDATE_ACCOUNT = API_PREFIX + "/accounts/(\\d+)";
    public static final String DELETE_ACCOUNT = API_PREFIX + "/accounts/(\\d+)";

    public static final String CREATE_BANK = API_PREFIX + "/banks$";
    public static final String GET_BANK = API_PREFIX + "/banks/(\\d+)";
    public static final String UPDATE_BANK = API_PREFIX + "/banks/(\\d+)";
    public static final String DELETE_BANK = API_PREFIX + "/banks/(\\d+)";

    private ObjectMapper objectMapper;
    private UserService userService;
    private AccountService accountService;
    private BankRepository bankRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectMapper = (ObjectMapper) DependencyProvider.get().forClass(ObjectMapper.class);
        userService = (UserService) DependencyProvider.get().forClass(UserService.class);
        accountService = (AccountService) DependencyProvider.get().forClass(AccountService.class);
        bankRepository = (BankRepository) DependencyProvider.get().forClass(BankRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI();

        if (path.matches(GET_USER)) {
            Pattern pattern = Pattern.compile(GET_USER);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));
                getUser(id, response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(GET_ACCOUNT)) {
            Pattern pattern = Pattern.compile(GET_ACCOUNT);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));
                getAccount(id, response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(GET_BANK)) {
            Pattern pattern = Pattern.compile(GET_BANK);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Integer id = Integer.valueOf(matcher.group(1));
                getBank(id, response);
            } else {
                response.sendError(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI();
        final String message = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        if (path.matches(CREATE_USER)) {
            User user = objectMapper.readValue(message, User.class);
            user = userService.addUser(user);

            getUser(user.getId(), response);
        }

        if (path.matches(CREATE_ACCOUNT)) {
            Account account = objectMapper.readValue(message, Account.class);
            account = accountService.createAccount(account);

            getUser(account.getId(), response);
        }

        if (path.matches(CREATE_BANK)) {
            Bank bank = objectMapper.readValue(message, Bank.class);
            bank = bankRepository.create(bank);

            getBank(bank.getId(), response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI();
        final String message = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        if (path.matches(UPDATE_USER)) {
            Pattern pattern = Pattern.compile(UPDATE_USER);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));

                User user = objectMapper.readValue(message, User.class);
                user = userService.updateUser(id, user);

                getUser(user.getId(), response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(UPDATE_ACCOUNT)) {
            Pattern pattern = Pattern.compile(UPDATE_ACCOUNT);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));

                Account account = objectMapper.readValue(message, Account.class);
                account = accountService.updateAccount(id, account);

                getAccount(account.getId(), response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(UPDATE_BANK)) {
            Pattern pattern = Pattern.compile(UPDATE_BANK);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Integer id = Integer.valueOf(matcher.group(1));

                Bank bank = objectMapper.readValue(message, Bank.class);
                bank = bankRepository.update(id, bank);

                getBank(bank.getId(), response);
            } else {
                response.sendError(400);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI();

        if (path.matches(DELETE_USER)) {
            Pattern pattern = Pattern.compile(DELETE_USER);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));
                deleteUser(id, response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(DELETE_ACCOUNT)) {
            Pattern pattern = Pattern.compile(DELETE_ACCOUNT);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));
                deleteAccount(id, response);
            } else {
                response.sendError(400);
            }
        }

        if (path.matches(DELETE_BANK)) {
            Pattern pattern = Pattern.compile(DELETE_BANK);
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                Long id = Long.valueOf(matcher.group(1));
                deleteBank(id, response);
            } else {
                response.sendError(400);
            }
        }
    }

    private void deleteUser(Long id, HttpServletResponse response) {
        userService.deleteById(id);
    }

    private void deleteAccount(Long id, HttpServletResponse response) {
        accountService.deleteById(id);
    }

    private void deleteBank(Long id, HttpServletResponse response) {
        bankRepository.deleteById(id);
    }

    private void send(String message, HttpServletResponse response) {
        final PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(message);
        out.flush();
    }

    private void getUser(Long id, HttpServletResponse response) throws JsonProcessingException {
        final User user = userService.findById(id);
        final String body = objectMapper.writeValueAsString(user);
        send(body, response);
    }

    private void getAccount(Long id, HttpServletResponse response) throws JsonProcessingException {
        final Account account = accountService.findById(id);
        final String body = objectMapper.writeValueAsString(account);
        send(body, response);
    }

    private void getBank(Integer id, HttpServletResponse response) throws JsonProcessingException {
        final Bank bank = bankRepository.findById(id);
        final String body = objectMapper.writeValueAsString(bank);
        send(body, response);
    }
}
