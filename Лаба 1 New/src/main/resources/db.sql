-- Таблица Item: хранит файлы и папки
CREATE TABLE Item (
    -- Уникальный идентификатор элемента
    Id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    -- Имя элемента
    Name VARCHAR(255) NOT NULL,
    -- Является ли элемент папкой (true/false)
    IsFolder BOOLEAN NOT NULL DEFAULT false,
    -- Ссылка на родительскую папку
    ParentId UUID REFERENCES Item(Id) DEFAULT NULL,
    -- Дата создания элемента
    CreatedAt TIMESTAMP NOT NULL DEFAULT NOW(),
    -- Дата последнего изменения элемента
    UpdatedAt TIMESTAMP NOT NULL DEFAULT NOW(),
    -- Размер элемента (в байтах), только для файлов
    Size BIGINT DEFAULT NULL 
);

-- Таблица Process: описывает работающие процессы, включая связь с родительским процессом
CREATE TABLE Process (
    -- Уникальный идентификатор процесса
    Id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    -- Название процесса
    Name VARCHAR(255) NOT NULL,
    -- Ссылка на родительский процесс
    ParentId UUID REFERENCES Process(Id) DEFAULT NULL,
    -- Ссылка на родительский процесс
    StartedAt TIMESTAMP NOT NULL DEFAULT NOW(),
    -- Аргументы процесса, представленные в виде массива строк
    Arguments VARCHAR(250)[] DEFAULT NULL,
    -- Ссылка на исходный файл, обязательно для заполнения
    SourceFile UUID NOT NULL REFERENCES Item(Id)
);


CREATE TYPE FileAccessType AS ENUM ('Read', 'Write');

-- Таблица ProcessFiles: связь между процессами и занятами файлами
CREATE TABLE ProcessFile (
    -- Уникальный идентификатор записи
    Id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    -- Ссылка на процесс
    ProcessId UUID REFERENCES Process(Id) NOT NULL,
    -- Ссылка на файл
    ItemId UUID REFERENCES Item(Id) NOT NULL,
    -- Тип доступа
    Access FileAccessType NOT NULL
);


