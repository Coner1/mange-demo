package uap.web.dubbo;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import com.yzqc.crm.mbe.repository.MbeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Date;

/**
 * Created by Administrator on 2015/8/5.
 */
@ContextConfiguration(locations = "/applicationContext.xml")
public class TestDubbo extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MbeRepository repository;

    @Test
    public void testPaget() {
        MbeGroupmg mbeGroupmg = repository.findOne(1L);
        mbeGroupmg.setName("bbbb");
        System.out.println(repository.update("vvv", "1111", "bbb", new Date(), 1L, new Date()));
    }


}
