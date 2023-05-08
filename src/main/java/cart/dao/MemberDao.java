package cart.dao;

import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password")
        ));
    }

    public Long findIdByEmail(final String email) {
        String sql = "SELECT id FROM member WHERE email LIKE ? ";

        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }
}
