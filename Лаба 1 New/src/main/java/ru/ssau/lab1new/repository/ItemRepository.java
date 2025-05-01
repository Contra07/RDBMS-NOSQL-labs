package ru.ssau.lab1new.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab1new.model.Item;
import ru.ssau.lab1new.projection.ItemCompareProjection;
import ru.ssau.lab1new.projection.ItemFullPathProjection;
import ru.ssau.lab1new.projection.ItemProjection;

public interface ItemRepository extends Repository<Item, UUID>
{   
    @NativeQuery(
        """
        with 
            --Последовательная иерархия до элемента
            recursive PathToItem as
            (
                select i.*, CAST(:path AS text[]) as targetpath, ARRAY[i."name"]::text[] as fullpath, 1 as depth
                from item i 
                where i.parentid is null
                union
                select 
                    i.*, 
                    targetpath,
                    fullpath || i."name"::text,
                    depth + 1
                from PathToItem pti join item i on i.parentid = pti.id 
                where targetpath[depth+1] = i."name"::text
            ),
            --Только требуемый элемент, если такой существует
            ItemPath as
            (
                select *
                from PathToItem as pti
                where depth = array_length(targetpath, 1)
            ),
            --Элемент файл или внутренние элементы папки
            ItemLevelPath as
            (
                select i.*, fullpath
                from ItemPath as pti join item as i on pti.id = i.id
                where i.isfolder = false 
                union 
                select i.*, fullpath || i."name"::text
                from ItemPath as pti join item as i on pti.id = i.parentid
            )
        select * from ItemLevelPath order by name
        """
    )
    List<ItemFullPathProjection> findLevelItems(@Param("path") String[] path);

    @NativeQuery(
        """
        with 
            --Последовательная иерархия до элемента
            recursive PathToItem as
            (
                select i.*, CAST(:path AS text[]) as targetpath, ARRAY[i."name"]::text[] as fullpath, 1 as depth
                from item i 
                where i.parentid is null
                union
                select 
                    i.*, 
                    targetpath,
                    fullpath || i."name"::text,
                    depth + 1
                from PathToItem pti join item i on i.parentid = pti.id 
                where targetpath[depth+1] = i."name"::text
            ),
            --Только требуемый элемент, если такой существует
            ItemPath as
            (
                select *
                from PathToItem as pti
                where depth = array_length(targetpath, 1)
            ),
            --Элемент файл или полная иерархия папки
            ItemInnerPath as
            (
                select i.*, pti.fullpath, ARRAY[i.id] as path
                from ItemPath as pti join item as i on pti.id = i.id
                union 
                select i.*, iip.fullpath || i."name"::text, path || i.id
                from ItemInnerPath as iip join item as i on iip.id = i.parentid
            )
        select * from ItemInnerPath ORDER BY path, name
        """
    )
    List<ItemFullPathProjection> findInnerItems(@Param("path") String[] path);

    @NativeQuery("""
        with 
            --Последовательная иерархия до элемента
            recursive PathToItem as
            (
                select i.*, CAST(:path AS text[]) as targetpath, ARRAY[i."name"]::text[] as fullpath, 1 as depth
                from item i 
                where i.parentid is null
                union
                select 
                    i.*, 
                    targetpath,
                    fullpath || i."name"::text,
                    depth + 1
                from PathToItem pti join item i on i.parentid = pti.id 
                where targetpath[depth+1] = i."name"::text
            )
        SELECT pti.fullpath, i.* FROM PathToItem pti join item i on pti.id = i.id
        where depth = array_length(targetpath,1) 
            """)
    ItemFullPathProjection findItem(@Param("path") String[] path);

    @NativeQuery(
        """
        with RECURSIVE
            -- Самая глубокая уже существующей запись
            Existing AS (
            SELECT eip.*, CAST(:path AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
            FROM ExistingItemPath(CAST(:path AS text[])) as eip
            ORDER BY array_length(eip.fullpath, 1) desc LIMIT 1
            ),
            -- Если количество элементов полного пути больше, чем глубина существующего узла
            Missing AS (
                select e.targetpath as targetpath, e.id AS base_id, e.depth AS base_depth
                from Existing e
                WHERE array_length(e.targetpath, 1) > e.depth
            ),
            -- Набор новых элементов для вставки
            NewItems AS (
                select
                    gen_random_uuid() as id,						-- id нового узла
                    m.targetpath[m.base_depth + 1] as name,     	-- имя нового узла
                    m.base_id as base_id,                       	-- родительский id
                    m.base_depth+1 as base_depth,					-- глубина нового узла
                    array_length( m.targetpath,1) as total_parts, 	-- полный путь
                    CASE 
                        WHEN array_length( m.targetpath,1) - m.base_depth > 1 
                        THEN true   -- если после него ещё есть элементы – создаём папку
                      ELSE :file  
                    end as isfolder,
                    CASE 
                        WHEN array_length( m.targetpath,1) - m.base_depth > 1 
                        THEN 0   -- если после него ещё есть элементы – создаём папку
                      ELSE :size  -- иначе – файл (финальный элемент),
                    end as size,
                    m.targetpath as targetpath
                FROM Missing m
                
                union 
                
                SELECT 
                    gen_random_uuid(),
                    i_s.targetpath[i_s.base_depth + 1],    	
                    i_s.id,							
                    i_s.base_depth + 1,						
                    i_s.total_parts,
                    CASE 
                            WHEN total_parts - i_s.base_depth > 1 
                            THEN true   
                            ELSE :file  
                    end as isfolder,
                    CASE 
                        WHEN total_parts - i_s.base_depth > 1 
                        THEN 0   -- если после него ещё есть элементы – создаём папку
                        ELSE :size
                    end as size,
                    i_s.targetpath as targetpath
                FROM NewItems i_s
                WHERE i_s.base_depth < i_s.total_parts
            ),
            --  Вставка новых элементов
            InsertItems as
            (
                INSERT INTO item(id, name, parentid, isfolder, size)
                    select ni.id, ni.name, ni.base_id, ni.isfolder, ni.size
                    from NewItems as ni
                    order by ni.base_depth asc
                returning *
            )
        select * from InsertItems
        """
    )
    List<ItemProjection> insertItemRecursive(@Param("path") String[] path, @Param("file") boolean file, @Param("size") Long size);

