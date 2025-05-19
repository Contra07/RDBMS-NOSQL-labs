package ru.ssau.shared.pojo;

public interface PojoFactory<E, P>
{
    E toEntity(P pojo) throws PojoConversionException;
    P fromEntity(E entity) throws PojoConversionException;
}