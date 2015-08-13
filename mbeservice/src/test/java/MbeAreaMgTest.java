import com.yzqc.crm.mbe.repository.MbeAreaMgRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by Administrator on 2015/8/10.
 */
@ContextConfiguration(locations = "/applicationContext.xml")
public class MbeAreaMgTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MbeAreaMgRepository areaRepository;

    @Test
    public void testQuery() {
        System.out.println(areaRepository.queryPage("3", new PageRequest(0, 30)));

    }

}
