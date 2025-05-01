package ru.ssau.lab1new.repository;

import java.util.List;
import java.util.UUID;
import ru.ssau.lab1new.model.Process;
import ru.ssau.lab1new.projection.ProcessDeleteProjection;
import ru.ssau.lab1new.projection.ProcessFullPathProjection;
import ru.ssau.lab1new.projection.ProcessProjection;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;

public interface ProcessRepository extends Repository<Process, UUID> 
{
    @NativeQuery(
        """
        select p.*
        from process as p
        where p.id = :id
        """
    )
    ProcessProjection findProcess(UUID id);

    @NativeQuery(
        """
        with recursive 
            ProcessPath as
            (
                select *, ARRAY[p.id] as path 
                from process p
                where p.parentid is null
                union
                select p.*, path || p.id
                from ProcessPath as pp join process as p on p.parentid = pp.id
            )
        select p.*, fp.fullpath
        from ProcessPath p join FullPath(p.sourcefile) as fp on p.sourcefile = fp.id
        ORDER BY p.path;
        """
    )
    List<ProcessFullPathProjection> findProcesses();

    @NativeQuery(
        """
        with SourceFile as
        (
            select i.*, eip.fullpath as path
            from ExistingItemPath(:path) as eip join item as i on i.id = eip.id
            where array_length(eip.fullpath, 1) = array_length(:path, 1)
            order by array_length(eip.fullpath, 1) desc
            limit 1
        )
        INSERT INTO Process (name, parentid, arguments, sourcefile) 
        select :name, :parentid, :arguments, sf.id
        from SourceFile as sf
        returning *     
        """
    )
    ProcessProjection createProcess(String name, UUID parentid, String[] path, String[] arguments);

    @NativeQuery(
        """
        with recursive
            ChildProcess as (
                select p.*, 1 as depth from process as p where p.id = :id
                union 
                select p.*, depth + 1
                from ChildProcess as cp join process as p on p.parentid = cp.id
            ),
            ChildProcessFiles as (
                select pf.* 
                from ChildProcess as cp 
                join processfile as pf on pf.processid = cp.id
            ),
            DeleteFiles as (
                delete from processfile as pf
                USING ChildProcessFiles as cpf
                where cpf.id = pf.id
                returning
                    pf.id AS file_id,
                    pf.processid
            ),
            DeleteProcess as (
                delete from process as p
                USING ChildProcess as cp
                where cp.id = p.id
                returning 
                    p.id,
                    p.name,
                    p.parentid,
                    p.arguments,
                    p.sourcefile,
                    p.startedat
            )
        SELECT 
            dp.id,
            dp.name,
            dp.parentid,
            dp.arguments,
            dp.sourcefile,
            dp.startedat,
            count(df.processid) AS files
        FROM DeleteProcess dp 
        left JOIN DeleteFiles df ON dp.id = df.processid
        GROUP BY 
            dp.id, 
            dp.name, 
            dp.parentid, 
            dp.arguments, 
            dp.sourcefile, 
            dp.startedat;
        """
    )
    List<ProcessDeleteProjection> removeProcess(UUID id);
}
