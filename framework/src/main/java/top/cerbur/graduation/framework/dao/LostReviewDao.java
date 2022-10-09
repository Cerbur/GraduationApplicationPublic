package top.cerbur.graduation.framework.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.cerbur.graduation.framework.bo.LostReviewBO;
import top.cerbur.graduation.framework.entity.LostReview;

import java.util.List;

@Mapper
@Repository
public interface LostReviewDao {
    Integer insertLostReview(LostReview lostReview);

    List<LostReviewBO> searchReview(LostReviewBO lostReviewBO);

    Integer deleteLostReviewById(Integer id);

    Integer deleteLostReviewByIdAndUser(Integer userId, Integer reviewId);

}
