package top.yqingyu.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
  * description: 方便Springboot的使用
  *
  * @author yqingyu
  * DATE 2021/11/19
  */
public class SpringBootUtils {

    /**
     *  动态合并MyBatis的XML文件路径
     *
      * @param paths mapper.xml 的映射路径
     * @return Resource
     */
    public static Resource[] mergeMyBatisPathMatchingArray(String ...paths) throws IOException {

        Resource[] resources = new Resource[2];
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();

        for (String path : paths) {
            ArrayUtils.addAll(resources,pathResolver.getResources(path));
        }

        return resources;
    }

}
