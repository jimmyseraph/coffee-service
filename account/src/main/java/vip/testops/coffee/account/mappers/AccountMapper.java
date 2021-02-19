package vip.testops.coffee.account.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import vip.testops.coffee.account.entities.dto.AccountDTO;

/**
 * account object mapper to table t_account
 * this is a mybatis mapper file implements with annotation
 * @version 1.0
 * @author liudao
 */
@Mapper
public interface AccountMapper {
    /**
     * select the account info from t_account in the database
     * @param accountName account name
     * @return account info object
     */
    @Select("select * from t_account where accountName = #{accountName}")
    AccountDTO getUserByName(String accountName);

    /**
     * insert a new account into t_account
     * @param accountDTO account info
     * @return insert number
     */
    @Insert("insert into t_account values(" +
            "null," +
            "#{accountName}," +
            "#{salt}," +
            "#{password}," +
            "#{cellphone}," +
            "#{gender}," +
            "#{createTime}," +
            "#{lastLoginTime})")
    int addAccount(AccountDTO accountDTO);

    /**
     * update account info in the table
     * @param accountDTO account data object
     * @return update number
     */
    @Update("update t_account set " +
            "password = #{password}," +
            "cellphone = #{cellphone}," +
            "gender = #{gender}," +
            "lastLoginTime = #{lastLoginTime} " +
            "where accountId = #{accountId}")
    int updateAccountById(AccountDTO accountDTO);

}