    @NativeQuery(
        """
        with RECURSIVE
            Existing AS (
                SELECT eip.*, CAST(:path AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
                FROM ExistingItemPath(CAST(:path AS text[])) as eip
                ORDER BY depth desc LIMIT 1
            ),
            ParentItem AS (
                SELECT i.*, e.targetpath as targetpath, e.depth AS base_depth
                FROM Existing e join item i on i.id = e.id
                WHERE array_length(e.targetpath, 1) - 1 = e.depth and i.isfolder = true
            ),
            NewItem as (
                INSERT INTO item(name, parentid, isfolder, size)
                    select 
                        targetpath[array_length(pi.targetpath,1)], 
                        pi.id, 
                        CASE 
                            WHEN array_length(pi.targetpath,1) - pi.base_depth > 1 
                            THEN true   -- если после него ещё есть элементы – создаём папку
                            ELSE :file  -- иначе – файл 
                        end as isfolder,
                        CASE 
                            WHEN array_length(pi.targetpath,1) - pi.base_depth > 1 
                            THEN 0   
                            else :size 
                        end as size
                    from ParentItem as pi
                returning *
            )
        select * from NewItem
        """
    )
    ItemProjection insertItem(@Param("path") String[] path, @Param("file") boolean file, @Param("size") Long size);

    @NativeQuery(
        """
        with
            Existing AS (
                SELECT eip.*, CAST(:path AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
                FROM ExistingItemPath(CAST(:path AS text[])) as eip
                ORDER BY depth desc LIMIT 1
            )
            delete from item as i
            USING Existing as e
            where i.id = e.id 
                and array_length(e.targetpath, 1) = e.depth
            returning *
        """
    )
    ItemProjection removeItem(@Param("path") String[] path);

    @NativeQuery
    (
        """
        with recursive
            Existing AS (
                SELECT eip.*, CAST(:path AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
                FROM ExistingItemPath(CAST(:path AS text[])) as eip
                ORDER BY depth desc LIMIT 1
            ),
            TargetItem as (
                select i.*
                from Existing as e join item i on e.id = i.id
                where array_length(e.targetpath, 1) = e.depth
            ),
            InnerItems as (
                select ti.*, 1 as depth 
                from TargetItem as ti
                union
                select i.*, ii.depth + 1 
                from InnerItems as ii join item i on i.parentid = ii.id
            ),
            ItemsToDelete as (
                select * from InnerItems as ii
                    order by ii.depth desc
            )
            delete from item as i
                USING ItemsToDelete as itd
                where i.id = itd.id 
            returning *
        """
    )
    List<ItemProjection> removeItemsRecursive(@Param("path") String[] path);

    @NativeQuery(
        """
        with ItemPath1 as
        (
            SELECT eip.*, CAST(:path1 AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
            FROM ExistingItemPath(CAST(:path1 AS text[])) as eip
            ORDER BY depth desc LIMIT 1
        ),
        ItemPath2 as
        (
            SELECT eip.*, CAST(:path1 AS text[]) as targetpath, array_length(eip.fullpath, 1) as depth 
            FROM ExistingItemPath(CAST(:path2 AS text[])) as eip
            ORDER BY depth desc LIMIT 1
        ),
        ItemLevelPath1 as
        (
            select i.*, fullpath
            from ItemPath1 as pti join item as i on pti.id = i.id
            where i.isfolder = false 
            union 
            select i.*, fullpath || i."name"::text
            from ItemPath1 as pti join item as i on pti.id = i.parentid
            where i.isfolder = false 
        ),
        ItemLevelPath2 as
        (
            select i.*, fullpath
            from ItemPath2 as pti join item as i on pti.id = i.id
            where i.isfolder = false 
            union 
            select i.*, fullpath || i."name"::text
            from ItemPath2 as pti join item as i on pti.id = i.parentid
            where i.isfolder = false 
        )
        select ilp1.name as firstName, ilp2.name as secondName
        from ItemLevelPath1 ilp1 full join ItemLevelPath2 ilp2 on ilp1.name = ilp2.name
        """
    )
    List<ItemCompareProjection> compareElements(@Param("path1") String[] path1, @Param("path2") String[] path2);
}
