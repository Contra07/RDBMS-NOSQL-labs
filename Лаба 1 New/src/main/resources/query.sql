CREATE OR REPLACE FUNCTION ExistingItemPath(path text[])
RETURNS TABLE(
    id UUID,
    fullpath text[]
)
AS $$
begin
RETURN QUERY
WITH RECURSIVE PathToItem AS (
    -- Выбираем корневые записи
    SELECT 
        i.id as itemid, 
        path targetpath, 
        ARRAY[i."name"]::text[] AS existingpath,
        1 AS depth
    FROM item i 
    WHERE i.parentid IS NULL
    UNION
    -- Выбираем дочерние узлы, соответствующие части targetpath
    SELECT 
        i.id, 
        targetpath,
        existingpath || i."name"::text,
        depth + 1
    FROM PathToItem pti
    JOIN item i ON i.parentid = pti.itemid 
    WHERE targetpath[depth + 1] = i."name"::text
)
SELECT itemid AS id, existingpath AS fullpath
FROM PathToItem;
END;
$$ LANGUAGE plpgsql STABLE;


--пути рекурсивно

with RECURSIVE fullpath (id, isfolder, size, path)
as (
	select i.id, i.isfolder, i."size", CONCAT('/', i."name")
	from item i where i.parentid is null
	union
	select i.id, i.isfolder, i."size", CONCAT(path,'/', i."name")
	from item i join fullpath f on i.parentid = f.id
)
select * from fullpath f;

--Дети рекурсивно

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
		select i.*, pti.fullpath
		from ItemPath as pti join item as i on pti.id = i.id
		union 
		select i.*, iip.fullpath || i."name"::text
		from ItemInnerPath as iip join item as i on iip.id = i.parentid
	)
select * from ItemInnerPath


--Дети

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
select * from ItemLevelPath

--Найти путь по id
CREATE OR REPLACE FUNCTION FullPath(p_id uuid)
RETURNS TABLE(
    id uuid,
    fullpath text[]
)
AS $$
BEGIN
    RETURN QUERY
    WITH RECURSIVE FullPath AS (
        -- Начальная выборка: выбираем запись с заданным id
        SELECT 
            i.id, 
            i.parentid,
            i.id as beginid,
            array[i."name"]::text[] AS fullpath, 
            1 AS depth
        FROM item i
        WHERE i.id = p_id

        UNION ALL

        -- Рекурсивная часть: поднимаемся по иерархии, добавляя имя родителя в начало массива
        SELECT 
            i.id, 
            i.parentid,
            fp.beginid as beginid,
            i."name" || fp.fullpath AS fullpath, 
            fp.depth + 1 AS depth
        FROM FullPath fp
        JOIN item i ON i.id = fp.parentid
    )
    SELECT fp.beginid, fp.fullpath
    FROM FullPath as fp
    ORDER BY depth desc
    LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;