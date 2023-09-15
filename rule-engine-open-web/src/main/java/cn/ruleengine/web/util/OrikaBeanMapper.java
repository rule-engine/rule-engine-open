package cn.ruleengine.web.util;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 〈BeanMapper〉
 *
 * @author 丁乾文
 * @date 2023/9/15 23:07
 * @since 1.0.0
 */
public class OrikaBeanMapper {

    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    public static <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return MAPPER_FACTORY.getMapperFacade().map(sourceObject, destinationClass);
    }

    public static <S, D> D map(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return MAPPER_FACTORY.getMapperFacade().map(sourceObject, destinationClass, context);
    }

    public static <S, D> List<D> mapList(Collection<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            destinationList.add(map(source, destinationClass));
        }
        return destinationList;
    }

    public static <S, D> List<D> mapList(Collection<S> sourceList, Class<D> destinationClass, MappingContext context) {
        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            destinationList.add(map(source, destinationClass, context));
        }
        return destinationList;
    }

    public static <S, D> void mapCollection(Collection<S> sourceCollection, Collection<D> destinationCollection, Class<D> destinationClass) {
        for (S source : sourceCollection) {
            destinationCollection.add(map(source, destinationClass));
        }
    }

    public static <S, D> void mapCollection(Collection<S> sourceCollection, Collection<D> destinationCollection, Class<D> destinationClass, MappingContext context) {
        for (S source : sourceCollection) {
            destinationCollection.add(map(source, destinationClass, context));
        }
    }

}
