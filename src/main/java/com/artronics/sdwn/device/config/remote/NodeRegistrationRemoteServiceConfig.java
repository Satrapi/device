package com.artronics.sdwn.device.config.remote;

import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.device.config.SdwnControllerEntityBeanConfig;
import com.artronics.sdwn.domain.entities.SdwnControllerEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import javax.annotation.PostConstruct;

@Configuration
@Import(SdwnControllerEntityBeanConfig.class)
public class NodeRegistrationRemoteServiceConfig
{
    private final static Logger log = Logger.getLogger(NodeRegistrationRemoteServiceConfig.class);

    private NodeRegistrationService nodeRegistrationService;

    @Autowired
    private SdwnControllerEntity controllerEntity;

    @PostConstruct
    public void initBean(){
        this.nodeRegistrationService = createNodeRegistrationRemoteService();
    }

    @Bean
    public NodeRegistrationService getNodeRegistrationService()
    {
        return nodeRegistrationService;
    }

    private NodeRegistrationService createNodeRegistrationRemoteService(){
        String serviceUrl = controllerEntity.getUrl()+"/registerNode";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(NodeRegistrationService.class);
        pb.afterPropertiesSet();
        NodeRegistrationService s = (NodeRegistrationService) pb.getObject();
        log.debug("Fetching Remote Service: "+s.toString());

        return s;
    }
}
