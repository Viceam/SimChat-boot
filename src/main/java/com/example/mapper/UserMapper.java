package com.example.mapper;

import com.example.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into tb_user values(null, #{username}, #{password})")
    int save(User user);

    @Select("select * from tb_user where username=#{username}")
    User selectByUserName(String username);
}
