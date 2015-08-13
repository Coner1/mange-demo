import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import com.yzqc.crm.mbe.repository.MbeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/5.
 */
@ContextConfiguration(locations = "/applicationContext.xml")
public class MbeRespositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MbeRepository mbeRepository;

    @Test
    public void testSave() {
        List<MbeGroupmg> list = new ArrayList<MbeGroupmg>(100);
        for (int i = 0; i < 100; i++) {
            MbeGroupmg groupmg = new MbeGroupmg();
            groupmg.setCode("123");
            groupmg.setCreater(BigInteger.ONE);
            groupmg.setCreatetime(new Date());
            groupmg.setDescription("description" + i);
            groupmg.setDr(0);
            groupmg.setName("name" + i);
            groupmg.setTs(new Date());
            groupmg.setVstatus(1);
            list.add(groupmg);
        }
        mbeRepository.save(list);
    }

    @Test
    public void testUpdate() {
        MbeGroupmg mbeGroupmg = mbeRepository.findOne(1L);
        mbeGroupmg.setName("bbbb");
        mbeRepository.update("vvv", "1111", "bbb", new Date(), 1L, mbeGroupmg.getTs());

    }

}
