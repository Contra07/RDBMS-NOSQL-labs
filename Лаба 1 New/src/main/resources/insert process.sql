-- Процесс для текстового редактора
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'TextEditorProcess', NULL, ARRAY['--edit', '--autosave'], 
     (SELECT Id FROM Item WHERE Name = 'word.exe' AND IsFolder = false));

-- Процесс для запуска Excel
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'ExcelProcess', NULL, ARRAY['--analyze'], 
     (SELECT Id FROM Item WHERE Name = 'excel.exe' AND IsFolder = false));

-- Процесс для PowerPoint
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'PowerPointProcess', NULL, ARRAY['--slideshow'], 
     (SELECT Id FROM Item WHERE Name = 'powerpoint.exe' AND IsFolder = false));

-- Процесс для запуска игры
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'GameLauncherProcess', NULL, ARRAY['--start'], 
     (SELECT Id FROM Item WHERE Name = 'game_launcher.exe' AND IsFolder = false));

-- Процесс для компилятора
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'CompilerProcess', NULL, ARRAY['--compile', '--optimize'], 
     (SELECT Id FROM Item WHERE Name = 'compiler.exe' AND IsFolder = false));

-- Процесс отладчика
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'DebuggerProcess', NULL, ARRAY['--debug'], 
     (SELECT Id FROM Item WHERE Name = 'debugger.exe' AND IsFolder = false));

-- Процесс для работы с изображением профиля
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'ImageProcessor', NULL, ARRAY['--resize', '--enhance'], 
     (SELECT Id FROM Item WHERE Name = 'profile_picture.png' AND IsFolder = false));

-- Процесс для работы с музыкой
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'MusicProcessor', NULL, ARRAY['--play', '--shuffle'], 
     (SELECT Id FROM Item WHERE Name = 'track1.mp3' AND IsFolder = false));

-- Процесс для видео
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'VideoProcessor', NULL, ARRAY['--convert', '--optimize'], 
     (SELECT Id FROM Item WHERE Name = 'tutorial_video.mkv' AND IsFolder = false));

-- Процесс для работы с документами
INSERT INTO Process (Id, Name, ParentId, Arguments, SourceFile) VALUES
    (gen_random_uuid(), 'DocumentProcessor', NULL, ARRAY['--read', '--print'], 
     (SELECT Id FROM Item WHERE Name = 'Resume.docx' AND IsFolder = false));
