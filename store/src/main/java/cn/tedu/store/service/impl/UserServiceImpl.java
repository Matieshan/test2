package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.exception.DuplicateKeyException;
import cn.tedu.store.service.exception.InsertException;

/**
 * 处理用户数据的业务层实现类
 */
@Service
public class UserServiceImpl 
	implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User reg(User user) throws DuplicateKeyException, InsertException {
		// 根据尝试注册的用户名查询用户数据
		User data = findByUsername(
				user.getUsername());
		// 判断查询到的数据是否为null
		if (data == null) {
			// 是：用户名不存在，允许注册，则
			// 【补充非用户提交的数据】
			// 是否已经删除：否
			user.setIsDelete(0); 
			// 4项日志
			Date now = new Date();
			user.setCreatedUser(user.getUsername());
			user.setCreatedTime(now);
			user.setModifiedUser(user.getUsername());
			user.setModifiedTime(now);
			// -----------------------
			// 【处理密码加密】
			// 加密-1：获取随机的UUID作为盐值
			String salt = UUID.randomUUID().toString().toUpperCase();
			// 加密-2：获取用户提交的原始密码
			String srcPassword = user.getPassword();
			// 加密-3：基于原始密码和盐值执行加密，获取通过MD5加密的密码
			String md5Password = getMd5Password(srcPassword, salt);
			// 加密-4：将加密后的密码封装在user对象中
			user.setPassword(md5Password);
			// 加密-5：将盐值封装在user对象中
			user.setSalt(salt);
			// 执行注册
			addnew(user);
			// 返回注册的用户对象
			return user;
		} else {
			// 否：用户名已被占用，抛出DuplicateKeyException异常
			throw new DuplicateKeyException(
				"注册失败！尝试注册的用户名(" + user.getUsername() + ")已经被占用！");
		}
	}
	
	/**
	 * 获取根据MD5加密的密码
	 * @param srcPassword 原密码
	 * @param salt 盐值
	 * @return 加密后的密码
	 */
	private String getMd5Password(
			String srcPassword, String salt) {
		// 【注意】以下加密规则是自由设计的
		// ----------------------------
		// 盐值 拼接 原密码 拼接 盐值
		String str = salt + srcPassword + salt;
		// 循环执行10次摘要运算
		for (int i = 0; i < 10; i++) {
			str = DigestUtils
				.md5DigestAsHex(str.getBytes())
					.toUpperCase();
		}
		// 返回摘要结果
		return str;
	}
	
	/**
	 * 插入用户数据
	 * @param user 用户数据
	 * @throws InsertException
	 */
	private void addnew(User user) {
		Integer rows = userMapper.addnew(user);
		if (rows != 1) {
			throw new InsertException(
				"增加用户数据时出现未知错误！");
		}
	}
	
	/**
	 * 根据用户名查询用户数据
	 * @param username 用户名
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	private User findByUsername(String username) {
		return userMapper.findByUsername(username);
	}
}





