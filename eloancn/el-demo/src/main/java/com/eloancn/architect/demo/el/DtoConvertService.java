package com.eloancn.architect.demo.el;

import com.eloancn.architect.dto.NameDto;
import com.eloancn.architect.model.Name;
import com.eloancn.framework.utils.BeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DtoConvertService model转换成dto的服务类
 * Created by qinxf on 2017/11/6.
 */
@Service
public class DtoConvertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DtoConvertService.class);

    /**
     * List<Name> convert to List<NameDto>
     * @param list
     * @return
     */
    public List<NameDto> convert(List<Name> list){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DtoConvertService.convert=>List<Name> convert to List<NameDto> start!");
        }
        if(list == null || list.size() == 0){
            return null;
        }
        List<NameDto> result = BeanMapper.mapList(list, NameDto.class);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DtoConvertService.convert=>List<Name> convert to List<NameDto> end!");
        }
        return result;
    }

    /**
     * Name convert to NameDto
     * @param name
     * @return
     */
    public NameDto convert(Name name){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DtoConvertService.convert=>Name convert to NameDto start!");
        }
        if(name == null){
            return null;
        }
        NameDto result = BeanMapper.map(name, NameDto.class);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=DtoConvertService.convert=>Name convert to NameDto end!");
        }
        return result;
    }
}
