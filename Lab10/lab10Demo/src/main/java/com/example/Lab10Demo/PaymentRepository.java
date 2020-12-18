package com.example.Lab10Demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {

	private final NamedParameterJdbcTemplate template;

	private static final RowMapper<UserModel> userMapper = new RowMapper<UserModel>() {

		@Override
		public UserModel mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			return new UserModel(resultSet.getString("id"),
					resultSet.getString("firstName"),
					resultSet.getString("lastName"),
					resultSet.getString("email"),
					resultSet.getString("password"));
		}
	};
	
	private static final RowMapper<AccountModel> accountMapper = new RowMapper<AccountModel>() {

		@Override
		public AccountModel mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			return new AccountModel(resultSet.getString("id"),
					resultSet.getString("userId"),
					AccountModel.Currency.valueOf(resultSet.getString("currency")),
					resultSet.getDouble("amount"));
		}
	};
	
    public PaymentRepository(final DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
    }
    
    public Optional<UserModel> getUser(String email) {
    	String sql = "select id, first_name, last_name, email, password from users where email = :email";
    	MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
    	return template.query(sql, parameters, userMapper).stream().findFirst();
    }
    
    public List<AccountModel> getUserAccounts(String userId) {
    	String sql = "select id, user_id, currency, amount from users where user_id = :user_id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("user_id", userId);
    	return template.query(sql, parameters, accountMapper);
    }
    
    public boolean savePayment(PaymentModel payment) {
    	String sql = "insert into payments (id, sender_account_id, receiver_account_id, amount) "
    			+ "values (:id, :sender_account_id, :receiver_account_id, :amount)";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", UUID.randomUUID().toString())
				.addValue("sender_account_id", payment.getSenderAccountId())
				.addValue("receiver_account_id", payment.getReceiverAccountId())
				.addValue("amount", payment.getAmount());
    	if (template.update(sql, parameters) != 1) {
    		return false;
    	}
    	sql = "update accounts set amount = amount - :amount where id = :id";
    	parameters = new MapSqlParameterSource()
    			.addValue("id", payment.getSenderAccountId())
				.addValue("amount", payment.getAmount());
		boolean senderAccountUpdated = template.update(sql, parameters) == 1;

		sql = "update accounts set amount = amount + :amount where id = :id";
		parameters = new MapSqlParameterSource()
				.addValue("id", payment.getReceiverAccountId())
				.addValue("amount", payment.getAmount());
		boolean receiverAccountUpdated = template.update(sql, parameters) == 1;

		return senderAccountUpdated && receiverAccountUpdated;
    }
}
