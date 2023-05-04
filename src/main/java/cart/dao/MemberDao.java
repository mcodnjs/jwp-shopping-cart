package cart.dao;

import cart.domain.Member;
import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public List<Product> findProductByEmail(final String email) {
        String sql = "SELECT product.id, name, image_url, price FROM cart JOIN product ON cart.product_id = product.id WHERE member_id = (SELECT id FROM member WHERE email LIKE ?)";
        return jdbcTemplate.query(sql,
                ps -> ps.setString(1, email),
                (rs, rowNum) -> new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("image_url"),
                        rs.getInt("price")
                ));
    }

    public Long findByEmail(final String email) {
        String sql = "SELECT id FROM member WHERE email LIKE ? ";

        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }


    public void save(final Long memberId, final Long productId) {
        String sql = "INSERT INTO cart (member_id, product_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, memberId, productId);
    }

    public void delete(final Long memberId, final Long productId) {
        String sql = "DELETE FROM cart WHERE member_id = ? and product_id = ?";

        jdbcTemplate.update(sql, memberId, productId);
    }

    public Optional<Long> findByIds(final Long memberId, final Long productId) {
        String sql = "SELECT id FROM cart WHERE member_id = ? and product_id = ?";

        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, memberId, productId);
            return Optional.of(id);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
