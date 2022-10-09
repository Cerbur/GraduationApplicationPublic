package top.cerbur.graduation.framework.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.cerbur.graduation.framework.bo.LostTypeBO;

import java.util.List;

@Mapper
@Repository
public interface LostTypeDao {
    List<LostTypeBO> getAllType();
}
