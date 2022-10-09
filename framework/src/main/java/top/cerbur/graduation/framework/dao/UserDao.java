package top.cerbur.graduation.framework.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.entity.User;

import java.util.List;

/**
 * @author cerbur
 */
@Mapper
@Repository
public interface UserDao {
    User getUserById(Integer id);

    Integer createNewUser(User user);

    User getUserByOpenId(String openId);

    User searchUserBySchoolIdAndName(User user);

    List<String> getSchoolIdByClassName(String className);

    Integer updateUserInfo(UserBO userBO);
}
