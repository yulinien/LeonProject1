package com.example.leonproject.dao.mybatis;

import com.example.leonproject.dao.entity.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
    @Select("SELECT * FROM account WHERE username = #{username}")
    AccountDO getUser(String username);

}
