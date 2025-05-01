package ru.ssau.lab1new.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;

import ru.ssau.lab1new.model.ProcessFile;
import ru.ssau.lab1new.projection.ProcessFileFullProjection;
import ru.ssau.lab1new.projection.ProcessWithFileCountProjection;

public interface ProcessFileRepository extends Repository<ProcessFile, UUID> 
{
    @NativeQuery(
        """
        select pf.id, p.id as processid, p.name as processname, pf.access, pf.itemid as itemid, fp.fullpath as fullpath
        from processfile pf
            join FullPath(pf.itemid) as fp on pf.itemid = fp.id
            right join process p on pf.processid = p.id 
        """
    )
    List<ProcessFileFullProjection> readAllProcessFiles();

    @NativeQuery(
        """
        with fileaccess as (
            SELECT 
                pg_enum.enumlabel as access
            FROM pg_enum
            JOIN pg_type ON pg_type.oid = pg_enum.enumtypid
            WHERE pg_type.typname = 'fileaccesstype'
        )
        select p.id as processid, p.name as processname, fa.access as access, count(pf.access) as filesnumber
        from process p 
            cross join fileaccess fa
            left join processfile pf on pf.processid = p.id and fa.access = pf.access::name
        group by p.id, p.name, fa.access,pf.access	
        order by p.id
        """
    )
    List<ProcessWithFileCountProjection> countAllProcessFiles();

    @NativeQuery(
        """
        with fileaccess as (
            SELECT 
                pg_enum.enumlabel as access
            FROM pg_enum
            JOIN pg_type ON pg_type.oid = pg_enum.enumtypid
            WHERE pg_type.typname = 'fileaccesstype'
        )
        select p.id as processid, p.name as processname, fa.access as access, count(pf.access) as filesnumber
        from process p 
            cross join fileaccess fa
            left join processfile pf on pf.processid = p.id and fa.access = pf.access::name
        group by p.id, p.name, fa.access,pf.access	
        having fa.access = :access
        order by p.id
        """
    )
    List<ProcessWithFileCountProjection> countAllTypeProcessFiles(String access);

    @NativeQuery(
        """
        with recursive 
        ChildProcess as (
            select p.*, 1 as depth from process as p where p.id = :id
            union 
            select p.*, depth + 1
            from ChildProcess as cp join process as p on p.parentid = cp.id
        )
        select pf.id, p.id as processid, p.name as processname, pf.access, pf.itemid as itemid, fp.fullpath as fullpath
        from processfile pf
            join FullPath(pf.itemid) as fp on pf.itemid = fp.id
            right join ChildProcess p on pf.processid = p.id
        """
    )
    List<ProcessFileFullProjection> readProcessFiles(UUID id);

    @NativeQuery(
        """
        with 
        File as
        (
            select i.*, eip.fullpath as path
            from ExistingItemPath(:path) as eip join item as i on i.id = eip.id
            where array_length(eip.fullpath, 1) = array_length(:path, 1)
            order by array_length(eip.fullpath, 1) desc
            limit 1
        ),
        InsertFiles as
        (
            insert into processfile (processid, itemid, access)
            select 
                CAST(:processId as uuid) as processid, 
                f.id as itemid,
                cast(unnest(CAST(:accesses AS text[])) as FileAccessType) as access
            from File as f
            returning *
        )
        select pf.id, p.id as processid, p.name as processname, pf.access, pf.itemid as itemid, fp.fullpath as fullpath
        from InsertFiles pf
            join FullPath(pf.itemid) as fp on pf.itemid = fp.id
            join process p on pf.processid = p.id
        """
    )
    List<ProcessFileFullProjection> createProcessFiles(UUID processId, String[] path, String[] accesses);

    @NativeQuery(
        """
        with ItemDelete as
        (
            delete from processfile
            where id = :id
            returning *
        )
        select pf.id, p.id as processid, p.name as processname, pf.access, pf.itemid as itemid, fp.fullpath as fullpath
        from ItemDelete pf
            join FullPath(pf.itemid) as fp on pf.itemid = fp.id
            join process p on pf.processid = p.id
        """
    )
    ProcessFileFullProjection removeProcessFile(UUID id);
}
