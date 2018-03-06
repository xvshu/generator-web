package com.eloancn.architect.demo.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.eloancn.architect.api.NameService;
import com.eloancn.architect.dto.NameDto;
import com.eloancn.framework.sevice.api.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * DubboConsumerService Dubbo消费者
 * Created by qinxf on 2017/11/6.
 */
@Service
public class DubboConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboConsumerService.class);

    /**
     * 调用dubbo服务，获取对应的Name对象
     * @param id
     * @return
     */
    public ResultDTO<NameDto> getNameById(Long id){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DubboConsumerService.getNameById=>get name[id={}] from dubbo service start!",id);
        }
        if(id == null || id <= 0){
            return null;
        }
        ResultDTO<NameDto> result = this.getNameServiceByDubboo().getNameById(id);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DubboConsumerService.getNameById=>get name[id={}] from dubbo service end!",id);
        }
        return result;
    }

    /**
     * 硬编码dubbo消费者，获取NameService对象
     * @return NameService
     */
    public NameService getNameServiceByDubboo(){
        ApplicationContext context = ServiceBean.getSpringContext();
        RegistryConfig registryConfig = (RegistryConfig) context.getBean("elZookeeper");
        String zkUrl = registryConfig.getAddress();
        if(StringUtils.isEmpty(zkUrl)){
            return null;
        }else {
            ApplicationConfig application = new ApplicationConfig();
            application.setName("dubbo.xxx");
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol("zookeeper");
            registry.setAddress(zkUrl);
            registry.setCheck(false);

            ReferenceConfig<NameService> rc = new ReferenceConfig<NameService>();
            rc.setApplication(application);
            rc.setRegistry(registry);
            rc.setInterface(NameService.class.getName());
            rc.setVersion("1.0-SNAPSHOT");
            rc.setGroup("el-demo");
            rc.setProtocol("dubbo");
            rc.setTimeout(1000);

            return rc.get();
        }
    }
}
