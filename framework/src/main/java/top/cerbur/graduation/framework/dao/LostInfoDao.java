package top.cerbur.graduation.framework.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.cerbur.graduation.framework.bo.LostSearchInputBO;
import top.cerbur.graduation.framework.bo.LostSearchOutputBO;
import top.cerbur.graduation.framework.entity.LostInfo;

import java.util.List;

@Mapper
@Repository
public interface LostInfoDao {
    LostInfo getLostInfoById(Integer id);

    Integer createNewLostInfo(LostInfo lostInfo);

    List<LostSearchOutputBO> searchLost(LostSearchInputBO lostSearchOutputBO);

    Integer deleteLostInfoById(Integer id);

    Integer deleteLostInfoByUserIdAndId(Integer userId, Integer lostId);

    Integer updateLostInfoFoundStatusByUserIdAndId(Integer userId, Integer lostId);

}
