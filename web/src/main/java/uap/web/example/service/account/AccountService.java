package uap.web.example.service.account;

import com.yzqc.support.shiro.Account;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.CustomSearchFilter;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;
import uap.web.example.entity.MgrUser;
import uap.web.example.repository.MgrFunctionJdbcDao;
import uap.web.example.repository.MgrUserDao;
import uap.web.service.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * 用户管理类.
 *
 * @author calvin
 */
// Spring Service Bean的标识.
@Service
@Transactional
public class AccountService {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private MgrUserDao userDao;

    @Autowired
    private MgrFunctionJdbcDao funcDao;

    private Clock clock = Clock.DEFAULT;

    public List<MgrUser> getAllUser() {
        return (List<MgrUser>) userDao.findAll();
    }

    public MgrUser getUser(Long id) {
        return userDao.findOne(id);
    }

    public MgrUser findUserByLoginName(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    public void registerUser(MgrUser user) {
        entryptPassword(user);
        user.setRoles("user");
        user.setRegisterDate(clock.getCurrentDate());

        userDao.save(user);
    }

    public boolean registerCustomUser(MgrUser user) {
        try {
            entryptPassword(user);
            user.setRoles("user");
            user.setRegisterDate(clock.getCurrentDate());
            userDao.save(user);
            funcDao.addRoleUser(user.getId());

        } catch (Exception e) {
            logger.error("注册用户失败!");
            return false;
        }

        return true;
    }

    @Account(operationType = Account.OperationType.UPDATE)
    public void updateUser(MgrUser user) {
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
        }
        userDao.save(user);
    }

    @Account(operationType = Account.OperationType.REMOVE)
    public void deleteUser(Long id) {
        if (isSupervisor(id)) {
            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        userDao.delete(id);

    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        return (String)SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(MgrUser user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    /**
     * 创建动态查询条件组合.
     */
    public Specification<MgrUser> buildSpecification(Map<String, Object> searchParams) {
        Map<String, CustomSearchFilter> filters = CustomSearchFilter.parse(searchParams);
        Specification<MgrUser> user = DynamicSpecifications.bySearchFilter(filters.values(), MgrUser.class);
        return user;
    }

    public Page<MgrUser> getAccountPage(Map<String, Object> searchParams, PageRequest pageRequest) {
        Specification<MgrUser> spec = buildSpecification(searchParams);
        return userDao.findAll(spec, pageRequest);
    }

    @Transactional
    public MgrUser saveEntity(MgrUser entity) throws Exception {
        entryptPassword(entity);
        if (0 == entity.getId()) {
            entity.setId(userDao.getNextId());
            entity.setRegisterDate(clock.getCurrentDate());
        }
        //funcDao.addRoleUser(entity.getId());
        return userDao.save(entity);
    }

    @Autowired
    public void setUserDao(MgrUserDao userDao) {
        this.userDao = userDao;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
