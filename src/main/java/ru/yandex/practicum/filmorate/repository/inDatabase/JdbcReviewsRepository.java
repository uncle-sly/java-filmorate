package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcReviewsRepository extends JdbcBaseRepository<Review> implements ReviewsRepository {
    private final JdbcTemplate jdbc;

    public JdbcReviewsRepository(NamedParameterJdbcOperations jdbc,
                                 RowMapper<Review> mapper,
                                 JdbcTemplate jdbcTemplate) {
        super(jdbc, mapper);
        this.jdbc = jdbcTemplate;
    }

    public Review create(Review review) {

        String sql = """
                INSERT INTO reviews (content, isPositive, userId, filmId)
                VALUES (:content, :isPositive, :userId, :filmId);
                """;

        Map<String, Object> params = Map.of("content", review.getContent(),
                "isPositive", review.getIsPositive(),
                "userId", review.getUserId(),
                "filmId", review.getFilmId());

        long reviewId = insert(sql, params);

        return get(reviewId).get();
    }

    public Optional<Review> get(long reviewId) {
        String sql = """
                SELECT reviewId,
                       content,
                       isPositive,
                       userId,
                       filmId
                FROM reviews
                WHERE reviewId = :reviewId;
                """;

        Map<String, Object> params = Map.of("reviewId", reviewId);

        return findOne(sql, params);
    }

    public Review update(Review review) {
        String sql = """
                UPDATE reviews SET content = :content,
                                   isPositive = :isPositive,
                                   userId = :userId,
                                   filmId = :filmId
                WHERE reviewId = :reviewId;
                """;
        Map<String, Object> params = Map.of("content", review.getContent(),
                "isPositive", review.getIsPositive(),
                "userId", review.getUserId(),
                "filmId", review.getFilmId(),
                "reviewId", review.getReviewId());

        update(sql, params);

        return get(review.getReviewId()).get();
    }

    public void delete(long id) {
        String sql = """
                DELETE FROM reviews
                WHERE reviewId = :reviewId;
                """;

        Map<String, Object> params = Map.of("reviewId", id);
        delete(sql, params);
    }

    public List<Review> getSome(Optional<Long> filmId, Optional<Long> count) {
        String sql = """
                SELECT reviewId,
                       content,
                       isPositive,
                       userId,
                       filmId
                FROM reviews
                """;
        Map<String, Object> params = new HashMap<>();
        if (filmId.isPresent()) {
            sql = sql + "\nWHERE filmId = :filmId";
            params.put("filmId", filmId.get());
        }
        if (count.isPresent()) {
            sql = sql + "\nLIMIT :count";
            params.put("count", count.get());
        }
        sql = sql + ";";

        return findMany(sql, params);
    }


}
