package net.iotsite.wekit.message.mapper;

import net.iotsite.wekit.message.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<UserInfo, Integer> {

    /**
     * 查询用户
     *
     * @param openId
     * @return
     */
    UserInfo queryByUserOpenId(String openId);

    boolean existsByUserOpenId(String openId);

    UserInfo queryByUserSecretKey(String secretKey);
}
