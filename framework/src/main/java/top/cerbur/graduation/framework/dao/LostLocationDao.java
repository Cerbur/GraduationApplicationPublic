package top.cerbur.graduation.framework.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.cerbur.graduation.framework.bo.LostLocationBO;

import java.util.List;

/**
 * @author cerbur
 */
@Mapper
@Repository
public interface LostLocationDao {
    List<LostLocationBO> getAllLocation();
}
